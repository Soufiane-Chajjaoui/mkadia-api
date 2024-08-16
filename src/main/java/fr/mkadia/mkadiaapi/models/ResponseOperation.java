package fr.mkadia.mkadiaapi.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor @Builder
public class ResponseOperation<T>{
    private String message;
    private T object;
}