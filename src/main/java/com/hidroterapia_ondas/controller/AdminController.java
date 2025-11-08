package com.hidroterapia_ondas.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/prueba")
    public Map<String, Object> pruebaAdmin() {
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Acceso permitido solo para ADMIN");
        response.put("estado", "OK");
        return response;
    }
}
