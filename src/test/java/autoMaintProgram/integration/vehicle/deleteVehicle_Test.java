package autoMaintProgram.integration.vehicle;

import autoMaintProgram.*;
import autoMaintProgram.garage.GarageEntity;
import autoMaintProgram.repos.GarageRepository;
import autoMaintProgram.repos.VehicleRepository;
import autoMaintProgram.vehicle.VehicleController;
import autoMaintProgram.vehicle.VehicleEntity;
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
public class deleteVehicle_Test {

    @Autowired
    GarageRepository garageRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    VehicleController vehicleController;

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
    public void deleteVehicle() throws Exception {
        GarageEntity expectedGarage = new GarageEntity();
        String garageUuid = UUID.randomUUID().toString();
        expectedGarage.setGarageId(garageUuid);
        expectedGarage.setGarageName("Justin");

        garageRepository.save(expectedGarage);

        VehicleEntity vehicleEntity = new VehicleEntity();
        vehicleEntity.setGarageId(garageUuid);
        String vehicleUuid = UUID.randomUUID().toString();
        vehicleEntity.setVehicleId(vehicleUuid);

        vehicleRepository.save(vehicleEntity);

        assertThat(garageRepository.count(), is(1L));
        assertThat(vehicleRepository.count(), is(1L));

        vehicleController.deleteVehicleInGarage(garageUuid, vehicleUuid);

        assertThat(garageRepository.count(), is(1L));
        assertThat(vehicleRepository.count(), is(0L));
    }
}