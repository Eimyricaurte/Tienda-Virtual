package uptc.co.tienda.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import uptc.co.tienda.DTO.ProductoDTO;
import uptc.co.tienda.Entities.ProductoEntity;
import uptc.co.tienda.Services.JWT;
import uptc.co.tienda.Services.Implementation.ProductoServiceImplementation;

@RestController
@RequestMapping("/producto")
@CrossOrigin(origins="*")
public class ProductoController {
        
    private static final Logger logger = LoggerFactory.getLogger(ProductoController.class);
	private static final String secretKey = "bXktc2VjdXJlLXNlY3JldC1rZXktYmFzZTY0LWF1dGg=";


    @Autowired
	@Qualifier("productoServiceImplementation")
	private ProductoServiceImplementation psi;

    private JWT jwt= new JWT();

    //----------listar para admin

    @GetMapping(path="/listar/",produces= MediaType.APPLICATION_JSON_VALUE)
	public ProductoDTO listar(){
        logger.info("Solicitud recibida: listar todos los productos.->Admin");
		return new ProductoDTO(psi.getListProducto());
	}

    //------Buscador  productos por cantidad  para ADMIN
    @GetMapping(path="/buscarProductoCantidad/{cantidad}",produces= MediaType.APPLICATION_JSON_VALUE)
	public ProductoDTO buscarProductoCantidad(@PathVariable int cantidad){
        logger.info("Solicitud recibida: buscar productos por cantidad.");
		return new ProductoDTO(psi.buscarProductos(cantidad) );
	}

	// ------Buscar productos por nombre o que contenga esa palabra
	@GetMapping(path="/buscarProductoNombreA/{nombre}",produces= MediaType.APPLICATION_JSON_VALUE)
	public ProductoDTO buscarProductoNombreA(@PathVariable String nombre){
        logger.info("Solicitud recibida: buscar productos por nombre (admin).");
		return new ProductoDTO(psi.buscarProductosNombreA(nombre));
	}
 
	// ------Buscar productos por nombre o que contenga esa palabra y que la cantidad!=0
	@GetMapping(path="/buscarProductoNombre/{nombre}",produces= MediaType.APPLICATION_JSON_VALUE)
	public ProductoDTO buscarProductoNombre(@PathVariable String nombre){
        logger.info("Solicitud recibida: buscar productos disponibles por nombre.");
		return new ProductoDTO(psi.buscarProductosNombre(nombre));
	}


	@PostMapping("/save/")
    public ProductoDTO save(@RequestBody ProductoEntity productoEntity){
        logger.info("Solicitud recibida: registrar un producto.");
        return new ProductoDTO(psi.saveProducto(productoEntity));
    }

	@DeleteMapping("/delete/{codigo}")
	public ProductoDTO delete(@PathVariable int codigo){
        logger.info("Solicitud recibida: eliminar producto.");
		try{
		psi.deleteProducto(codigo);;
		return new ProductoDTO("El producto con codigo "+codigo+" se elimino correctamente");
	    }catch(Exception e){
			 return new ProductoDTO("El producto con codigo "+codigo+"no se puede eliminar");
		}
	}

    //---Se resive toda la informacion del producto
	@PutMapping("/update/")
    public ProductoDTO update(@RequestBody ProductoEntity productoEntity){
        logger.info("Solicitud recibida: actualizar producto.");
		return new ProductoDTO(psi.updateProducto(productoEntity));
		 
    }
 
    //---------listar catalogo usuario
    @GetMapping(path="/listarCatalogoUsuario/",produces= MediaType.APPLICATION_JSON_VALUE)
	public ProductoDTO catalogoUsuario(@RequestHeader(value="Authorization", required=false) String authHeader){
        logger.info("Solicitud recibida: listar catálogo de productos disponibles para usuarios.");
		
       try {
        //  Valida token llamando a tu clase JwtUtil
        String username = jwt.validarToken(authHeader);
        logger.info("Usuario autenticado: " + username);

        //  Si el token es válido, devolver catálogo
        return new ProductoDTO(psi.listaProductosCatalogo());

       } catch (Exception e) {
        //  Respuesta estándar si falla el token
        return new ProductoDTO(e.getMessage());
      }	
		
	}



}
