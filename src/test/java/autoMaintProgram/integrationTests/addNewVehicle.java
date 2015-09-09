package autoMaintProgram.integrationTests;

import autoMaintProgram.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class addNewVehicle {
    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    GarageRepository garageRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    VehicleController vehicleController;

    MockMvc mockMvc;

    @Before
    public void setupMock() {
        mockMvc = webAppContextSetup(webApplicationContext)
                .defaultRequest(get("/")
                        .accept(MediaType.APPLICATION_JSON))
                .alwaysExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .build();
    }

    @Before
    public void clearDb() {
        garageRepository.deleteAll();
        vehicleRepository.deleteAll();
    }

    @Test
    public void addNewVehicle_Success() throws Exception {
//        ClassPathResource classPathResource = new ClassPathResource("requests/addVehicle.json");
//        String request = new String(Files.readAllBytes(Paths.get(classPathResource.getURI())));

        GarageEntity garageEntity = new GarageEntity();
        String garageUuid = UUID.randomUUID().toString();
        garageEntity.setGarageId(garageUuid);
        garageEntity.setGarageName("Justin");
        garageRepository.save(garageEntity);

//        mockMvc.perform(post("/garages/{garageId}/vehicles", garageEntity.getGarageId())
//                .content(request)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated())
//                .andReturn();

        VehicleRequest vehicleRequest = new VehicleRequest();
        vehicleRequest.setVehicleName("Tito");
        vehicleRequest.setVehicleYear("2014");
        vehicleRequest.setVehicleMake("Chevy");
        vehicleRequest.setVehicleModel("Silverado");

        vehicleController.addNewVehicle(garageEntity.getGarageId(), vehicleRequest);

        List<VehicleEntity> vehicleEntityList = vehicleRepository.findAll();
        assertThat(vehicleEntityList.size(), is(1));
        VehicleEntity savedVehicle = vehicleEntityList.get(0);

        assertThat(savedVehicle.getGarageId(), is(garageUuid));
        assertThat(savedVehicle.getVehicleId(), isA(String.class));
        assertThat(savedVehicle.getVehicleName(), is("Tito"));
        assertThat(savedVehicle.getVehicleYear(), is("2014"));
        assertThat(savedVehicle.getVehicleMake(), is("Chevy"));
        assertThat(savedVehicle.getVehicleModel(), is("Silverado"));
    }
}
