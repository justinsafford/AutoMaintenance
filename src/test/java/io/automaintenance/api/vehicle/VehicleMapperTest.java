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

        VehicleResponse mappedVehicleResponse =
                vehicleResponseMapper.map(vehicleRequest, garageId, vehicleId);

        assertThat(mappedVehicleResponse.getGarageId(), is("garageId"));
        assertThat(mappedVehicleResponse.getVehicleId(), is("vehicleId"));
        assertThat(mappedVehicleResponse.getName(), is("Tito"));
        assertThat(mappedVehicleResponse.getMake(), is("Chevy"));
        assertThat(mappedVehicleResponse.getModel(), is("Silverado"));
        assertThat(mappedVehicleResponse.getYear(), is("2014"));
    }
}
