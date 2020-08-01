package com;

import java.util.HashSet;
import java.util.Set;

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

@Path("/bolo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BoloResource {
  private Set<Bolo> bolos;

  public BoloResource() {
    bolos = new HashSet<>();
    bolos.add(new Bolo("Chocolate", "Melhor bolo do mundo"));
    bolos.add(new Bolo("Sensação", "Chocolate com morango"));
  }

  @GET
  @Operation(summary = "Retorna todos os bolos cadastrados")
  @APIResponse(responseCode = "200", //
      content = @Content(//
          mediaType = MediaType.APPLICATION_JSON, //
          schema = @Schema(//
              implementation = Bolo.class, //
              type = SchemaType.ARRAY)))
  public Set<Bolo> list() {
    return bolos;
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
  public Set<Bolo> add(//
      @RequestBody(required = true, //
          content = @Content(//
              mediaType = MediaType.APPLICATION_JSON, //
              schema = @Schema(//
                  implementation = Bolo.class))) Bolo bolo) {
    bolos.add(bolo);
    return bolos;
  }

  @Operation(summary = "Deleta um bolo pelo nome do bolo")
  @APIResponse(responseCode = "200", //
      description = "Todos os bolos cadastrados menos aquele retirado", //
      content = @Content(mediaType = MediaType.APPLICATION_JSON, //
          schema = @Schema(implementation = Bolo.class, //
              type = SchemaType.ARRAY)))
  @DELETE
  @Path("/{nome}")
  public Set<Bolo> delete(//
      @Parameter(description = "Nome do bolo a ser retirado", required = true) //
      @PathParam("nome") String nome) {
    bolos.removeIf(boloExistente -> //
    boloExistente.nome.equalsIgnoreCase(nome));
    return bolos;
  }
}