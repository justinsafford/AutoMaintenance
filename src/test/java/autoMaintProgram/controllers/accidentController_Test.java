package autoMaintProgram.controllers;

import autoMaintProgram.AccidentController;
import autoMaintProgram.AccidentEntity;
import autoMaintProgram.AccidentRepository;
import autoMaintProgram.ResourcesNotFoundException;
import autoMaintProgram.repos.VehicleRepository;
import autoMaintProgram.vehicle.VehicleEntity;
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

import static org.hamcrest.CoreMatchers.isA;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class accidentController_Test {

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
    public void addAccident() throws Exception {
        VehicleEntity vehicleEntity = new VehicleEntity();
        when(vehicleRepository.findOne("vId")).thenReturn(vehicleEntity);

        AccidentEntity expectedAccident = new AccidentEntity();
        when(accidentRepository.save(any(AccidentEntity.class))).thenReturn(expectedAccident);

        mockMvc.perform(post("/vehicles/{vehicleId}/accidents", "vId")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isCreated());

        verify(vehicleRepository, times(1)).findOne("vId");
        verify(accidentRepository, times(1)).save(Matchers.isA(AccidentEntity.class));
        verifyNoMoreInteractions(accidentRepository, vehicleRepository);
    }

    @Test
    public void addAccidentWithUnknownVehicleId_throwsRNFException() throws Exception {
        when(vehicleRepository.findOne("unknownVId")).thenReturn(null);

        expectedException.expectCause(isA(ResourcesNotFoundException.class));
        expectedException.expectMessage("Vehicle not found");

        mockMvc.perform(post("/vehicles/{vehicleId}/accidents", "unknownVId")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isCreated());
    }

    @Test
    public void addAccidentWithMissingVehicleId_throwsRNFException() throws Exception {
        expectedException.expectCause(isA(ResourcesNotFoundException.class));
        expectedException.expectMessage("Vehicle not found");

        mockMvc.perform(post("/vehicles//accidents"));
    }
}
