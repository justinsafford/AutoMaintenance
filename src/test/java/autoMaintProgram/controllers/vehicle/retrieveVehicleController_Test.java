package autoMaintProgram.controllers.vehicle;

import autoMaintProgram.ResourcesNotFoundException;
import autoMaintProgram.garage.GarageEntity;
import autoMaintProgram.repos.GarageRepository;
import autoMaintProgram.repos.VehicleRepository;
import autoMaintProgram.vehicle.VehicleController;
import autoMaintProgram.vehicle.VehicleEntity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.isA;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class retrieveVehicleController_Test {
    @Mock
    VehicleRepository vehicleRepository;

    @Mock
    GarageRepository garageRepository;

    @InjectMocks
    VehicleController vehicleController;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    MockMvc mockMvc;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setupMock() {
        mockMvc = standaloneSetup(vehicleController)
                .build();
    }

    @Test
    public void retrieveVehicleFromGarage() throws Exception {
        VehicleEntity vehicleEntity = new VehicleEntity();
        when(vehicleRepository.findFirstByGarageIdAndVehicleId("gId", "vId")).thenReturn(vehicleEntity);

        mockMvc.perform(get("/garages/{garageId}/vehicles/{vehicleId}", "gId", "vId")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());

        verify(vehicleRepository, times(1)).findFirstByGarageIdAndVehicleId("gId", "vId");
        verifyNoMoreInteractions(vehicleRepository);
    }

    @Test
    public void retrieveMultipleVehiclesFromGarage() throws Exception {
        List<VehicleEntity> vehicleEntityList = new ArrayList<>();
        when(vehicleRepository.findAllByGarageId("gId")).thenReturn(vehicleEntityList);

        mockMvc.perform(get("/garages/{garageId}/vehicles", "gId")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());

        verify(vehicleRepository, times(1)).findAllByGarageId("gId");
        verifyNoMoreInteractions(vehicleRepository);
    }

    @Test
    public void retrieveMultipleVehiclesWithUnknownVehiclesInGarage_throwsRNFException() throws Exception {
        when(vehicleRepository.findAllByGarageId("gId")).thenReturn(null);

        expectedException.expectCause(isA(ResourcesNotFoundException.class));
        expectedException.expectMessage("Vehicle not found");

        mockMvc.perform(get("/garages/{garageId}/vehicles", "gId")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    public void retrieveVehicleWithMismatchingIds_throwsRNFException() throws Exception {
        GarageEntity garageEntity = new GarageEntity();
        when(garageRepository.findOne("gId")).thenReturn(garageEntity);

        when(vehicleRepository.findFirstByGarageIdAndVehicleId("gId", "vId")).thenReturn(null);

        expectedException.expectCause(isA(ResourcesNotFoundException.class));
        expectedException.expectMessage("Vehicle not found");

        mockMvc.perform(get("/garages/{garageId}/vehicles/{vehicleId}", "gId", "vId")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    public void retrieveMultipleVehiclesWithNoGarageId_ReturnNotFound() throws Exception {
        expectedException.expectCause(isA(ResourcesNotFoundException.class));
        expectedException.expectMessage("Garage is required");

        mockMvc.perform(get("/garages//vehicles"));
    }
}
