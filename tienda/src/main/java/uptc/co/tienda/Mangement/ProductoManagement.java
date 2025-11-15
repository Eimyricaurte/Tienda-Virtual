package uptc.co.tienda.Mangement;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import uptc.co.tienda.Entities.ProductoEntity;

@Repository("CrudProducto")
public interface ProductoManagement extends CrudRepository<ProductoEntity, Integer> {

    List<ProductoEntity> findByCantidad(int cantidad);


    //----usuario catalogo
         List<ProductoEntity> findByCantidadNot(int cantidad);
         List<ProductoEntity> findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCase(String nombre, String descripcion);



}
