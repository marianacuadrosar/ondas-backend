package com.hidroterapia_ondas.service;

import com.hidroterapia_ondas.model.User;
import com.hidroterapia_ondas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetalis loadUserByUsername (String username) throws UsernameNotFoundException {
        com.hidroterapia_ondas.model.User usuario = userRepository.findByUsername (username)
        .orElseThrow(()-> new UsernameNotFoundException("Usuario no encontrado: " + username));

        return User.builder()
        .username(usuario.getUsername())
        .password(usuario.getPassword())
        .roles(usuario.getRole())
        .build()

    } 
}