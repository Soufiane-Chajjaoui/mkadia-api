package fr.mkadia.mkadiaapi.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.mkadia.mkadiaapi.enums.DeliveryMode;
import fr.mkadia.mkadiaapi.enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDTO {
    private Integer id;
    private OrderDTO order;
    private DeliveryStatus status;
    private AddressDTO address;
    private DeliveryMode mode;
    private UserDTO assignedTo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
