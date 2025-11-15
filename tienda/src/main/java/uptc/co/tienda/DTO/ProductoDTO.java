package uptc.co.tienda.DTO;

import java.util.List;

import uptc.co.tienda.Entities.ProductoEntity;
import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ProductoDTO {

    private String mensaje;
    private List<ProductoEntity> lista;
    private ProductoEntity productoEntity;

    public ProductoDTO(String mensaje) {
        this.mensaje = mensaje;
    }
    public ProductoDTO(List<ProductoEntity> lista) {
        this.lista = lista;
    }
    public ProductoDTO(ProductoEntity facturaEntity) {
        this.productoEntity = facturaEntity;
    }
    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    public List<ProductoEntity> getLista() {
        return lista;
    }
    public void setLista(List<ProductoEntity> lista) {
        this.lista = lista;
    }
    public ProductoEntity getProductoEntity() {
        return productoEntity;
    }
    public void setProductoEntity(ProductoEntity productoEntity) {
        this.productoEntity = productoEntity;
    }
    


}
