package com;

import javax.ws.rs.core.Application;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.servers.Server;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@OpenAPIDefinition(//
    tags = { //
        @Tag(name = "tutorial", description = "Exemplo de estudo."), //
        @Tag(name = "bolo", description = "Pessoas gostam de bolo.")//
    }, //
    info = @Info(//
        title = "A fantástica fábrica de bolo", //
        version = "1.0.0", //
        contact = @Contact(//
            name = "Fale com o Aleatório", //
            url = "https://github.com/lucasscharf")), //
    servers = { //
        @Server(url = "http://localhost:8080")//
    }) //
public class Descricao extends Application {

}