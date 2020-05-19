package com.leandro.primeeventosapi.service.impl;

import com.leandro.primeeventosapi.domain.entity.Usuario;
import com.leandro.primeeventosapi.domain.repository.UsuarioRepository;
import com.leandro.primeeventosapi.exception.BussinesException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UserDetailsService {

    private final PasswordEncoder encoder;
    private final UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = repository.findByEmail(email)
                .orElseThrow(() -> new BussinesException("Email ou senha inválidos."));

        String[] roles = usuario.isAdmin() ? new String[] {"ADMIN", "CLIENTE"} : new String[] {"CLIENTE"};
        return User.builder()
                        .username(usuario.getEmail())
                        .password(usuario.getPassword())
                        .roles(roles)
                    .build();
    }

    public UserDetails autenticar(Usuario usuario) {
        UserDetails user = loadUserByUsername(usuario.getEmail());
        boolean senhasBatem = encoder.matches(usuario.getPassword(), user.getPassword());
        if(senhasBatem){
            return user;
        }else{
            throw new BussinesException("Email ou senha inválidos.");
        }
    }
}
