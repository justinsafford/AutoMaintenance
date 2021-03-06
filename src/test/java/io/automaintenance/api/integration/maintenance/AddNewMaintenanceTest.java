package io.automaintenance.api.integration.maintenance;

import io.automaintenance.api.Application;
import io.automaintenance.api.maintenance.MaintenanceEntity;
import io.automaintenance.api.repos.MaintenanceRepository;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class AddNewMaintenanceTest {

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    MaintenanceRepository maintenanceRepository;

    @Autowired
    WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

    @Before
    public void setupMock() {
        mockMvc = webAppContextSetup(webApplicationContext)
                .defaultRequest(post("/")
                        .accept(MediaType.APPLICATION_JSON))
                .alwaysExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .build();
    }

    @Before
    public void clearDb() {
        vehicleRepository.deleteAll();
        maintenanceRepository.deleteAll();
    }

    @Test
    public void addNewGarage() throws Exception {
        ClassPathResource classPathResource = new ClassPathResource("requests/addMaintenance.json");
        String request = new String(Files.readAllBytes(Paths.get(classPathResource.getURI())));

        VehicleResponse vehicleResponse = new VehicleResponse();
        vehicleResponse.setVehicleId("vId");
        vehicleRepository.save(vehicleResponse);

        mockMvc.perform(post("/vehicles/{vehicleId}/maintenance", "vId")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.vehicleId", is("vId")))
                .andExpect(jsonPath("$.type", is("Change Oil")))
                .andExpect(jsonPath("$.description", is("Change oil interval")))
                .andExpect(jsonPath("$.mileageExpected", is("50,000")))
                .andExpect(status().isCreated())
                .andReturn();

        List<MaintenanceEntity> maintenanceEntityList = maintenanceRepository.findAll();
        assertThat(maintenanceEntityList.size(), is(1));
    }
}
