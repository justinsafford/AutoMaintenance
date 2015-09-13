package autoMaintProgram.integration.garage;

import autoMaintProgram.Application;
import autoMaintProgram.garage.GarageController;
import autoMaintProgram.garage.GarageEntity;
import autoMaintProgram.repos.GarageRepository;
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

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class retrieveGarageTest {

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
        GarageEntity expectedGarage = new GarageEntity();
        String garageUuid = UUID.randomUUID().toString();
        expectedGarage.setGarageId(garageUuid);
        expectedGarage.setGarageName("Justin");

        garageRepository.save(expectedGarage);

        GarageEntity actualGarage;
        actualGarage = garageController.retrieveGarage(garageUuid);

        assertThat(garageRepository.count(), is(1L));
        assertThat(actualGarage.getGarageId(), is(garageUuid));
        assertThat(actualGarage.getGarageName(), is("Justin"));
        //        ClassPathResource classPathResource = new ClassPathResource("responses/retrieveGarage.json");
        //        String expectedJson = new String(Files.readAllBytes(Paths.get(classPathResource.getURI())));
        //TODO:Get this test working - content type not set error..
//        mockMvc.perform(get("/garages/{id}", garageUuid))
//                .andExpect(content().json(expectedJson))
//                .andExpect(status().isOk());
    }
}
