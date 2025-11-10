package uptc.co.tienda.Mangement;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import uptc.co.tienda.Entities.FacturaEntity;
import uptc.co.tienda.Entities.UsuarioEntity;

@Repository("CrudFactura")
public interface FacturaMangement extends CrudRepository<FacturaEntity, Integer> {

        FacturaEntity findByCodigoReferencia(String codigoReferencia);
        List<FacturaEntity> findByUsuario_Correo(String correo);
        List<FacturaEntity> findByFechaTransaccion(String fechaTransaccion);
        FacturaEntity findFirstByUsuarioCorreoOrderByCodigoFacturaDesc(String correo);


}
