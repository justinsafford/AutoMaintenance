package io.automaintenance.api.vehicle;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultVehicleServiceTest {

    @Mock
    private VehicleResponseMapper vehicleResponseMapper;

    @InjectMocks
    private DefaultVehicleService defaultVehicleService;


    @Test
    public void createVehicleAndPersistToDatabase() {

        when(vehicleResponseMapper.map(any(VehicleRequest.class), anyString(), anyString()))
                .thenReturn(new VehicleResponse());

        defaultVehicleService.addNewVehicle(any(VehicleRequest.class), anyString(), anyString());
    }
}
