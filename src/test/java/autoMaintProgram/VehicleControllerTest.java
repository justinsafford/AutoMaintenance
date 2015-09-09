package autoMaintProgram;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
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

        verify(vehicleRepository, times(1)).save(isA(VehicleEntity.class));
        verifyNoMoreInteractions(vehicleRepository);
    }

//    @Test
//    public void retrieveVehicleFromGarage() throws Exception {
//        mockMvc.perform(get("/garages/{id}", "id")
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("{}"))
//                .andExpect(status().isOk());
//
//        verify(garageRepository, times(1)).findOne("id");
//        verifyNoMoreInteractions(garageRepository);
//    }
}
