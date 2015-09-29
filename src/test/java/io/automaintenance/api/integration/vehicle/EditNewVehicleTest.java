package io.automaintenance.api.integration.vehicle;

import io.automaintenance.api.Application;
import io.automaintenance.api.garage.GarageEntity;
import io.automaintenance.api.repos.GarageRepository;
import io.automaintenance.api.repos.VehicleRepository;
import io.automaintenance.api.vehicle.VehicleEntity;
import org.hamcrest.Matchers;
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class EditNewVehicleTest {
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
    public void editNewVehicle_Success() throws Exception {
        GarageEntity garageEntity = new GarageEntity();
        garageEntity.setGarageId("gIdExisting");
        garageEntity.setGarageName("Justin");
        garageRepository.save(garageEntity);

        VehicleEntity existingVehicle = new VehicleEntity();
        existingVehicle.setGarageId("gIdExisting");
        existingVehicle.setVehicleId("vIdExisting");
        existingVehicle.setMake("Ford");
        existingVehicle.setModel("Taurus");
        existingVehicle.setName("Sheila");
        existingVehicle.setYear("1999");
        vehicleRepository.save(existingVehicle);

        ClassPathResource classPathResource = new ClassPathResource("requests/addVehicle.json");
        String request = new String(Files.readAllBytes(Paths.get(classPathResource.getURI())));

        mockMvc.perform(put("/garages/{garageId}/vehicles/{vehicleId}", existingVehicle.getGarageId(), existingVehicle.getVehicleId())
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.name", Matchers.is("Tito")))
                .andReturn();

        List<VehicleEntity> vehicleEntityList = vehicleRepository.findAll();
        assertThat(vehicleEntityList.size(), is(1));
        VehicleEntity savedVehicle = vehicleEntityList.get(0);

        assertThat(savedVehicle.getGarageId(), is(existingVehicle.getGarageId()));
        assertThat(savedVehicle.getVehicleId(), is(existingVehicle.getVehicleId()));
        assertThat(savedVehicle.getName(), is("Tito"));
        assertThat(savedVehicle.getYear(), is("2014"));
        assertThat(savedVehicle.getMake(), is("Chevy"));
        assertThat(savedVehicle.getModel(), is("Silverado"));
    }
}
