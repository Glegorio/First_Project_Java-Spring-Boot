package br.com.glegorio.crudprodutos.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/status")
@Tag(name = "Status", description = "Verificação de funcionamento da API")
public class StatusController {

    @GetMapping
    @Operation(summary = "Verificar se a API está funcionando")
    public String verificarStatus(){
        return "API de produtos funcionando!";
    }
}
