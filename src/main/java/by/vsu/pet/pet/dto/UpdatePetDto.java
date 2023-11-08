package by.vsu.pet.pet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdatePetDto(@NotNull @NotBlank String name) {
}
