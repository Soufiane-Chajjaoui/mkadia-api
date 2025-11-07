package fr.mkadia.mkadiaapi.mappers;

import fr.mkadia.mkadiaapi.dtos.AdminOrderDTO;
import fr.mkadia.mkadiaapi.dtos.ClientOrderDTO;
import fr.mkadia.mkadiaapi.dtos.OrderDTO;
import fr.mkadia.mkadiaapi.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, OrderItemMapper.class, PaymentMapper.class, DeliveryMapper.class})
public interface OrderMapper {

    @Mapping(target = "items", ignore = true)
    @Mapping(target = "payment", ignore = true)
    OrderDTO fromEntity(Order order);

    @Mapping(target = "delivery", source = "delivery.assignedTo")
    @Mapping(target = "client", source = "user")
    @Mapping(target = "paymentStatus", source = "payment.status")
    @Mapping(target = "paymentMethod", source = "payment.method")
    @Mapping(target = "address", source = "delivery.address")
    @Mapping(target = "items", source = "items", ignore = true)
    @Mapping(target = "countItems", expression = "java(order.getItems() != null ? order.getItems().size() : 0)")
    AdminOrderDTO toAdminOrder(Order order);


    @Mapping(target = "paymentStatus", source = "payment.status")
    @Mapping(target = "paymentMethod", source = "payment.method")
    @Mapping(target = "address", source = "delivery.address")
    @Mapping(target = "items", source = "items", ignore = true)
    @Mapping(target = "countItems", expression = "java(order.getItems() != null ? order.getItems().size() : 0)")
    ClientOrderDTO toClientOrder(Order order);

    @Mapping(target = "delivery", source = "delivery.assignedTo")
    @Mapping(target = "client", source = "user")
    @Mapping(target = "paymentStatus", source = "payment.status")
    @Mapping(target = "paymentMethod", source = "payment.method")
    @Mapping(target = "address", source = "delivery.address")
    @Mapping(target = "items" , source = "items")
    AdminOrderDTO toOrderDetails(Order order);

    // ✅ Pour l'écriture (DTO → Entity) - SUPPRIMEZ la référence circulaire
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "payment", ignore = true)
    @Mapping(target = "delivery", ignore = true) // ✅ IMPORTANT : Ignorez les relations
    @Mapping(target = "coupon", ignore = true)
    Order fromDTO(OrderDTO orderDTO);}