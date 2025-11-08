package fr.mkadia.mkadiaapi.mappers;

import fr.mkadia.mkadiaapi.dtos.PaymentDTO;
import fr.mkadia.mkadiaapi.entities.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "order", ignore = true) // ✅ On ignore la référence circulaire
    PaymentDTO fromEntity(Payment payment);

    // ✅ DTO → Entity
    @Mapping(target = "order", ignore = true) // ✅ IMPORTANT
    Payment fromDTO(PaymentDTO paymentDTO);
}