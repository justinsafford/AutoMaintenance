package io.automaintenance.api;

import io.automaintenance.api.ResourcesNotFoundException;
import io.automaintenance.api.maintenance.MaintenanceController;
import io.automaintenance.api.maintenance.MaintenanceEntity;
import io.automaintenance.api.repos.MaintenanceRepository;
import io.automaintenance.api.repos.VehicleRepository;
import io.automaintenance.api.vehicle.VehicleEntity;
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

public class maintenanceControllerTest {

    @Mock
    VehicleRepository vehicleRepository;

    @Mock
    MaintenanceRepository maintenanceRepository;

    @InjectMocks
    MaintenanceController maintenanceController;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    MockMvc mockMvc;

    @Before
    public void setupMock() {
        mockMvc = standaloneSetup(maintenanceController)
                .build();
    }

    @Test
    public void addMaintenance() throws Exception {
        VehicleEntity vehicleEntity = new VehicleEntity();
        when(vehicleRepository.findOne("vId")).thenReturn(vehicleEntity);

        MaintenanceEntity expectedMaintenance = new MaintenanceEntity();
        when(maintenanceRepository.save(any(MaintenanceEntity.class))).thenReturn(expectedMaintenance);

        mockMvc.perform(post("/vehicles/{vehicleId}/maintenance", "vId")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isCreated());

        verify(vehicleRepository, times(1)).findOne("vId");
        verify(maintenanceRepository, times(1)).save(Matchers.isA(MaintenanceEntity.class));
        verifyNoMoreInteractions(maintenanceRepository, vehicleRepository);
    }

    @Test
    public void addMaintenanceWithUnknownVehicleId_throwsRNFException() throws Exception {
        when(vehicleRepository.findOne("unknownVId")).thenReturn(null);

        expectedException.expectCause(isA(ResourcesNotFoundException.class));
        expectedException.expectMessage("Vehicle not found");

        mockMvc.perform(post("/vehicles/{vehicleId}/maintenance", "unknownVId")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"));
    }

    @Test
    public void addMaintenanceWithMissingVehicleId_throwsRNFException() throws Exception {
        expectedException.expectCause(isA(ResourcesNotFoundException.class));
        expectedException.expectMessage("Vehicle not found");

        mockMvc.perform(post("/vehicles//maintenance"));
    }
}
