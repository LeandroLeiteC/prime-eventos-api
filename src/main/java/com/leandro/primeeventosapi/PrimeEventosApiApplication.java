package com.leandro.primeeventosapi;

import com.leandro.primeeventosapi.domain.entity.Usuario;
import com.leandro.primeeventosapi.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;

@SpringBootApplication
public class PrimeEventosApiApplication {

    @Autowired
    private UsuarioRepository repository;
    @Autowired
    private PasswordEncoder encoder;

    @Value("${admin.nome}")
    private String nome;

    @Value("${admin.email}")
    private String email;

    @Value("${admin.password}")
    private String password;

    @Value("${evento.foto}")
    private String PATH_FOTO;

    @Bean
    public CommandLineRunner run(){
        return args -> {

            Usuario usuario = Usuario.builder()
                    .email(email.toLowerCase())
                    .password(encoder.encode(password))
                    .nome(nome)
                    .admin(true)
                    .ativo(true)
                    .build();

            repository.save(usuario);

            System.out.println("");
            System.out.println("   ========= PRIME EVENTOS API =========");
            System.out.println("   |          LOGIN DE ADMIN");
            System.out.println("   | Email: " + usuario.getEmail());
            System.out.println("   | Senha: *****" );
            System.out.println("   =====================================");

            File dir = new File(String.valueOf(PATH_FOTO));
            dir.mkdir();
        };


    }

    public static void main(String[] args) {
        SpringApplication.run(PrimeEventosApiApplication.class, args);
    }

}
