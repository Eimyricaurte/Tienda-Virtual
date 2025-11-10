package uptc.co.tienda.Entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "productos")
public class ProductoEntity {

    
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name="Codigo" )
    private int codigo;

     @Column(name="Nombre")
    private String nombre;
    
    @Column(name="Descripcion")
    private String descripcion;

    @Column(name="Imagen")
    private String imagen;

    @Column(name="Precio")
    private double precio;

    @Column(name="Cantidad")
    private int cantidad;

    @OneToMany (mappedBy = "producto")
    @JsonIgnore
    private List<VentaEntity> listaVentaProEntity;

    public ProductoEntity() {
    }

    public ProductoEntity(int codigo, String nombre, String descripcion, String imagen, double precio, int cantidad,
            List<VentaEntity> listaVentaProEntity) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.precio = precio;
        this.cantidad = cantidad;
        this.listaVentaProEntity = listaVentaProEntity;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public List<VentaEntity> getListaVentaProEntity() {
        return listaVentaProEntity;
    }

    public void setListaVentaProEntity(List<VentaEntity> listaVentaProEntity) {
        this.listaVentaProEntity = listaVentaProEntity;
    }
    


    
}
