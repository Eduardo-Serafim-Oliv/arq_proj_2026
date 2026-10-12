package com.trokr.adpter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


// Um jeito de consultar o log, sem abrir o Compass toda vez
// ============================================================

@RestController
@RequestMapping("/logs")
public class LogEventoController {

//    private final LogEventoService logEventoService;
//
//    @GetMapping
//    public List<LogEventoResponseDTO> listarPorTipo(@RequestParam String tipo) {

        // TODO: chamar logEventoRepository.findByTipo(tipo) (adicionar esse metodo ao repository, se ainda nao existir)
        // TODO: mapear para LogEventoResponseDTO e retornar
//    }
}
