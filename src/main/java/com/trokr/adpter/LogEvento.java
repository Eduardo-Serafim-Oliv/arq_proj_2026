package com.trokr.adpter;

import com.trokr.model.StatusAvaliacao;
import com.trokr.model.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Document
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogEvento {


    //TODO: Entender como funciona a associoação entre atributos e colunas no caso do Mongo DB,
    // será necessário @Column()...??

    private String tipo;

//    @Column
    private Integer nota;

    @Enumerated(EnumType.STRING)
    private Enum nivel;

    private String origem;

    private Map<String, Object> payload;

    @CreationTimestamp
//    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    private Long usuarioId;
}
