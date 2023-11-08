package by.vsu.pet.pet;

import by.vsu.pet.pet.dto.CreatePetDto;
import by.vsu.pet.pet.dto.PetDtoMapper;
import by.vsu.pet.pet.dto.RequestPetDto;
import by.vsu.pet.pet.dto.UpdatePetDto;
import by.vsu.pet.pet.utils.PetUtils;
import by.vsu.pet.shared.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PetServiceImplTest {

    @InjectMocks
    private PetServiceImpl petService;

    @Mock
    private PetDtoMapper dtoMapper;

    @Mock
    private PetRepository petRepository;

    @Mock
    private PetUtils petUtils;

    private Pet testPet;

    @BeforeEach()
    void beforeEach() {
        testPet = new Pet();
        testPet.setName("Buffy");
        testPet.setBreed("Golden Retriever");
    }

    @Test
    void PetService_Save_ReturnSavedPet() {
        var createDto = new CreatePetDto(testPet.getName(), testPet.getBreed());
        given(dtoMapper.mapCreateDto(any(CreatePetDto.class))).willReturn(testPet);
        given(petRepository.save(any(Pet.class))).willReturn(testPet);

        var savedPat = petService.createPet(createDto);

        Assertions.assertNotNull(savedPat);
        Assertions.assertEquals(testPet.getName(), savedPat.getName());
        Assertions.assertEquals(testPet.getBreed(), savedPat.getBreed());
        verify(petRepository, times(1)).save(Mockito.eq(testPet));
        verify(dtoMapper, times(1)).mapCreateDto(createDto);
    }

    @Test
    void PetService_FindById_ReturnFoundedPet() {
        given(petRepository.findById(anyInt())).willReturn(Optional.of(testPet));

        var foundedPet = petService.findById(1);

        Assertions.assertEquals(testPet, foundedPet);
        verify(petRepository, times(1)).findById(anyInt());
    }

    @Test
    void PetService_FindById_ThrowWhenNotFound() {
        given(petRepository.findById(anyInt())).willReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class,
                () -> petService.findById(1));
        verify(petRepository, times(1)).findById(anyInt());
    }

    @Test
    void PetService_FindByAll_SearchWithoutCriteriaAndReturnListOfPets() {
        var petList = new ArrayList<Pet>();
        petList.add(testPet);
        var mockPageable = mock(Pageable.class);
        var mockPage = mock(Page.class);
        given(petUtils.buildPageable(any(RequestPetDto.class))).willReturn(mockPageable);
        given(petRepository.findAll(mockPageable)).willReturn(mockPage);
        given(mockPage.getContent()).willReturn(petList);

        var foundedPets = petService.findAll(new RequestPetDto());

        Assertions.assertEquals(petList.size(), foundedPets.size());
        Assertions.assertEquals(testPet, petList.get(0));

        verify(petRepository, times(1)).findAll(mockPageable);
        verify(mockPage, times(1)).getContent();
        verify(petUtils, times(1)).buildPageable(any(RequestPetDto.class));
    }

    @Test
    void PetService_FindByAll_SearchByBreedReturnListOfPets() {
        var petList = new ArrayList<Pet>();
        petList.add(testPet);
        var breed = "Retriever";
        var request = new RequestPetDto(breed);
        var mockPageable = mock(Pageable.class);
        given(petUtils.buildPageable(request)).willReturn(mockPageable);
        given(petRepository.findAllByBreed(breed,
                mockPageable)).willReturn(petList);

        var foundedPets = petService.findAll(request);


        Assertions.assertEquals(petList.size(), foundedPets.size());
        Assertions.assertEquals(testPet, petList.get(0));

        verify(petRepository, times(1)).findAllByBreed(breed, mockPageable);
        verify(petUtils, times(1)).buildPageable(request);
    }

    @Test
    void PetService_FindAll_SearchByNameAndBreedAndReturnList() {
        var petList = new ArrayList<Pet>();
        petList.add(testPet);
        var request = new RequestPetDto(testPet.getBreed()
                , testPet.getName());
        given(petRepository.findAllByNameAndBreed(anyString(), anyString())).willReturn(petList);

        var foundedPets = petService.findAll(request);

        Assertions.assertEquals(petList.size(), foundedPets.size());
        Assertions.assertEquals(testPet, petList.get(0));

        verify(petRepository, times(1)).findAllByNameAndBreed(testPet.getName(),
                testPet.getBreed());
    }

    @Test
    void PetService_DeleteById_ReturnIfDeleted() {
        when(petRepository.findById(anyInt())).thenReturn(Optional.of(testPet));
        doNothing().when(petRepository).delete(testPet);

        petService.deleteById(1);

        verify(petRepository,times(1)).findById(anyInt());
        verify(petRepository, times(1)).delete(testPet);
    }

    @Test
    void PetService_Update_ReturnUpdatedPet() {
        var updatedPet = new Pet();
        updatedPet.setName("Figoro");
        updatedPet.setBreed("Milrouse");

        var updateDto = new UpdatePetDto(updatedPet.getName());
        given(petRepository.findById(anyInt())).willReturn(Optional.of(testPet));
        given(petRepository.save(any(Pet.class))).willReturn(updatedPet);

        var savedPet = petService.update(1, updateDto);

        Assertions.assertEquals(updatedPet, savedPet);

        verify(petRepository, times(1)).findById(anyInt());
        verify(petRepository, times(1)).save(any(Pet.class));
    }

    @Test
    void PetService_Update_ThrowWhenNotFound() {
        var updateDto = new UpdatePetDto(testPet.getName());
        given(petRepository.findById(anyInt())).willReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class,
                () -> petService.update(1, updateDto));
    }
}
