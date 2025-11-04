package fr.mkadia.mkadiaapi.mappers;

import fr.mkadia.mkadiaapi.dtos.FavoriteDTO;
import fr.mkadia.mkadiaapi.entities.Favorite;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring" , uses = {ProductMapper.class, UserMapper.class})
public interface FavoriteMapper {
    public static FavoriteMapper INSTANCE = Mappers.getMapper(FavoriteMapper.class);
    FavoriteDTO fromEntity(Favorite favorite);
    Favorite fromDTO(Favorite favorite);
}
