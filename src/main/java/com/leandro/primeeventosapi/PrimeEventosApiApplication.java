package com.leandro.primeeventosapi;

import com.leandro.primeeventosapi.domain.entity.Cliente;
import com.leandro.primeeventosapi.domain.entity.Usuario;
import com.leandro.primeeventosapi.domain.repository.ClienteRepository;
import com.leandro.primeeventosapi.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Bean
    public CommandLineRunner run(){
        return args -> {
            Usuario usuario = Usuario.builder()
                    .email("leandro@email.com")
                    .password(encoder.encode("123456"))
                    .admin(true)
                    .ativo(true)
                    .build();

            Cliente cliente = Cliente.builder()
                    .usuario(usuario)
                    .nome("Leandro Leite Costa")
                    .build();
            repository.save(cliente);

            System.out.println("");
            System.out.println("   ====== PRIME EVENTOS API ======");
            System.out.println("   = LOGIN DE ADMIN              =");
            System.out.println("   = email: leandro@email.com    =");
            System.out.println("   = senha: 123456               =");
            System.out.println("   ===============================");
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(PrimeEventosApiApplication.class, args);
    }

}
