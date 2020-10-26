package com.dipper.springboot.reactor.app;

import com.dipper.springboot.reactor.app.models.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringBootReactorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringBootReactorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Flux<Usuario> nombres = Flux.just("Andres Guzman", "Maria Fulana", "Juan Mengano", "Pedro Fulano", "Diego Sultano", "Bruce Lee", "Bruce Willis")
                .map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
                .filter(usuario -> usuario.getNombre().equalsIgnoreCase("Bruce"))
                .doOnNext(usuario -> {
                    if (usuario == null) {
                        throw new RuntimeException("Nombre no puede ser vacio");
                    }
                    System.out.println(usuario.getNombre().concat(" ").concat(usuario.getApellido()));
                })
                .map(usuario -> {
                    String nombre = usuario.getNombre().toLowerCase();
                    usuario.setNombre(nombre);
                    return usuario;
                });
        nombres.subscribe(e -> LOGGER.info(e.toString()),
                error -> LOGGER.error(error.getMessage()),
                new Runnable() {
                    @Override
                    public void run() {
                        LOGGER.info("Ha finalizado la ejecucion del observable con exito!!");
                    }
                });
    }
}
