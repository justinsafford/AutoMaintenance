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

import static org.hamcrest.core.Is.isA;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class vehicleControllerTest {
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
    }

    @Test
    public void retrieveVehicleFromGarage() throws Exception {
        GarageEntity garageEntity = new GarageEntity();
        when(garageRepository.findOne("gId")).thenReturn(garageEntity);
        VehicleEntity vehicleEntity = new VehicleEntity();
        when(vehicleRepository.findOne("vId")).thenReturn(vehicleEntity);

        mockMvc.perform(get("/garages/{garageId}/vehicles/{vehicleId}", "gId", "vId")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());

        verify(garageRepository, times(1)).findOne("gId");
        verify(vehicleRepository, times(1)).findOne("vId");
        verifyNoMoreInteractions(garageRepository, vehicleRepository);
    }

    @Test
    public void retrieveVehicleWithUnknownGarageId_throwsNotFoundException() throws Exception {
        when(garageRepository.findOne("gId")).thenReturn(null);
        VehicleEntity vehicleEntity = new VehicleEntity();
        when(vehicleRepository.findOne("vId")).thenReturn(vehicleEntity);

        expectedException.expectCause(isA(ResourcesNotFoundException.class));
        expectedException.expectMessage("GarageId was not found");

        mockMvc.perform(get("/garages/{garageId}/vehicles/{vehicleId}", "gId", "vId")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    public void retrieveVehicleWithUnknownVehicleId_throwsNotFoundException() throws Exception {
        GarageEntity garageEntity = new GarageEntity();
        when(garageRepository.findOne("gId")).thenReturn(garageEntity);
        when(vehicleRepository.findOne("vId")).thenReturn(null);

        expectedException.expectCause(isA(ResourcesNotFoundException.class));
        expectedException.expectMessage("VehicleId was not found");

        mockMvc.perform(get("/garages/{garageId}/vehicles/{vehicleId}", "gId", "vId")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());
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
        GarageEntity garageEntity = new GarageEntity();
        when(garageRepository.findOne(garageEntity.getGarageId())).thenReturn(null);

        expectedException.expectCause(isA(ResourcesNotFoundException.class));
        expectedException.expectMessage("GarageId is required");

        mockMvc.perform(post("/garages/{garageId}/vehicles", "")
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
