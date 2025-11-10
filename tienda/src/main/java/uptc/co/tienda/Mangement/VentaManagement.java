package uptc.co.tienda.Mangement;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import uptc.co.tienda.Entities.FacturaEntity;
import uptc.co.tienda.Entities.VentaEntity;

@Repository("CrudVenta")
public interface VentaManagement extends CrudRepository<VentaEntity, Integer> {

        List<VentaEntity> findByFacturaEntity_CodigoFactura(int codigo);


}
