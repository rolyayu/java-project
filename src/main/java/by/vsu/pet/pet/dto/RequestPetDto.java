package by.vsu.pet.pet.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class RequestPetDto {

    public static int DEFAULT_PAGE_SIZE = 20;
    public static int DEFAULT_OFFSET = 0;

    private Optional<Integer> pageSize;
    private Optional<Integer> offset;
    private Optional<String> breed;
    private Optional<String> name;

    private List<String> orders;

    public RequestPetDto(String breed) {
        this.breed = Optional.of(breed);
        this.offset = Optional.empty();
        this.pageSize = Optional.empty();
        this.name = Optional.empty();
        this.orders = List.of();
    }

    public RequestPetDto() {
        this.offset = Optional.empty();
        this.pageSize = Optional.empty();
        this.breed = Optional.empty();
        this.name = Optional.empty();
        this.orders = List.of();
    }

    public RequestPetDto(String breed,String name) {
        this.breed = Optional.of(breed);
        this.name = Optional.of(name);
        this.offset = Optional.empty();
        this.pageSize = Optional.empty();
        this.orders = List.of();
    }

    public RequestPetDto(String breed, String name, List<String> orders) {
        this(breed,name);
        this.orders = orders;
        this.offset = Optional.empty();
        this.pageSize = Optional.empty();
    }
}
