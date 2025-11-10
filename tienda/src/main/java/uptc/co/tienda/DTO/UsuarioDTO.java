package uptc.co.tienda.DTO;

import java.util.List;

import uptc.co.tienda.Entities.UsuarioEntity;
import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class UsuarioDTO {

    private String mensaje;
    private List<UsuarioEntity> lista;
    private UsuarioEntity estudianteEntity;




    
    public UsuarioDTO(String mensaje) {
        this.mensaje = mensaje;
    }
    public UsuarioDTO(String mensaje, List<UsuarioEntity> lista) {
        this.mensaje = mensaje;
        this.lista = lista;
    }
    public UsuarioDTO(String mensaje, UsuarioEntity estudianteEntity) {
        this.mensaje = mensaje;
        this.estudianteEntity = estudianteEntity;
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
    public UsuarioEntity getEstudianteEntity() {
        return estudianteEntity;
    }
    public void setEstudianteEntity(UsuarioEntity estudianteEntity) {
        this.estudianteEntity = estudianteEntity;
    }

    

}
