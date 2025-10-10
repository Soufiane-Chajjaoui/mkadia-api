package fr.mkadia.mkadiaapi.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ElementsOfPageDTO<T> {

    private int currentPage;
    private int totalPages;
    private int pageSize;
    private long totalRecords;
    @JsonProperty("elements")
    private List<T> elementsDTO;
}
