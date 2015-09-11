package autoMaintProgram;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
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

public class vehicleControllerTest1 {
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
    public void addNewVehicleToGarage() throws Exception {
        GarageEntity garageEntity = new GarageEntity();
        garageEntity.setGarageId("id");
        garageEntity.setGarageName("testName");

        GarageEntity expectedGarageEntity = new GarageEntity();
        when(garageRepository.findOne(garageEntity.getGarageId()))
                .thenReturn(expectedGarageEntity);

        mockMvc.perform(post("/garages/{garageId}/vehicles", garageEntity.getGarageId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isCreated());

        verify(vehicleRepository, times(1)).save(Matchers.isA(VehicleEntity.class));
        verifyNoMoreInteractions(vehicleRepository);
        //change
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
    public void retrieveVehicleWithMismatchingIds_throwsNotFoundException() throws Exception {
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
    public void retrieveMultipleVehiclesWithNoVehiclesInGarage_ReturnNotFound() throws Exception {
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
    public void retrieveMultipleVehiclesWithNoGarageId_ReturnNotFound() throws Exception {
        expectedException.expectCause(isA(ResourcesNotFoundException.class));
        expectedException.expectMessage("Garage is required");

        mockMvc.perform(get("/garages//vehicles")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addNewVehicleWithUnknownGarage_ReturnNotFound() throws Exception {
        GarageEntity garageEntity = new GarageEntity();
        when(garageRepository.findOne(garageEntity.getGarageId())).thenReturn(null);

        expectedException.expectCause(isA(ResourcesNotFoundException.class));
        expectedException.expectMessage("GarageId not found");

        mockMvc.perform(post("/garages/{garageId}/vehicles", "id")
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    public void addNewVehicleWithMissingGarage_ReturnNotFound() throws Exception {
        expectedException.expectCause(isA(ResourcesNotFoundException.class));
        expectedException.expectMessage("GarageId is required");

        mockMvc.perform(post("/garages//vehicles")
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void deleteGarage() throws Exception {
        VehicleEntity vehicleEntity = new VehicleEntity();
        when(vehicleRepository.findFirstByGarageIdAndVehicleId("gId", "vId"))
                .thenReturn(vehicleEntity);

        mockMvc.perform(delete("/garages/{gId}/vehicles/{vId}", "gId", "vId")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andReturn();

        verify(vehicleRepository, times(1)).findFirstByGarageIdAndVehicleId("gId", "vId");
        verify(vehicleRepository, times(1)).delete("vId");
        verifyNoMoreInteractions(vehicleRepository);
    }

    @Test
    public void deleteGarageWithUnknownVehicleId_ReturnNotFound() throws Exception {
        when(vehicleRepository.findFirstByGarageIdAndVehicleId("gId", "vId"))
                .thenReturn(null);

        expectedException.expectCause(isA(ResourcesNotFoundException.class));
        expectedException.expectMessage("Vehicle not found");

        mockMvc.perform(delete("/garages/{garageId}/vehicles/{vehicleId}", "garageId", "vehicleId")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andReturn();
    }

}
