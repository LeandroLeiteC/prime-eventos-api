package com.leandro.primeeventosapi;

import com.leandro.primeeventosapi.domain.entity.Cliente;
import com.leandro.primeeventosapi.domain.entity.Usuario;
import com.leandro.primeeventosapi.domain.repository.ClienteRepository;
import com.leandro.primeeventosapi.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class PrimeEventosApiApplication {

    @Autowired
    private ClienteRepository repository;
    @Autowired
    private PasswordEncoder encoder;

    @Value("${admin.nome}")
    String nome;

    @Value("${admin.email}")
    String email;

    @Value("${admin.password}")
    String password;

    @Bean
    public CommandLineRunner run(){
        return args -> {

            Usuario usuario = Usuario.builder()
                    .email(email.toLowerCase())
                    .password(encoder.encode(password))
                    .admin(true)
                    .ativo(true)
                    .build();

            Cliente cliente = Cliente.builder()
                    .usuario(usuario)
                    .nome(nome.toUpperCase())
                    .build();
            repository.save(cliente);

            System.out.println("");
            System.out.println("   ========= PRIME EVENTOS API =========");
            System.out.println("   |          LOGIN DE ADMIN");
            System.out.println("   | Email: " + usuario.getEmail());
            System.out.println("   | Senha: *****" );
            System.out.println("   =====================================");
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(PrimeEventosApiApplication.class, args);
    }

}
