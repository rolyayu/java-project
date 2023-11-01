package by.vsu.pet.pet;

import by.vsu.pet.pet.dto.RequestPetDto;
import by.vsu.pet.pet.utils.PetUtils;
import by.vsu.pet.shared.exceptions.BadRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

public class PetUtilsTest {
    private PetUtils petUtils;

    @BeforeEach
    void beforeEach() {
        petUtils = new PetUtils();
    }

    @Test
    void PetUtils_ParseOrder_ReturnParsedOrder() {
        var rawOrder = "name,asc";

        var parsedOrder = petUtils.parseOrder(rawOrder);

        Assertions.assertTrue(parsedOrder.isAscending());
        Assertions.assertEquals("name",parsedOrder.getProperty());
    }

    @Test
    void PetUtils_ParseOrder_ThrowBadRequestBecauseOfWrongPattern() {
        var wrongPatternOrder = "name";

        Assertions.assertThrows(BadRequestException.class, () -> {
            petUtils.parseOrder(wrongPatternOrder);
        });
    }

    @Test
    void PetUtils_ParseOrder_ThrowBadRequestBecauseOfWrongField() {
        var wrongFieldName = "nname,asc";

        Assertions.assertThrows(BadRequestException.class, () -> {
            petUtils.parseOrder(wrongFieldName);
        });
    }

    @Test
    void PetUtils_ProcessSort_ReturnSort() {
        var rawOrders = List.of("name,asc", "breed,desc");

        var sort = petUtils.processSort(rawOrders);

        Assertions.assertFalse(sort.isEmpty());
        Assertions.assertEquals(2,sort.stream().count());
    }

    @Test
    void PetUtils_BuildPageable_ReturnPageable() {
        var requestPetDto = new RequestPetDto();
        requestPetDto.setName(Optional.of("Luffy"));
        requestPetDto.setBreed(Optional.of("Retriever"));

        var pageable = petUtils.buildPageable(requestPetDto);

        Assertions.assertEquals(20,pageable.getPageSize());
        Assertions.assertEquals(0,pageable.getOffset());
    }
}
