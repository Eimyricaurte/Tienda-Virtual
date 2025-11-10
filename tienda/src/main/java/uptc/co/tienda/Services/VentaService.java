package uptc.co.tienda.Services;

import java.util.List;

import uptc.co.tienda.Entities.VentaEntity;

public interface VentaService {
    public abstract List<VentaEntity> getListVentas();
    public abstract VentaEntity saveVenta(VentaEntity ventaEntity);
    public abstract List<VentaEntity> findByFactura_CodigoFactura(int codigo);


}
