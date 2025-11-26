package com.hidroterapia_ondas.repository;

import com.hidroterapia_ondas.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, Long> {
}