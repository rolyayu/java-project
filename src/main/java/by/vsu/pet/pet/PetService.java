package by.vsu.pet.pet;

import by.vsu.pet.pet.dto.CreatePetDto;
import by.vsu.pet.pet.dto.RequestPetDto;
import by.vsu.pet.pet.dto.UpdatePetDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PetService {
    Pet createPet(CreatePetDto createPetDto);

    Pet findById(int id);

    List<Pet> findAll(RequestPetDto requestPetDto);

    void deleteById(int id);

    Pet update(int id, UpdatePetDto updatePetDto);
}
