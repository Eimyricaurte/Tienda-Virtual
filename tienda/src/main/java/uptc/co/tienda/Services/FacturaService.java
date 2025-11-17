package uptc.co.tienda.Services;

import java.util.List;

import uptc.co.tienda.Entities.FacturaEntity;

public interface FacturaService {

   public abstract List<FacturaEntity> comprasRealizadas();
   public abstract List<FacturaEntity> obtenerFacturasPorRangoFechas(String fechaInicio, String fechaFin);
   public abstract List<FacturaEntity> findByEstado(String estado);


   public abstract FacturaEntity usuarioOrderByCodigoFacturaDesc(String correo);
   public abstract FacturaEntity saveFactura(FacturaEntity facturaEntity);
   public abstract FacturaEntity updateFactura(FacturaEntity facturaEntity);
   public abstract FacturaEntity getFacturaCodigoReferencia(String codigoReferencia);
   List<FacturaEntity> facturasUsuario(String correo);




}
