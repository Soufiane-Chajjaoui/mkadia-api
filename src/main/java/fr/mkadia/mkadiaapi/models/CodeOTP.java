package fr.mkadia.mkadiaapi.models;

import lombok.*;

@Getter
@Setter
@Builder @AllArgsConstructor @NoArgsConstructor
public class CodeOTP {
    private String code;
    private String to;
}
