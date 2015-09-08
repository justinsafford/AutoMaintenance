package autoMaintProgram.integrationTests;

import autoMaintProgram.Application;
import autoMaintProgram.GarageController;
import autoMaintProgram.GarageEntity;
import autoMaintProgram.GarageRepository;
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
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class retrieveGarage {

    @Autowired
    GarageRepository garageRepository;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    GarageController garageController;

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
        garageRepository.deleteAll();
    }

    @Test
    public void retrieveGarageById() throws Exception {
        ClassPathResource classPathResource = new ClassPathResource("responses/retrieveGarage.json");
        String expectedJson = new String(Files.readAllBytes(Paths.get(classPathResource.getURI())));

        GarageEntity expectedGarage = new GarageEntity();
        UUID garageUuid = UUID.randomUUID();
        expectedGarage.setGarageId(UUID.randomUUID());
        expectedGarage.setGarageName("Justin");

        garageRepository.save(expectedGarage);

        mockMvc.perform(get("/garages/{id}", garageUuid))
                .andExpect(content().json(expectedJson))
                .andExpect(status().isOk());
    }
}
