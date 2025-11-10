package uptc.co.tienda.Entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "factura")
public class FacturaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name="codigoFactura")
    private int codigoFactura;

    @Column(name="codigoReferencia")
    private String codigoReferencia;

   @ManyToOne
    @JoinColumn(name = "correo")
    private UsuarioEntity usuario;    

     @Column(name="estadoTransaccion" )
    private String estadoTransaccion;

     @Column(name="estado" )
    private String estado;

     @Column(name="total" )
    private double total;  
    
     @Column(name="transaccionId" )
    private String transaccionId;

     @Column(name="ordenIdPay" )
    private String ordenIdPay;

    @Column(name="codigoAutorizacionBanco" )
    private String codigoAutorizacionBanco;

    @Column(name="fechaTransaccion" )
    private String fechaTransaccion;

    @OneToMany (mappedBy = "facturaEntity")
    @JsonIgnore
    private List<VentaEntity> listaVentaEntity;

    
    
    public FacturaEntity() {
        
    }
    

    public FacturaEntity(int codigoFactura, UsuarioEntity usuario) {
        this.codigoFactura = codigoFactura;
        this.usuario = usuario;
    }


    

    public FacturaEntity(UsuarioEntity usuario) {
        this.usuario = usuario;
    }


    public FacturaEntity(int codigoFactura, UsuarioEntity usuario, double total) {
        this.codigoFactura = codigoFactura;
        this.usuario = usuario;
        this.total = total;
    }


    public FacturaEntity(int codigoFactura, String codigoReferencia, UsuarioEntity usuario, String estadoTransaccion,
            String estado, double total, String transaccionId, String ordenIdPay, String codigoAutorizacionBanco,
            String fechaTransaccion, List<VentaEntity> listaVentaEntity) {
        this.codigoFactura = codigoFactura;
        this.codigoReferencia = codigoReferencia;
        this.usuario = usuario;
        this.estadoTransaccion = estadoTransaccion;
        this.estado = estado;
        this.total = total;
        this.transaccionId = transaccionId;
        this.ordenIdPay = ordenIdPay;
        this.codigoAutorizacionBanco = codigoAutorizacionBanco;
        this.fechaTransaccion = fechaTransaccion;
        this.listaVentaEntity = listaVentaEntity;
    }


    public int getCodigoFactura() {
        return codigoFactura;
    }

    public void setCodigoFactura(int codigoFactura) {
        this.codigoFactura = codigoFactura;
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
    }

    public String getEstadoTransaccion() {
        return estadoTransaccion;
    }

    public void setEstadoTransaccion(String estadoTransaccion) {
        this.estadoTransaccion = estadoTransaccion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getTransaccionId() {
        return transaccionId;
    }

    public void setTransaccionId(String transaccionId) {
        this.transaccionId = transaccionId;
    }

    public String getOrdenIdPay() {
        return ordenIdPay;
    }

    public void setOrdenIdPay(String ordenIdPay) {
        this.ordenIdPay = ordenIdPay;
    }

    public String getCodigoAutorizacionBanco() {
        return codigoAutorizacionBanco;
    }

    public void setCodigoAutorizacionBanco(String codigoAutorizacionBanco) {
        this.codigoAutorizacionBanco = codigoAutorizacionBanco;
    }

    public String getFechaTransaccion() {
        return fechaTransaccion;
    }

    public void setFechaTransaccion(String fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }

   

    public List<VentaEntity> getListaVentaEntity() {
        return listaVentaEntity;
    }

    public void setListaVentaEntity(List<VentaEntity> listaVentaEntity) {
        this.listaVentaEntity = listaVentaEntity;
    }


    public String getCodigoReferencia() {
        return codigoReferencia;
    }


    public void setCodigoReferencia(String codigoReferencia) {
        this.codigoReferencia = codigoReferencia;
    }


}
