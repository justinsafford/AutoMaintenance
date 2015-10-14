package io.automaintenance.api.vehicle;

import io.automaintenance.api.ResourcesNotFoundException;
import io.automaintenance.api.garage.GarageEntity;
import io.automaintenance.api.repos.GarageRepository;
import io.automaintenance.api.repos.VehicleRepository;
import org.hamcrest.core.Is;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.isA;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultVehicleServiceTest {
    @Mock
    private GarageRepository garageRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private VehicleResponseMapper vehicleResponseMapper;

    @InjectMocks
    private DefaultVehicleService defaultVehicleService;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void createVehicleAndPersistToDatabase() {
        when(garageRepository.findOne("garage-id"))
                .thenReturn(new GarageEntity());

        VehicleResponse vehicleResponse = new VehicleResponse();
        when(vehicleResponseMapper.map(any(VehicleRequest.class), anyString(), anyString()))
                .thenReturn(vehicleResponse);

        when(vehicleRepository.save(vehicleResponse))
                .thenReturn(vehicleResponse);

        defaultVehicleService.addVehicle(any(VehicleRequest.class), "garage-id");

        verify(garageRepository, times(1)).findOne(anyString());
        verify(vehicleRepository, times(1)).save(any(VehicleResponse.class));
    }

    @Test
    public void findVehicleInGarageReturnsVehicle() {
        when(vehicleRepository.findFirstByGarageIdAndVehicleId("garage-id", "vehicle-id"))
                .thenReturn(new VehicleResponse());

        defaultVehicleService.findVehicle("garage-id", "vehicle-id");

        verify(vehicleRepository, times(1)).findFirstByGarageIdAndVehicleId(anyString(), anyString());

        verify(vehicleRepository, times(1)).findFirstByGarageIdAndVehicleId("garage-id", "vehicle-id");
        verifyNoMoreInteractions(vehicleRepository);
    }

    @Test
    public void addNewVehicleWithUnknownGarage_throwsRNFException() throws Exception {
        when(garageRepository.findOne(anyString())).thenReturn(null);

        expectedException.expect(isA(ResourcesNotFoundException.class));
        expectedException.expectMessage("Garage not found");

        defaultVehicleService.addVehicle(any(VehicleRequest.class), "garage-id");
    }

    @Test
    public void retrieveVehicleWithUnknownGarageId_throwsRNFException() {
        when(vehicleRepository.findFirstByGarageIdAndVehicleId("garage-id", "vehicle-id"))
                .thenReturn(null);

        expectedException.expect(isA(ResourcesNotFoundException.class));
        expectedException.expectMessage("Vehicle not found");

        defaultVehicleService.findVehicle(null, "vehicle-id");
    }

    @Test
    public void retrieveVehicleWithUnknownVehicleId_throwsRNFException() {
        when(vehicleRepository.findFirstByGarageIdAndVehicleId("garage-id", "vehicle-id"))
                .thenReturn(null);

        expectedException.expect(isA(ResourcesNotFoundException.class));
        expectedException.expectMessage("Vehicle not found");

        defaultVehicleService.findVehicle("garage-id", null);
    }

    @Test
    public void retrieveMultipleVehiclesFromGarage() throws Exception {
        List<VehicleResponse> vehicleResponseList = new ArrayList();
        when(vehicleRepository.findAllByGarageId("gId")).thenReturn(vehicleResponseList);

        defaultVehicleService.findAllVehicles("gId");

        verify(vehicleRepository, times(1)).findAllByGarageId("gId");
        verifyNoMoreInteractions(vehicleRepository);
    }

    @Test
    public void retrieveMultipleVehiclesWithUnknownVehiclesInGarage_throwsRNFException() throws Exception {
        when(vehicleRepository.findAllByGarageId("gId")).thenReturn(null);

        expectedException.expect(Is.isA(ResourcesNotFoundException.class));
        expectedException.expectMessage("Vehicle not found");

        defaultVehicleService.findAllVehicles("gId");
    }

    @Test
    public void editVehicleFromGarage() throws Exception {
        VehicleResponse vehicleResponse = new VehicleResponse();
        when(vehicleRepository.findFirstByGarageIdAndVehicleId("gId", "vId")).thenReturn(vehicleResponse);

        when(vehicleResponseMapper.map(any(VehicleRequest.class), anyString(), anyString())).thenReturn(vehicleResponse);

        VehicleRequest vehicleRequest = new VehicleRequest();
        defaultVehicleService.editVehicle(vehicleRequest, "gId", "vId");

        verify(vehicleRepository, times(1)).findFirstByGarageIdAndVehicleId("gId", "vId");
        verify(vehicleRepository, times(1)).save(Matchers.isA(VehicleResponse.class));
        verify(vehicleResponseMapper, times(1)).map(any(VehicleRequest.class), anyString(), anyString());
        verifyNoMoreInteractions(vehicleRepository);
    }

    @Test
    public void editUnknownVehicle_throwsRNFException() throws Exception {
        when(vehicleRepository.findFirstByGarageIdAndVehicleId("gId", "vId")).thenReturn(null);

        expectedException.expect(isA(ResourcesNotFoundException.class));
        expectedException.expectMessage("Vehicle not found");

        defaultVehicleService.editVehicle(new VehicleRequest(), "gId", "vId");
    }
}
