package io.automaintenance.api.integration.vehicle;

import io.automaintenance.api.Application;
import io.automaintenance.api.garage.GarageEntity;
import io.automaintenance.api.repos.GarageRepository;
import io.automaintenance.api.repos.VehicleRepository;
import io.automaintenance.api.vehicle.VehicleResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class AddNewVehicleTest {
    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    GarageRepository garageRepository;

    @Autowired
    VehicleRepository vehicleRepository;

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
    public void addNewVehicle() throws Exception {
        ClassPathResource classPathResource = new ClassPathResource("requests/addVehicle.json");
        String request = new String(Files.readAllBytes(Paths.get(classPathResource.getURI())));

        GarageEntity garageEntity = new GarageEntity();
        String garageUuid = UUID.randomUUID().toString();
        garageEntity.setGarageId(garageUuid);
        garageEntity.setGarageName("Justin");
        garageRepository.save(garageEntity);

        mockMvc.perform(post("/garages/{garageId}/vehicles", garageEntity.getGarageId())
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        List<VehicleResponse> vehicleResponseList = vehicleRepository.findAll();
        assertThat(vehicleResponseList.size(), is(1));
        VehicleResponse savedVehicle = vehicleResponseList.get(0);

        assertThat(savedVehicle.getGarageId(), is(garageUuid));
        assertThat(savedVehicle.getVehicleId(), isA(String.class));
        assertThat(savedVehicle.getName(), is("Tito"));
        assertThat(savedVehicle.getYear(), is("2014"));
        assertThat(savedVehicle.getMake(), is("Chevy"));
        assertThat(savedVehicle.getModel(), is("Silverado"));
    }
}
