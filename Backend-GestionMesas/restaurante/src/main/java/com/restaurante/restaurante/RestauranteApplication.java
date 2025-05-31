package com.restaurante.restaurante;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.restaurante.restaurante.model.Mesa;
import com.restaurante.restaurante.repository.MesaRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RestauranteApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestauranteApplication.class, args);
    }

    @Bean
    public ApplicationRunner initializer(MesaRepository mesaRepository) {
        return args -> {
            /*if (mesaRepository.count() == 0) {
                mesaRepository.save(new Mesa(1, true));
                mesaRepository.save(new Mesa(2, false));
                mesaRepository.save(new Mesa(3, true));
                mesaRepository.save(new Mesa(4, true));
                mesaRepository.save(new Mesa(5, false));
                mesaRepository.save(new Mesa(6, false));
            }*/
        };
    }
}