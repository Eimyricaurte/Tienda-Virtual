package uptc.co.tienda.Services;

import java.util.List;

import uptc.co.tienda.Entities.ProductoEntity;

public interface ProductoService {
 

    public abstract List<ProductoEntity> getListProducto();
    public abstract List<ProductoEntity> buscarProductos(int cantidad);

    // buscar productos por nombre o que contenga ese nombre
    public abstract List<ProductoEntity> buscarProductosNombre(String nombre);


    public abstract ProductoEntity saveProducto(ProductoEntity productoEntity);
    public abstract ProductoEntity updateProducto(ProductoEntity productoEntity);
    public abstract void deleteProducto(int codigo);
    public abstract ProductoEntity getProductoCodigo(int codigo);
    public abstract List<ProductoEntity> listaProductosCatalogo();


}
