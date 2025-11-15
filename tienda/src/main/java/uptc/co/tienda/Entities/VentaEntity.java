package uptc.co.tienda.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ventas")
public class VentaEntity {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name="Id" )
     private int id;

 
    @ManyToOne
    @JoinColumn (name="CodigoFactura")
    @JsonIgnoreProperties("listaVentaEntity")
    private FacturaEntity facturaEntity;

    @ManyToOne
    @JoinColumn (name="CodigoProducto")
    @JsonIgnoreProperties("listaVentaProEntity")
    private  ProductoEntity producto;

    @Column(name="Cantidad" )
    private int cantidad;

    @Column(name="PrecioTotal" )
    private double precioTotal;



    
    public VentaEntity() {
    }




//  -------Editar cantidad de un producto en el carro
    public VentaEntity(int id, int cantidad, double precioTotal) {
        this.id = id;
        this.cantidad = cantidad;
        this.precioTotal = precioTotal;
    }




   //-----------Registra compra de un producto

    public VentaEntity(int id, FacturaEntity facturaEntity, ProductoEntity producto, int cantidad, double precioTotal) {
        this.id = id;
        this.facturaEntity = facturaEntity;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioTotal = precioTotal;
    }



    



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FacturaEntity getFacturaEntity() {
        return facturaEntity;
    }

    public void setFacturaEntity(FacturaEntity facturaEntity) {
        this.facturaEntity = facturaEntity;
    }

   

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }





    public ProductoEntity getProducto() {
        return producto;
    }





    public void setProducto(ProductoEntity producto) {
        this.producto = producto;
    }


    
    
    

}
