package io.automaintenance.api.vehicle;

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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class EditVehicleControllerTest {
    @Mock
    private VehicleService vehicleService;

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
    public void editVehicleFromGarage() throws Exception {
        VehicleRequest vehicleRequest = new VehicleRequest();
        VehicleResponse vehicleResponse = new VehicleResponse();
        when(vehicleService.editVehicle(vehicleRequest, "gId", "vId")).thenReturn(vehicleResponse);

        mockMvc.perform(put("/garages/{garageId}/vehicles/{vehicleId}", "gId", "vId")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isAccepted());

        verify(vehicleService, times(1)).editVehicle(any(VehicleRequest.class), anyString(), anyString());
    }
}
