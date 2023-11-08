package by.vsu.pet.pet;

import by.vsu.pet.pet.dto.CreatePetDto;
import by.vsu.pet.pet.dto.RequestPetDto;
import by.vsu.pet.pet.dto.UpdatePetDto;
import by.vsu.pet.shared.exceptions.BadRequestException;
import by.vsu.pet.shared.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PetController.class)
public class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PetServiceImpl petService;

    @Test
    void PetController_Create_ReturnCreatedPet() throws Exception {
        var createPetDto = new CreatePetDto("Farcos", "Retriever");
        var createdPet = new Pet();
        createdPet.setBreed(createPetDto.breed());
        createdPet.setName(createPetDto.name());
        when(petService.createPet(createPetDto)).thenReturn(createdPet);

        mockMvc.perform(post("/pets")
                        .content("""
                                {
                                    "name": "Farcos",
                                    "breed": "Retriever"
                                }
                                """)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(createdPet.getName()))
                .andExpect(jsonPath("$.breed").value(createdPet.getBreed()));
        verify(petService, times(1)).createPet(createPetDto);
    }

    @Test
    void PetController_FindById_ReturnFoundedPet() throws Exception {
        var foundedPet = new Pet();
        foundedPet.setName("TestName");
        foundedPet.setBreed("TestBreed");
        when(petService.findById(anyInt())).thenReturn(foundedPet);

        mockMvc.perform(get("/pets/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(foundedPet.getName()))
                .andExpect(jsonPath("$.breed").value(foundedPet.getBreed()));
        verify(petService, times(1)).findById(anyInt());
    }

    @Test
    void PetController_FindById_ReturnErrorWhenNotFound() throws Exception {
        when(petService.findById(anyInt())).thenThrow(new NotFoundException("Pet not found"));

        mockMvc.perform((get("/pets/{id}", 1)))
                .andExpect(status().isNotFound());
        verify(petService, times(1)).findById(anyInt());
    }

    @Test
    void PetController_FindAll_ReturnListOfPets() throws Exception {
        when(petService.findAll(any(RequestPetDto.class))).thenReturn(List.of());

        mockMvc.perform(get("/pets")).andExpect(status().isOk());
        verify(petService, times(1)).findAll(any(RequestPetDto.class));
    }

    @Test
    void PetController_FindAll_ReturnBadRequestWhenQueryNotValid() throws Exception {
        when(petService.findAll(any(RequestPetDto.class))).thenThrow(new BadRequestException("Invalid query"));

        mockMvc.perform(get("/pets")).andExpect(status().isBadRequest());

        verify(petService,times(1)).findAll(any(RequestPetDto.class));
    }

    @Test
    void PetController_UpdatePet_ReturnUpdatePet() throws Exception {
        var updatedPet = new Pet();
        updatedPet.setName("TestName");
        updatedPet.setBreed("TestBreed");
        when(petService.update(anyInt(),
                any(UpdatePetDto.class))).thenReturn(updatedPet);

        mockMvc.perform(patch("/pets/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "TestName"
                                }
                                """)).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updatedPet.getName()));
        verify(petService,times(1)).update(anyInt(),
                any(UpdatePetDto.class));
    }

    @Test
    void PetController_UpdatePet_ReturnNotFoundError() throws Exception {
        when(petService.update(anyInt(),
                any(UpdatePetDto.class))).thenThrow(new NotFoundException(""));

        mockMvc.perform(patch("/pets/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "TestName"
                                }
                                """)).andExpect(status().isNotFound());
        verify(petService,times(1)).update(anyInt(),
                any(UpdatePetDto.class));
    }

    @Test
    void PetController_DeletePet_ReturnOk() throws Exception {
        doNothing().when(petService).deleteById(anyInt());

        mockMvc.perform(delete("/pets/{id}",1))
                .andExpect(status().isNoContent());

        verify(petService,times(1)).deleteById(anyInt());
    }

    @Test
    void PetController_DeletePet_ReturnNotFoundError() throws Exception {
        doThrow(new NotFoundException("")).when(petService).deleteById(anyInt());

        mockMvc.perform(delete("/pets/{id}",1))
                .andExpect(status().isNotFound());

        verify(petService,times(1)).deleteById(anyInt());
    }
}
