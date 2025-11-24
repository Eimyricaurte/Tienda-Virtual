package uptc.co.tienda.DTO;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import uptc.co.tienda.Entities.FacturaEntity;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class FacturaDTO {

    private String mensaje;
    private List<FacturaEntity> lista;
    private FacturaEntity facturaEntity;


    
    public FacturaDTO(FacturaEntity facturaEntity) {
        this.facturaEntity = facturaEntity;
    }
    public FacturaDTO(List<FacturaEntity> lista) {
        this.lista = lista;
    }
    public FacturaDTO(String mensaje) {
        this.mensaje = mensaje;
    }
    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    public List<FacturaEntity> getLista() {
        return lista;
    }
    public void setLista(List<FacturaEntity> lista) {
        this.lista = lista;
    }
    public FacturaEntity getFacturaEntity() {
        return facturaEntity;
    }
    public void setFacturaEntity(FacturaEntity facturaEntity) {
        this.facturaEntity = facturaEntity;
    }

    

}
