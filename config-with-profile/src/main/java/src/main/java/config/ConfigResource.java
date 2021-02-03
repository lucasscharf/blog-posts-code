package config;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import java.util.Map;
import java.util.HashMap;

@Path("/config") 
public class ConfigResource {  
  @ConfigProperty(name="config") // 1
  String config;
  
  @ConfigProperty(name="valorDefinido", defaultValue="Uma configuração") // 2
  String valorDefinido;
  
  @ConfigProperty(name="inteiro") // 3
  Integer intConfig;

  @ConfigProperty(name="C*O~M_caracteres-especiais!á") // 4
  String caracteresEspeciais;
  
  @GET
  @Produces(MediaType.APPLICATION_JSON) 
  public Map<String, Object> getConfigs() { // 5
    Map<String, Object> configs = new HashMap<>();
    configs.put("Configuração: ", config);
    configs.put("Valor definido: ", valorDefinido);
    configs.put("Configuração inteira: ", intConfig);
    configs.put("Caracteres especiais: ", caracteresEspeciais);
    
    return configs; 
  }
}