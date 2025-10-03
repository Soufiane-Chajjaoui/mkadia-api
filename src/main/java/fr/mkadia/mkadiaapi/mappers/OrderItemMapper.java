package fr.mkadia.mkadiaapi.mappers;

import fr.mkadia.mkadiaapi.dtos.OrderItemDTO;
import fr.mkadia.mkadiaapi.entities.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring" , uses = {OrderMapper.class})
public interface OrderItemMapper {
    OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);
    OrderItemDTO fromEntity(OrderItem orderItem);
    OrderItem fromDTO(OrderItemDTO orderItemDTO);
    List<OrderItemDTO> fromEntities(List<OrderItem> items);
}
