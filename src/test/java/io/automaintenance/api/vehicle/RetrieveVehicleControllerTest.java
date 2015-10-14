package io.automaintenance.api.vehicle;

import io.automaintenance.api.ResourcesNotFoundException;
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

import static org.hamcrest.core.Is.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class RetrieveVehicleControllerTest {
    @Mock
    VehicleService vehicleService;

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
        VehicleResponse vehicleResponse = new VehicleResponse();
        when(vehicleService.findVehicle("gId", "vId")).thenReturn(vehicleResponse);

        mockMvc.perform(get("/garages/{garageId}/vehicles/{vehicleId}", "gId", "vId")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    public void retrieveMultipleVehiclesFromGarage() throws Exception {
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

        mockMvc.perform(get("/garages//vehicles"));
    }
}
