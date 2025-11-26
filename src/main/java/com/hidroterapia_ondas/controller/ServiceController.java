package com.hidroterapia_ondas.controller;

import com.hidroterapia_ondas.model.Service;
import com.hidroterapia_ondas.repository.ServiceRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service")
@CrossOrigin(origins = "*")
public class ServiceController {

    private final ServiceRepository repo;

    public ServiceController(ServiceRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/all")
    public List<Service> getAll() {
        return repo.findAll();
    }
    // Crear un nuevo servicio (solo ADMIN)
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Service createService(@RequestBody Service service) {
        return repo.save(service);
    }
}