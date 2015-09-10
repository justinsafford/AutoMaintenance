package autoMaintProgram.integrationTests;

import autoMaintProgram.*;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class retrieveVehicles {

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
        vehicleEntity.setVehicleYear("2004");
        vehicleEntity.setVehicleMake("Chevy");
        vehicleEntity.setVehicleModel("Silverado");
        vehicleEntity.setVehicleName("Taylor");

        VehicleEntity dudVehicleEntity = new VehicleEntity();
        dudVehicleEntity.setGarageId(UUID.randomUUID().toString());
        dudVehicleEntity.setVehicleId(UUID.randomUUID().toString());
        dudVehicleEntity.setVehicleYear("sfas");
        dudVehicleEntity.setVehicleMake("asdfas");
        dudVehicleEntity.setVehicleModel("sdafas");
        dudVehicleEntity.setVehicleName("sdga");

        vehicleRepository.save(vehicleEntity);
        vehicleRepository.save(dudVehicleEntity);

        mockMvc.perform(get("/garages/{garageId}/vehicles/{vehicleId}", garageUuid, vehicleUuid))
                .andExpect(jsonPath("$.garageId", is(garageUuid)))
                .andExpect(jsonPath("$.vehicleId", is(vehicleUuid)))
                .andExpect(jsonPath("$.vehicleYear", is("2004")))
                .andExpect(jsonPath("$.vehicleMake", is("Chevy")))
                .andExpect(jsonPath("$.vehicleModel", is("Silverado")))
                .andExpect(jsonPath("$.vehicleName", is("Taylor")))
                .andExpect(status().isOk());

        assertThat(garageRepository.count(), is(1L));
        assertThat(vehicleRepository.count(), is(2L));
    }
}
