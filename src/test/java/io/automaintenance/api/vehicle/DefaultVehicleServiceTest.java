package io.automaintenance.api.vehicle;

import io.automaintenance.api.ResourcesNotFoundException;
import io.automaintenance.api.garage.GarageEntity;
import io.automaintenance.api.repos.GarageRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.isA;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultVehicleServiceTest {
    @Mock
    private GarageRepository garageRepository;

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

        defaultVehicleService.addNewVehicle(any(VehicleRequest.class), "garage-id");
    }

    @Test
    public void addNewVehicleWithUnknownGarage_throwsRNFException() throws Exception {
        when(garageRepository.findOne(anyString())).thenReturn(null);

        expectedException.expect(isA(ResourcesNotFoundException.class));
        expectedException.expectMessage("Garage not found");

        defaultVehicleService.addNewVehicle(any(VehicleRequest.class), "garage-id");
    }
}
