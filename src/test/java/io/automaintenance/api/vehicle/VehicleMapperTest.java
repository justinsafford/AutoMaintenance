package io.automaintenance.api.vehicle;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(MockitoJUnitRunner.class)
public class VehicleMapperTest {

    @InjectMocks
    private VehicleResponseMapper vehicleResponseMapper;

    @Test
    public void mapVehicleRequestToVehicleResponse() {
        VehicleRequest vehicleRequest = new VehicleRequest();
        vehicleRequest.setName("Tito");
        vehicleRequest.setMake("Chevy");
        vehicleRequest.setModel("Silverado");
        vehicleRequest.setYear("2014");

        String garageId = "garageId";
        String vehicleId = "vehicleId";

        VehicleEntity mappedVehicleEntity =
                vehicleResponseMapper.map(vehicleRequest, garageId, vehicleId);

        assertThat(mappedVehicleEntity.getGarageId(), is("garageId"));
        assertThat(mappedVehicleEntity.getVehicleId(), is("vehicleId"));
        assertThat(mappedVehicleEntity.getName(), is("Tito"));
        assertThat(mappedVehicleEntity.getMake(), is("Chevy"));
        assertThat(mappedVehicleEntity.getModel(), is("Silverado"));
        assertThat(mappedVehicleEntity.getYear(), is("2014"));
    }
}
