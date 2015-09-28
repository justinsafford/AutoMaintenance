package io.automaintenance.api.integration.vehicle;

import io.automaintenance.api.Application;
import io.automaintenance.api.garage.GarageEntity;
import io.automaintenance.api.repos.GarageRepository;
import io.automaintenance.api.repos.VehicleRepository;
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
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class retrieveVehiclesTest {

    @Autowired
    GarageRepository garageRepository;

    @Autowired
    VehicleRepository vehicleRepository;

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
        garageRepository.deleteAll();
        vehicleRepository.deleteAll();
    }

    @Test
    public void retrieveVehicleById() throws Exception {
        GarageEntity expectedGarage = new GarageEntity();
        String garageUuid = UUID.randomUUID().toString();
        expectedGarage.setGarageId(garageUuid);
        expectedGarage.setGarageName("Justin");

        garageRepository.save(expectedGarage);

        VehicleEntity vehicleEntity = new VehicleEntity();

        vehicleEntity.setGarageId(UUID.randomUUID().toString());

        String vehicleUuid = UUID.randomUUID().toString();
        vehicleEntity.setVehicleId(vehicleUuid);
        vehicleEntity.setGarageId(garageUuid);
        vehicleEntity.setYear("2004");
        vehicleEntity.setMake("Chevy");
        vehicleEntity.setModel("Silverado");
        vehicleEntity.setName("Taylor");

        VehicleEntity vehicleEntity2 = new VehicleEntity();
        vehicleEntity2.setGarageId(UUID.randomUUID().toString());
        vehicleEntity2.setVehicleId(UUID.randomUUID().toString());
        vehicleEntity2.setYear("sfas");
        vehicleEntity2.setMake("asdfas");
        vehicleEntity2.setModel("sdafas");
        vehicleEntity2.setName("sdga");

        vehicleRepository.save(vehicleEntity);
        vehicleRepository.save(vehicleEntity2);

        mockMvc.perform(get("/garages/{garageId}/vehicles/{vehicleId}", garageUuid, vehicleUuid))
                .andExpect(jsonPath("$.garageId", is(garageUuid)))
                .andExpect(jsonPath("$.vehicleId", is(vehicleUuid)))
                .andExpect(jsonPath("$.year", is("2004")))
                .andExpect(jsonPath("$.make", is("Chevy")))
                .andExpect(jsonPath("$.model", is("Silverado")))
                .andExpect(jsonPath("$.name", is("Taylor")))
                .andExpect(status().isOk());

        assertThat(garageRepository.count(), is(1L));
        assertThat(vehicleRepository.count(), is(2L));
    }

    @Test
    public void retrieveMultipleVehicles() throws Exception {
        GarageEntity expectedGarage = new GarageEntity();
        expectedGarage.setGarageId("gId");
        expectedGarage.setGarageName("Justin");

        garageRepository.save(expectedGarage);

        VehicleEntity vehicleEntity = new VehicleEntity();
        vehicleEntity.setGarageId(UUID.randomUUID().toString());
        vehicleEntity.setGarageId("gId");
        vehicleEntity.setVehicleId("vId1");
        vehicleEntity.setYear("2004");
        vehicleEntity.setMake("Chevy");
        vehicleEntity.setModel("Silverado");
        vehicleEntity.setName("Taylor");

        VehicleEntity vehicleEntity2 = new VehicleEntity();
        vehicleEntity2.setGarageId("gId");
        vehicleEntity2.setVehicleId("vId2");
        vehicleEntity2.setYear("2014");
        vehicleEntity2.setMake("Chevy");
        vehicleEntity2.setModel("Silverado");
        vehicleEntity2.setName("Tito");

        vehicleRepository.save(vehicleEntity);
        vehicleRepository.save(vehicleEntity2);

        ClassPathResource classPathResource = new ClassPathResource("responses/retrieveVehicles.json");
        String expectedBody = new String(Files.readAllBytes(Paths.get(classPathResource.getURI())));

        mockMvc.perform(get("/garages/{garageId}/vehicles", "gId"))
                .andExpect(content().json(expectedBody))
                .andExpect(status().isOk());

        assertThat(garageRepository.count(), is(1L));
        assertThat(vehicleRepository.count(), is(2L));
    }
}
