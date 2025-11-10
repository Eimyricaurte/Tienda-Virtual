package uptc.co.tienda.DTO;

import java.util.List;


import uptc.co.tienda.Entities.VentaEntity;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class VentaDTO {

    private String mensaje;
    private List<VentaEntity> lista;
    private VentaEntity ventaEntity;



    

    public VentaDTO(VentaEntity ventaEntity) {
        this.ventaEntity = ventaEntity;
    }
    public VentaDTO(List<VentaEntity> lista) {
        this.lista = lista;
    }
    public VentaDTO(String mensaje) {
        this.mensaje = mensaje;
    }
    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    public List<VentaEntity> getLista() {
        return lista;
    }
    public void setLista(List<VentaEntity> lista) {
        this.lista = lista;
    }
    public VentaEntity getVentaEntity() {
        return ventaEntity;
    }
    public void setVentaEntity(VentaEntity ventaEntity) {
        this.ventaEntity = ventaEntity;
    }



}
