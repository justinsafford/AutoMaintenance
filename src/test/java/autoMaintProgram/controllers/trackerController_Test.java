package autoMaintProgram.controllers;

import autoMaintProgram.ResourcesNotFoundException;
import autoMaintProgram.tracker.TrackerController;
import autoMaintProgram.tracker.TrackerEntity;
import autoMaintProgram.repos.TrackerRepository;
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

import static org.hamcrest.core.Is.isA;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class trackerController_Test {

    @Mock
    VehicleRepository vehicleRepository;

    @Mock
    TrackerRepository trackerRepository;

    @InjectMocks
    TrackerController trackerController;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    MockMvc mockMvc;

    @Before
    public void setupMock() {
        mockMvc = standaloneSetup(trackerController)
                .build();
    }

    @Test
    public void addTracker() throws Exception {
        VehicleEntity vehicleEntity = new VehicleEntity();
        when(vehicleRepository.findOne("vId")).thenReturn(vehicleEntity);

        TrackerEntity expectedTracker = new TrackerEntity();
        when(trackerRepository.save(any(TrackerEntity.class))).thenReturn(expectedTracker);

        mockMvc.perform(post("/vehicles/{vehicleId}/tracker", "vId")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isCreated());

        verify(vehicleRepository, times(1)).findOne("vId");
        verify(trackerRepository, times(1)).save(Matchers.isA(TrackerEntity.class));
        verifyNoMoreInteractions(trackerRepository, vehicleRepository);
    }

    @Test
    public void addTrackerWithUnknownVehicleId_throwsRNFException() throws Exception {
        when(vehicleRepository.findOne("unknownVId")).thenReturn(null);

        expectedException.expectCause(isA(ResourcesNotFoundException.class));
        expectedException.expectMessage("Vehicle not found");

        mockMvc.perform(post("/vehicles/{vehicleId}/tracker", "unknownVId")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"));
    }

    @Test
    public void addTrackerWithMissingVehicleId_throwsRNFException() throws Exception {
        expectedException.expectCause(isA(ResourcesNotFoundException.class));
        expectedException.expectMessage("Vehicle not found");

        mockMvc.perform(post("/vehicles//tracker"));
    }
}