package uptc.co.tienda.Services.Implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import uptc.co.tienda.Entities.ProductoEntity;
import uptc.co.tienda.Mangement.ProductoManagement;
import uptc.co.tienda.Services.ProductoService;

@Service
public class ProductoServiceImplementation implements ProductoService{

    @Autowired
	@Qualifier("CrudProducto")
	private ProductoManagement pm;
 
    //-------Todos los produtcos admin
    @Override
    @Cacheable("ListarProductos")
    public List<ProductoEntity> getListProducto() {
        return (List<ProductoEntity>) pm.findAll();
    }

    //-------Buscar productos por cantidad
    @Override
    @Cacheable("ListarProductosCantidad")
    public List<ProductoEntity> buscarProductos(int cantidad) {
          return pm.findByCantidad(cantidad); 
    }
    

    @Override
     @CacheEvict(value = { 
        "ListarProductos",
    "ListarProductosCantidad", 
    "ProductoCodigo",
    "ListarProductosCatalogo",
    "ListarProductosNombre",
    "ListarProductosNombreA"
    }, allEntries = true)
    public ProductoEntity saveProducto(ProductoEntity productoEntity) {
         return pm.save(productoEntity);    
    }

    @Override
    @CacheEvict(value = {
        "ListarProductos", 
    "ListarProductosCantidad", 
    "ProductoCodigo",
    "ListarProductosCatalogo",
    "ListarProductosNombre",
    "ListarProductosNombreA"
    }, allEntries = true)
    public ProductoEntity updateProducto(ProductoEntity productoEntity) {
        return pm.save(productoEntity);
    }

    @Override
    @CacheEvict(value = { 
        "ListarProductos",
    "ListarProductosCantidad", 
    "ProductoCodigo",
    "ListarProductosCatalogo",
    "ListarProductosNombre",
    "ListarProductosNombreA"
    }, allEntries = true)
    public void deleteProducto(int codigo) {
        pm.deleteById(codigo);
    }

    @Override
    @Cacheable("ProductoCodigo")
    public ProductoEntity getProductoCodigo(int codigo) {
       return pm.findById(codigo).orElseThrow(()->new IllegalArgumentException("El producto no existe"));
    }

    @Override
    @Cacheable("ListarProductosCatalogo")
    public List<ProductoEntity> listaProductosCatalogo() {
        return pm.findByCantidadNot(0);
    }

    @Override
    @Cacheable("ListarProductosNombre")
    public List<ProductoEntity> buscarProductosNombre(String nombre) {
         return pm.findByCantidadIsNotAndNombreContainingIgnoreCaseOrCantidadIsNotAndDescripcionContainingIgnoreCase(0,nombre, 0,nombre);
    }

    @Override
    @Cacheable("ListarProductosNombreA")
    public List<ProductoEntity> buscarProductosNombreA(String nombre) {
      return pm.findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCase(nombre, nombre);
    }

    

}
