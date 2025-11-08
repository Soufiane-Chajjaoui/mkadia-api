package fr.mkadia.mkadiaapi.mappers;

import fr.mkadia.mkadiaapi.dtos.OrderItemDTO;
import fr.mkadia.mkadiaapi.dtos.OrderItemSimpleDTO;
import fr.mkadia.mkadiaapi.entities.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface OrderItemMapper {

    @Mapping(target = "order", ignore = true)  // 🔥 NE PAS mapper Order dans OrderItemDTO
    @Mapping(target = "product", source = "product")
    OrderItemDTO fromEntity(OrderItem orderItem);
    OrderItemSimpleDTO toOrderItemSimpleDTO(OrderItem orderItem);
    OrderItem fromDTO(OrderItemDTO orderItemDTO);
    @Mapping(target = "order", ignore = true)
    List<OrderItemDTO> fromEntities(List<OrderItem> orderItems);
}
