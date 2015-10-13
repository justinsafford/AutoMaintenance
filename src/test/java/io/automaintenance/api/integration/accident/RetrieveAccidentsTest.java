package io.automaintenance.api.integration.accident;

import io.automaintenance.api.Application;
import io.automaintenance.api.accident.AccidentEntity;
import io.automaintenance.api.repos.AccidentRepository;
import io.automaintenance.api.repos.VehicleRepository;
import io.automaintenance.api.vehicle.VehicleResponse;
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
public class RetrieveAccidentsTest {

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    AccidentRepository accidentRepository;

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
        accidentRepository.deleteAll();
        vehicleRepository.deleteAll();
    }

    @Test
    public void retrieveMultipleAccidents() throws Exception {
        VehicleResponse vehicleResponse = new VehicleResponse();
        vehicleResponse.setVehicleId("vId");

        vehicleRepository.save(vehicleResponse);

        AccidentEntity accidentEntity1 = new AccidentEntity();
        accidentEntity1.setVehicleId("vId");
        accidentEntity1.setType("type1");
        accidentEntity1.setDescription("description1");
        accidentEntity1.setDamageLevel("damage1");

        AccidentEntity accidentEntity2 = new AccidentEntity();
        accidentEntity2.setVehicleId("vId");
        accidentEntity2.setType("type2");
        accidentEntity2.setDescription("description2");
        accidentEntity2.setDamageLevel("damage2");

        AccidentEntity accidentEntity3 = new AccidentEntity();
        accidentEntity3.setVehicleId("differentVid");

        accidentRepository.save(accidentEntity1);
        accidentRepository.save(accidentEntity2);
        accidentRepository.save(accidentEntity3);

        ClassPathResource classPathResource = new ClassPathResource("responses/retrieveAccidents.json");
        String expectedBody = new String(Files.readAllBytes(Paths.get(classPathResource.getURI())));

        mockMvc.perform(get("/vehicles/{vehicleId}/accidents", "vId"))
                .andExpect(content().json(expectedBody))
                .andExpect(status().isOk());

        assertThat(vehicleRepository.count(), is(1L));
        assertThat(accidentRepository.count(), is(3L));
    }
}
