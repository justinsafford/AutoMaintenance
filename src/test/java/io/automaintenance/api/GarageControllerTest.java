package io.automaintenance.api;

import io.automaintenance.api.ResourcesNotFoundException;
import io.automaintenance.api.garage.GarageController;
import io.automaintenance.api.garage.GarageEntity;
import io.automaintenance.api.repos.GarageRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.Is.isA;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class GarageControllerTest {

    @Mock
    private GarageRepository garageRepository;

    @InjectMocks
    private GarageController garageController;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    MockMvc mockMvc;

    @Before
    public void setupMock() {
        mockMvc = standaloneSetup(garageController)
                .build();
    }

    @Test
    public void addNewGarage() throws Exception {
        mockMvc.perform(post("/garages")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isCreated());

        verify(garageRepository, times(1)).save(Matchers.isA(GarageEntity.class));
        verifyNoMoreInteractions(garageRepository);
    }

    @Test
    public void retrieveGarage() throws Exception {
        mockMvc.perform(get("/garages/{id}", "id")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());

        verify(garageRepository, times(1)).findOne("id");
        verifyNoMoreInteractions(garageRepository);
    }

    @Test
    public void deleteGarage() throws Exception {
        GarageEntity garageEntity = new GarageEntity();
        when(garageRepository.findOne("id")).thenReturn(garageEntity);
        mockMvc.perform(delete("/garages/{id}", "id")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andReturn();

        verify(garageRepository, times(1)).findOne("id");
        verify(garageRepository, times(1)).delete("id");
        verifyNoMoreInteractions(garageRepository);
    }

    @Test
    public void deleteGarageWithUnknownId_throwsRNFException() throws Exception {
        when(garageRepository.findOne("unknownId")).thenReturn(null);

        expectedException.expectCause(isA(ResourcesNotFoundException.class));
        expectedException.expectMessage("Garage not found");

        mockMvc.perform(delete("/garages/{id}", "unknownId")
                .contentType(MediaType.APPLICATION_JSON));
    }
}

