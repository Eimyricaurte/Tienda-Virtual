package uptc.co.tienda.Mangement;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import uptc.co.tienda.Entities.VentaEntity;

@Repository("CrudVenta")
public interface VentaManagement extends CrudRepository<VentaEntity, Integer> {

       List<VentaEntity> findByFacturaEntityCodigoReferenciaIsNull();
        // Buscar ventas donde código referencia sea null Y producto tenga nombre específico
       List<VentaEntity> findByFacturaEntityCodigoReferenciaIsNullAndProductoNombre(String nombreProducto);
       // Buscar ventas donde código referencia sea null Y factura.usuario.correo sea específico
       List<VentaEntity> findByFacturaEntityCodigoReferenciaIsNullAndFacturaEntityUsuarioCorreo(String correo);

       List<VentaEntity> findByFacturaEntityCodigoReferenciaIsNotNull();
       List<VentaEntity> findByFacturaEntityCodigoReferenciaIsNotNullAndProductoNombre(String nombreProducto);
       List<VentaEntity> findByFacturaEntityCodigoReferenciaIsNotNullAndFacturaEntityUsuarioCorreo(String correo);



}
