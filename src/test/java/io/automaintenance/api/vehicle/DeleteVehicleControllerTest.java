package io.automaintenance.api.vehicle;

import io.automaintenance.api.repos.GarageRepository;
import io.automaintenance.api.repos.VehicleRepository;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class DeleteVehicleControllerTest {
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
    public void deleteVehicle() throws Exception {
        VehicleResponse vehicleResponse = new VehicleResponse();
        when(vehicleRepository.findFirstByGarageIdAndVehicleId("gId", "vId"))
                .thenReturn(vehicleResponse);

        mockMvc.perform(delete("/garages/{gId}/vehicles/{vId}", "gId", "vId")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andReturn();
    }
}
