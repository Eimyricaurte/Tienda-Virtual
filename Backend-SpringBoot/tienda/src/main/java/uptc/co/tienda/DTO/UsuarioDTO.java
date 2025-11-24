package uptc.co.tienda.DTO;

import java.util.List;

import uptc.co.tienda.Entities.UsuarioEntity;
import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class UsuarioDTO {

    private String mensaje;
    private String token;
    private List<UsuarioEntity> lista;
    private UsuarioEntity usuarioEntity;




    
    public UsuarioDTO(String mensaje, String token) {
        this.mensaje = mensaje;
        this.token = token;
    }
    public UsuarioDTO(String mensaje) {
        this.mensaje = mensaje;
    }
    public UsuarioDTO(List<UsuarioEntity> lista) {
        this.lista = lista;
    }
   

    
    public UsuarioDTO(UsuarioEntity usuarioEntity) {
        this.usuarioEntity = usuarioEntity;
    }
    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    public List<UsuarioEntity> getLista() {
        return lista;
    }
    public void setLista(List<UsuarioEntity> lista) {
        this.lista = lista;
    }
   
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public UsuarioEntity getUsuarioEntity() {
        return usuarioEntity;
    }
    public void setUsuarioEntity(UsuarioEntity usuarioEntity) {
        this.usuarioEntity = usuarioEntity;
    }

    

}
