package com;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@Path("/bolo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BoloResource {
  @Inject
  @Channel("add-bolo")
  Emitter<Bolo> addEmitter;
  
  @Inject
  @Channel("delete-bolo")
  Emitter<String> deleteEmitter;

  @PostConstruct
  @Transactional
  public void init() {
    new Bolo("Chocolate", "Melhor bolo do mundo").persist();
    new Bolo("Sensação", "Chocolate com morango").persist();
  }

  @GET
  @Operation(summary = "Retorna todos os bolos cadastrados")
  @APIResponse(responseCode = "200", //
      content = @Content(//
          mediaType = MediaType.APPLICATION_JSON, //
          schema = @Schema(//
              implementation = Bolo.class, //
              type = SchemaType.ARRAY)))
  public List<Bolo> list() {
    return Bolo.listAll();
  }

  @Operation(summary = "Cadastra um bolo")
  @APIResponse(responseCode = "200", //
      description = "Retorna todos os todos os bolos cadastrados, incluindo o novo bolo", //
      content = @Content(//
          mediaType = MediaType.APPLICATION_JSON, //
          schema = @Schema(//
              implementation = Bolo.class, //
              type = SchemaType.ARRAY)))
  @POST
  @Transactional
  public List<Bolo> add(//
      @RequestBody(required = true, //
          content = @Content(//
              mediaType = MediaType.APPLICATION_JSON, //
              schema = @Schema(//
                  implementation = Bolo.class))) Bolo bolo) {
    bolo.id = null; //coisa feia, não façam isso em casa
    bolo.persist();
    addEmitter.send(bolo);
    return list();
  }

  @Operation(summary = "Deleta um bolo pelo nome do bolo")
  @APIResponse(responseCode = "200", //
      description = "Todos os bolos cadastrados menos aquele retirado", //
      content = @Content(mediaType = MediaType.APPLICATION_JSON, //
          schema = @Schema(implementation = Bolo.class, //
              type = SchemaType.ARRAY)))
  @DELETE
  @Path("/{nome}")
  @Transactional
  public List<Bolo> delete(//
      @Parameter(description = "Nome do bolo a ser retirado", required = true) //
      @PathParam("nome") String nome) {
    Bolo.delete("nome", nome);
    deleteEmitter.send(nome);
    return Bolo.listAll();
  }
}