package uptc.co.tienda.Mangement;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import uptc.co.tienda.Entities.FacturaEntity;

@Repository("CrudFactura")
public interface FacturaMangement extends CrudRepository<FacturaEntity, Integer> {

        List<FacturaEntity> findByTotalIsNot(double total);
        List<FacturaEntity> findByFechaTransaccionBetweenAndTotalNot(String fechaInicio, String fechaFin, double total);
        List<FacturaEntity> findByEstadoAndTotalNot(String estado, double total);


        FacturaEntity findFirstByUsuarioCorreoOrderByCodigoFacturaDesc(String correo);
        FacturaEntity findByCodigoReferencia(String codigoReferencia);
        List<FacturaEntity>  findByUsuarioCorreoContainingAndCodigoReferenciaIsNotNullOrderByCodigoFacturaDesc(String correo);



}
