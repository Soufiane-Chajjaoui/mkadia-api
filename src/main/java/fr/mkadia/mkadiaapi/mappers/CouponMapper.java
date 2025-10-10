package fr.mkadia.mkadiaapi.mappers;

import fr.mkadia.mkadiaapi.dtos.CouponDTO;
import fr.mkadia.mkadiaapi.entities.Coupon;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CouponMapper {
    CouponDTO fromEntity(Coupon coupon);
    Coupon fromDTO(CouponDTO couponDTO);
}
