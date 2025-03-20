package fr.mkadia.mkadiaapi.mappers;

import fr.mkadia.mkadiaapi.dtos.MediaDTO;
import fr.mkadia.mkadiaapi.entities.Media;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Set;
@Mapper(componentModel = "spring" , uses = {ProductMapper.class})
public interface MediaMapper {
    MediaMapper INSTANCE = Mappers.getMapper(MediaMapper.class);
    Media fromDTO(MediaDTO mediaDTO);
    MediaDTO fromEntity(Media media);
    Set<Media> fromDTOs(Set<MediaDTO> mediaDTOS);
}
