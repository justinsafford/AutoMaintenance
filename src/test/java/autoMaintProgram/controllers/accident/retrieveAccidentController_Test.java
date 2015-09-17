package autoMaintProgram.controllers.accident;

import autoMaintProgram.ResourcesNotFoundException;
import autoMaintProgram.accident.AccidentController;
import autoMaintProgram.accident.AccidentEntity;
import autoMaintProgram.repos.AccidentRepository;
import autoMaintProgram.repos.VehicleRepository;
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

import static org.hamcrest.CoreMatchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class retrieveAccidentController_Test {

    @Mock
    AccidentRepository accidentRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    AccidentController accidentController;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    MockMvc mockMvc;

    @Before
    public void setupMock() {
        mockMvc = standaloneSetup(accidentController)
                .build();
    }

    @Test
    public void retrieveAllAccidentsByVehicleId() throws Exception {
        List<AccidentEntity> accidentEntityList = new ArrayList();
        when(accidentRepository.findAllByVehicleId("vId")).thenReturn(accidentEntityList);

        mockMvc.perform(get("/vehicles/{vehicleId}/accidents", "vId")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());

        verify(accidentRepository, times(1)).findAllByVehicleId("vId");
        verifyNoMoreInteractions(accidentRepository);
    }

    @Test
    public void retrieveAccidentsWithUnknownVehicleId_throwsRNFException() throws Exception {
        when(accidentRepository.findAllByVehicleId("unknownVId")).thenReturn(null);

        expectedException.expectCause(isA(ResourcesNotFoundException.class));
        expectedException.expectMessage("Accidents not found");

        mockMvc.perform(get("/vehicles/{vehicleId}/accidents", "unknownVId")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isCreated());
    }

    @Test
    public void retrieveAccidentsWithMissingVehicleId_throwsRNFException() throws Exception {
        expectedException.expectCause(isA(ResourcesNotFoundException.class));
        expectedException.expectMessage("Vehicle is required");

        mockMvc.perform(get("/vehicles//accidents"));
    }
}
