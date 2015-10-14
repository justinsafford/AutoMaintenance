package io.automaintenance.api.vehicle;

import io.automaintenance.api.ResourcesNotFoundException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.Is.isA;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class AddVehicleControllerTest {
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
    public void addNewVehicleToGarage() throws Exception {
        VehicleResponse vehicleResponse = new VehicleResponse();
        when(vehicleService.addVehicle(any(VehicleRequest.class), anyString()))
                .thenReturn(vehicleResponse);

        mockMvc.perform(post("/garages/{garageId}/vehicles", "garage-id")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isCreated());
    }

    @Test
    public void addNewVehicleWithMissingGarage_throwsRNFException() throws Exception {
        expectedException.expectCause(isA(ResourcesNotFoundException.class));
        expectedException.expectMessage("Garage is required");

        mockMvc.perform(post("/garages//vehicles"));
    }
}
