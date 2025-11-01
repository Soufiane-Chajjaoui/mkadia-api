package fr.mkadia.mkadiaapi.mappers;

import fr.mkadia.mkadiaapi.dtos.PaymentDTO;
import fr.mkadia.mkadiaapi.entities.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "order", ignore = true)  // 🔥 NE PAS mapper Order dans PaymentDTO
    PaymentDTO fromEntity(Payment payment);

    Payment fromDTO(PaymentDTO paymentDTO);
}