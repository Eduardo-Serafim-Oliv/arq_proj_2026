package com.trokr.adapter;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "logs_eventos")
public class LogEvento {

    @Id
    private String id;

    private String tipo;
    private NivelLog nivel;
    private String origem;
    private String mensagem;

    private Map<String, Object> payload;

    private LocalDateTime dataHora;
}
