package by.vsu.pet.pet;

import by.vsu.pet.pet.dto.CreatePetDto;
import by.vsu.pet.pet.dto.PetDtoMapper;
import by.vsu.pet.pet.dto.RequestPetDto;
import by.vsu.pet.pet.dto.UpdatePetDto;
import by.vsu.pet.pet.utils.PetUtils;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class PetServiceImpl implements PetService {

    PetRepository repository;

    PetDtoMapper mapper;

    PetUtils petUtils;

    public PetServiceImpl(PetRepository repository, PetDtoMapper mapper, PetUtils petUtils) {
        this.repository = repository;
        this.mapper = mapper;
        this.petUtils = petUtils;
    }

    @Override
    public Pet createPet(CreatePetDto createPetDto) {
        var pet = mapper.mapCreateDto(createPetDto);
        return repository.save(pet);
    }

    @Override
    public Pet findById(int id) {
        return repository.findById(id).orElseThrow(() -> new NoSuchElementException(String.format("Pet with %d id not found", id)));
    }

    @Override
    public List<Pet> findAll(RequestPetDto requestPetDto) {
        if (requestPetDto.getName().isPresent() && requestPetDto.getBreed().isPresent()) {
            return repository.findAllByNameAndBreed(requestPetDto.getName().get(), requestPetDto.getBreed().get());
        }
        var pageable = petUtils.buildPageable(requestPetDto);

        if (requestPetDto.getBreed().isPresent()) {
            return repository.findAllByBreed(requestPetDto.getBreed().get(), pageable);
        }

        return repository.findAll(pageable).getContent();
    }

    @Override
    public void deleteById(int id) {
        repository.deleteById(id);
    }

    @Override
    public Pet update(int id, UpdatePetDto updatePetDto) {
        var petToUpdate = this.findById(id);
        petToUpdate.setName(updatePetDto.name());
        return repository.save(petToUpdate);
    }
}
