package by.vsu.pet.pet.dto;

import by.vsu.pet.pet.Pet;
import org.springframework.stereotype.Component;

@Component
public class PetDtoMapper {
    public Pet mapCreateDto(CreatePetDto createPetDto) {
        var pet = new Pet();
        pet.setName(createPetDto.name());
        pet.setBreed(createPetDto.breed());
        return  pet;
    }

    public CreatePetDto mapToCreateDto(Pet pet) {
        return new CreatePetDto(
                pet.getName(),
                pet.getBreed()
        );
    }
}
