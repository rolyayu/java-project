package by.vsu.pet.pet;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Integer> {
    List<Pet> findAllByNameAndBreed(String name, String breed);

    List<Pet> findAllByBreed(String breed, Pageable page);
}
