package io.automaintenance.api.integration.tracker;

import io.automaintenance.api.Application;
import io.automaintenance.api.repos.TrackerRepository;
import io.automaintenance.api.repos.VehicleRepository;
import io.automaintenance.api.tracker.TrackerEntity;
import io.automaintenance.api.vehicle.VehicleEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class RetrieveTrackerTest {

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    TrackerRepository trackerRepository;

    @Autowired
    WebApplicationContext webApplicationContext;
    MockMvc mockMvc;

    @Before
    public void setupMock() {
        mockMvc = webAppContextSetup(webApplicationContext)
                .defaultRequest(get("/"))
                .alwaysExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .build();
    }

    @Before
    public void clearDb() {
        trackerRepository.deleteAll();
        vehicleRepository.deleteAll();
    }

    @Test
    public void retrieveMultipleAccidents() throws Exception {
        VehicleEntity vehicleEntity = new VehicleEntity();
        vehicleEntity.setVehicleId("vId");

        vehicleRepository.save(vehicleEntity);

        TrackerEntity trackerEntity = new TrackerEntity();
        trackerEntity.setVehicleId("vId");
        trackerEntity.setName("name1");
        trackerEntity.setDescription("description1");
        trackerEntity.setPriority("High");
        trackerEntity.setPendingStatus(true);


        TrackerEntity trackerEntity2 = new TrackerEntity();
        trackerEntity2.setVehicleId("vId");
        trackerEntity2.setName("name2");
        trackerEntity2.setDescription("description2");
        trackerEntity2.setPriority("Low");
        trackerEntity2.setPendingStatus(false);

        TrackerEntity trackerEntity3 = new TrackerEntity();
        trackerEntity3.setVehicleId("differentVid");

        trackerRepository.save(trackerEntity);
        trackerRepository.save(trackerEntity2);
        trackerRepository.save(trackerEntity3);

        ClassPathResource classPathResource = new ClassPathResource("responses/retrieveTrackers.json");
        String expectedBody = new String(Files.readAllBytes(Paths.get(classPathResource.getURI())));

        mockMvc.perform(get("/vehicles/{vehicleId}/tracker", "vId")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(expectedBody))
                .andExpect(status().isOk());

        assertThat(vehicleRepository.count(), is(1L));
        assertThat(trackerRepository.count(), is(3L));
    }
}
