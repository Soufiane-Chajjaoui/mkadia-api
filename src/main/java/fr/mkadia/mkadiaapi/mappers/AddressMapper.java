package fr.mkadia.mkadiaapi.mappers;

import fr.mkadia.mkadiaapi.dtos.AddressDTO;
import fr.mkadia.mkadiaapi.entities.Address;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {OrderMapper.class})
public interface AddressMapper {
    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);
    Address fromDTO(AddressDTO addressDTO);
    AddressDTO fromEntity(Address address);
}
