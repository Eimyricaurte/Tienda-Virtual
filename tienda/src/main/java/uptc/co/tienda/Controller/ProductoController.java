package uptc.co.tienda.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uptc.co.tienda.DTO.ProductoDTO;
import uptc.co.tienda.Entities.ProductoEntity;
import uptc.co.tienda.Services.Implementation.ProductoServiceImplementation;

@RestController
@RequestMapping("/producto")
@CrossOrigin(origins="*")
public class ProductoController {
        
    private static final Logger logger = LoggerFactory.getLogger(ProductoController.class);

    @Autowired
	@Qualifier("productoServiceImplementation")
	private ProductoServiceImplementation psi;

    //----------listar para admin

    @GetMapping(path="/listar/",produces= MediaType.APPLICATION_JSON_VALUE)
	public ProductoDTO listar(){
		logger.info("El usuario ingreso al sistema al servicio de listar");
		return new ProductoDTO(psi.getListProducto() );
	}

    //------Buscador  productos por cantidad  para ADMIN
    @GetMapping(path="/buscarProductoCantidad/",produces= MediaType.APPLICATION_JSON_VALUE)
	public ProductoDTO buscarProductoCantidad(@PathVariable int cantidad){
		logger.info("El usuario ingreso al sistema al servicio de listar");
		return new ProductoDTO(psi.getListProducto() );
	}
 
	// ------Buscar productos por nombre o que contenga esa palabra
	@GetMapping(path="/buscarProductoNombre/{nombre}",produces= MediaType.APPLICATION_JSON_VALUE)
	public ProductoDTO buscarProductoNombre(@PathVariable String nombre){
		logger.info("El usuario ingreso al sistema al servicio de listar");
		return new ProductoDTO(psi.buscarProductosNombre(nombre));
	}


	@PostMapping("/save/")
    public ProductoDTO save(@RequestBody ProductoEntity productoEntity){
		logger.info("El usuario ingreso al sistema--Registrar Producto ---");
        return new ProductoDTO(psi.saveProducto(productoEntity));
    }

    //---Se resive toda la informacion del producto
	@PutMapping("/update/")
    public ProductoDTO update(@RequestBody ProductoEntity productoEntity){
        logger.info("El usuario ingreso al sistema--Editar ---");
		
			return new ProductoDTO(psi.updateProducto(productoEntity));
		 
    }
 
    //---------listar catalogo usuario
    @GetMapping(path="/listarCatalogoUsuario/",produces= MediaType.APPLICATION_JSON_VALUE)
	public ProductoDTO catalogoUsuario(){
		logger.info("El usuario ingreso al sistema al servicio de listar");
		return new ProductoDTO(psi.listaProductosCatalogo());
	}



}
