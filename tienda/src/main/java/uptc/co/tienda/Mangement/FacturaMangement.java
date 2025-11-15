package uptc.co.tienda.Mangement;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import uptc.co.tienda.Entities.FacturaEntity;

@Repository("CrudFactura")
public interface FacturaMangement extends CrudRepository<FacturaEntity, Integer> {

        List<FacturaEntity> findByTotalIsNotNull();
        List<FacturaEntity> findByFechaTransaccionAndTotalIsNotNull(String fechaTransaccion);


        FacturaEntity findFirstByUsuarioCorreoOrderByCodigoFacturaDesc(String correo);
        FacturaEntity findByCodigoReferencia(String codigoReferencia);
        List<FacturaEntity>  findByUsuarioCorreoContainingAndCodigoReferenciaIsNotNullOrderByCodigoFacturaDesc(String correo);



}
