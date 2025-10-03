package fr.mkadia.mkadiaapi.mappers;


import fr.mkadia.mkadiaapi.dtos.OrderDTO;
import fr.mkadia.mkadiaapi.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring" , uses = {UserMapper.class, OrderItemMapper.class})
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);
    Order fromDTO(OrderDTO orderDTO);
    OrderDTO fromEntity(Order order);
}
