package by.vsu.pet.pet;

import by.vsu.pet.pet.dto.CreatePetDto;
import by.vsu.pet.pet.dto.RequestPetDto;
import by.vsu.pet.pet.dto.UpdatePetDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pets")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class PetController {
    PetService petService;

    @GetMapping("/{id}")
    public Pet findPetById(@PathVariable int id) {
        return petService.findById(id);
    }

    @GetMapping
    public List<Pet> findAll(RequestPetDto requestPetDto) {
        return petService.findAll(requestPetDto);
    }


    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Pet createPet(@RequestBody CreatePetDto createPetDto) {
        return petService.createPet(createPetDto);
    }

    @PatchMapping("/{id}")
    public Pet updatePet(@RequestBody UpdatePetDto updatePetDto, @PathVariable int id) {
        return petService.update(id, updatePetDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePet(@PathVariable int id) {
        petService.deleteById(id);
    }
}
