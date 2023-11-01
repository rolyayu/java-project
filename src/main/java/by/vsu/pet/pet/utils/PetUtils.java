package by.vsu.pet.pet.utils;


import by.vsu.pet.pet.dto.RequestPetDto;
import by.vsu.pet.shared.exceptions.BadRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class PetUtils {
    private final Set<String> orders;

    public PetUtils() {
        orders = new HashSet<>();
        orders.add("name");
        orders.add("breed");
        orders.add("id");
        orders.add("createdAt");
    }

    public Pageable buildPageable(RequestPetDto requestPetDto) {
        var pageSize = requestPetDto.getPageSize().isPresent()
                ? requestPetDto.getPageSize().get()
                : RequestPetDto.DEFAULT_PAGE_SIZE;
        var offset = requestPetDto.getOffset().isPresent()
                ? requestPetDto.getOffset().get()
                : RequestPetDto.DEFAULT_OFFSET;
        var sort = processSort(requestPetDto.getOrders());
        return PageRequest.of(offset,pageSize,sort);
    }


    public Sort processSort(List<String> rawOrders) {
        var orders = new HashSet<Sort.Order>();
        rawOrders.stream().map(this::parseOrder)
                .forEach(orders::add);
        return Sort.by(orders.stream().toList());
    }

    public Sort.Order parseOrder(String rawOrder) {
        var fieldWithOrder = rawOrder.split(",");
        if(fieldWithOrder.length!=2) {
            throw new BadRequestException("Sorting should be given in way '[field],[order]'");
        }
        var fieldName = fieldWithOrder[0];
        var isDescending = fieldWithOrder[1].equals("desc");
        if(!orders.contains(fieldName)) {
            throw new BadRequestException(String.format("Field %s is invalid.",fieldName));
        }
        return isDescending? Sort.Order.desc(fieldName) : Sort.Order.asc(fieldName);
    }
}
