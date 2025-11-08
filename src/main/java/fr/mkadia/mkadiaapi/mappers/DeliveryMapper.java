package fr.mkadia.mkadiaapi.mappers;

import fr.mkadia.mkadiaapi.dtos.DeliveryDTO;
import fr.mkadia.mkadiaapi.entities.Delivery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface DeliveryMapper {

    @Mapping(target = "order", ignore = true) // ✅ OK, on ignore la référence circulaire
    @Mapping(target = "address", source = "address")
    @Mapping(target = "assignedTo", source = "assignedTo")
    DeliveryDTO fromEntity(Delivery delivery);

    // ✅ DTO → Entity (pour l'écriture)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true) // ✅ IMPORTANT : Ne pas mapper la référence circulaire
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Delivery fromDTO(DeliveryDTO deliveryDTO);
}

