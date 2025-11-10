package uptc.co.tienda.Services;

import java.util.List;

import uptc.co.tienda.Entities.FacturaEntity;
import uptc.co.tienda.Entities.UsuarioEntity;

public interface FacturaService {
   public abstract List<FacturaEntity> getListFacturas();
   public abstract FacturaEntity saveFactura(FacturaEntity facturaEntity);
   public abstract FacturaEntity updateFactura(FacturaEntity facturaEntity);
   public abstract FacturaEntity getFacturaCodigoFactura(int codigoFactura);
   public abstract FacturaEntity getFacturaCodigoReferencia(String codigoReferencia);
   public abstract List<FacturaEntity> facturaUsuario_Correo(String correo);
   public abstract List<FacturaEntity> fechaTransaccion(String fechaTransaccion);
   public abstract FacturaEntity usuarioOrderByCodigoFacturaDesc(String correo);


}
