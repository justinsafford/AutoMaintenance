package autoMaintProgram.integration.tracker;

import autoMaintProgram.Application;
import autoMaintProgram.tracker.TrackerEntity;
import autoMaintProgram.repos.TrackerRepository;
import autoMaintProgram.repos.VehicleRepository;
import autoMaintProgram.vehicle.VehicleEntity;
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
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class addNewTracker_Test {

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
                .defaultRequest(post("/")
                        .accept(MediaType.APPLICATION_JSON))
                .alwaysExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .build();
    }

    @Before
    public void clearDb() {
        vehicleRepository.deleteAll();
        trackerRepository.deleteAll();
    }

    @Test
    public void addNewTracker() throws Exception {
        ClassPathResource classPathResource = new ClassPathResource("requests/addTracker.json");
        String request = new String(Files.readAllBytes(Paths.get(classPathResource.getURI())));

        VehicleEntity vehicleEntity = new VehicleEntity();
        vehicleEntity.setVehicleId("vId");
        vehicleRepository.save(vehicleEntity);

        mockMvc.perform(post("/vehicles/{vehicleId}/tracker", "vId")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.vehicleId", is("vId")))
                .andExpect(jsonPath("$.name", is("Steering issue")))
                .andExpect(jsonPath("$.description", is("Truck dies while turning at low speeds")))
                .andExpect(jsonPath("$.priority", is("High")))
                .andExpect(jsonPath("$.pendingStatus", is(1)))
                .andExpect(status().isCreated())
                .andReturn();

        List<TrackerEntity> trackerEntityList = trackerRepository.findAll();
        assertThat(trackerEntityList.size(), is(1));
    }
}
