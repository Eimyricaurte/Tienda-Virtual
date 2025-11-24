package uptc.co.tienda.Services;

import java.util.List;

import uptc.co.tienda.Entities.VentaEntity;

public interface VentaService {

    public abstract List<VentaEntity> ventasSinPago();
    public abstract List<VentaEntity> ventasSinPagoProducto(String nombre);
    public abstract List<VentaEntity> ventasSinPagoUsuario(String correo);

    public abstract List<VentaEntity> ventasPago();
    public abstract List<VentaEntity> ventasPagoProducto(String nombre);
    public abstract List<VentaEntity> ventasPagoUsuario(String correo);



  //  public abstract List<VentaEntity> getListVentas();
    public abstract VentaEntity saveVenta(VentaEntity ventaEntity);
    public abstract VentaEntity getVentaId(int codigo);
    public abstract void deleteVenta(int id);
    public abstract void updateVentaCantidad(VentaEntity venta);
    

}
