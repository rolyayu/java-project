package by.vsu.pet.pet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@DataJpaTest()
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@EnableJpaAuditing
public class PetRepositoryTest {
    @Autowired
    private PetRepository repository;

    private Pet testPet;

    @BeforeEach
    void beforeEach() {
        repository.deleteAll();
        testPet = new Pet();
        testPet.setBreed("Golden Retriever");
        testPet.setName("Buffy");
    }

    @Test
    void PetRepository_FindAllByNameAndBreed_ReturnOnePet() {
        var savedPet = this.repository.save(testPet);

        var foundedPets = this.repository.findAllByNameAndBreed(savedPet.getName(), savedPet.getBreed());

        Assertions.assertEquals(1, foundedPets.size());
        Assertions.assertNotNull(foundedPets.get(0));
    }

    @Test
    void PetRepository_FindAllByBreed_ReturnFoundedPets() {
        this.repository.save(testPet);
        var pet = new Pet();
        pet.setBreed(testPet.getBreed());
        pet.setName("Test");
        this.repository.save(pet);

        var foundedPets =
                this.repository.findAllByBreed(testPet.getBreed(), PageRequest.ofSize(10));

        Assertions.assertEquals(2, foundedPets.size());
    }
}
