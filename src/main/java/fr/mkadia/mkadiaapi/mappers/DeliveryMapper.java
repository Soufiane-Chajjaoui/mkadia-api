package fr.mkadia.mkadiaapi.mappers;

import fr.mkadia.mkadiaapi.dtos.DeliveryDTO;
import fr.mkadia.mkadiaapi.entities.Delivery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface DeliveryMapper {

    @Mapping(target = "order", ignore = true)
    DeliveryDTO fromEntity(Delivery delivery);

    Delivery fromDTO(DeliveryDTO deliveryDTO);
}

