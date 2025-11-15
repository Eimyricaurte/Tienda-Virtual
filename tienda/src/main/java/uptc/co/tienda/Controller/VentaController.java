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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import uptc.co.tienda.DTO.VentaDTO;
import uptc.co.tienda.Entities.FacturaEntity;
import uptc.co.tienda.Entities.ProductoEntity;
import uptc.co.tienda.Entities.VentaEntity;
import uptc.co.tienda.Services.Implementation.FacturaServiceImplementation;
import uptc.co.tienda.Services.Implementation.ProductoServiceImplementation;
import uptc.co.tienda.Services.Implementation.VentaServiceImplementation;

@RestController
@RequestMapping("/venta")
@CrossOrigin(origins="*")
public class VentaController {

    @Autowired
	@Qualifier("ventaServiceImplementation")
	private VentaServiceImplementation vsi;

	@Autowired
	@Qualifier("facturaServiceImplementation")
	private FacturaServiceImplementation fsi;

	@Autowired
	@Qualifier("productoServiceImplementation")
	private ProductoServiceImplementation psi;

        private static final Logger logger = LoggerFactory.getLogger(VentaController.class);


    @GetMapping(path="/listar/",produces= MediaType.APPLICATION_JSON_VALUE)
	public VentaDTO listar(){
		logger.info("El usuario ingreso al sistema");
		return new VentaDTO( vsi.getListVentas());
	}

// Registro producto en el carrito
	@PostMapping("/saveProductoCompra/")
    public VentaDTO saveProductoCompra(@RequestBody VentaEntity ventaEntity){
		logger.info("El usuario ingreso al sistema--Registrar ---");
		FacturaEntity fact=fsi.usuarioOrderByCodigoFacturaDesc(ventaEntity.getFacturaEntity().getUsuario().getCorreo());
       
		if (fact == null) {
			// Crear una factura nueva usando el usuario que viene desde la venta
			fact = new FacturaEntity(ventaEntity.getFacturaEntity().getUsuario());
			fact = fsi.saveFactura(fact);
		} 
		else if( fact==null ||fact.getCodigoReferencia()!=null ){
			 fact = new FacturaEntity(fact.getUsuario());
             fact=fsi.saveFactura(fact); 
		}
         ProductoEntity productoExistente=psi.getProductoCodigo(ventaEntity.getProducto().getCodigo());
		 int  cantidad=ventaEntity.getProducto().getCantidad()-ventaEntity.getCantidad();
		 productoExistente.setCantidad(cantidad);
		 psi.updateProducto(productoExistente);
		 ventaEntity.setFacturaEntity(fact);
        return new VentaDTO(vsi.saveVenta(ventaEntity));
    }

// ---Ver carro
    @GetMapping(path="/carro/{correo}",produces= MediaType.APPLICATION_JSON_VALUE)
	public VentaDTO carro(@PathVariable String correo){
  
		FacturaEntity fact=fsi.usuarioOrderByCodigoFacturaDesc(correo);
		if (fact == null) {
			System.out.println("null");
			return new VentaDTO("No hay productos en el carro");
		}
        else if(fact.getCodigoReferencia()==null ){
						System.out.println("venta");

			return new VentaDTO(fact.getListaVentaEntity());
		}
		return new VentaDTO("No hay productos en el carro");
	}

// ---Eliminar producto del carro
    @DeleteMapping(path="/eliminarVenta/{idVenta}",produces= MediaType.APPLICATION_JSON_VALUE)
	public VentaDTO eliminarVentaPro(@PathVariable int idVenta){
  
	    VentaEntity venta=vsi.getVentaId(idVenta);
		ProductoEntity productoExistente=psi.getProductoCodigo(venta.getProducto().getCodigo());
		 int  cantidad=venta.getProducto().getCantidad()+venta.getCantidad();
		 productoExistente.setCantidad(cantidad);
		 psi.updateProducto(productoExistente);
		 vsi.deleteVenta(idVenta);
		 return new VentaDTO("Producto eliminado del carro");
	}

//  -------Editar cantidad de un producto en el carro
    @PutMapping("/editarProductoUsuario/")
    public VentaDTO editarProductoUsuario(@RequestBody VentaEntity ventaEntity) {
        int  cantidad=0;
		VentaEntity venta=vsi.getVentaId(ventaEntity.getId());
		ProductoEntity productoExistente=psi.getProductoCodigo(venta.getProducto().getCodigo());
		if(ventaEntity.getCantidad()>venta.getCantidad()){

			int cantidadAgregar= ventaEntity.getCantidad()-venta.getCantidad();
			cantidad=productoExistente.getCantidad()-cantidadAgregar;

		}else if (ventaEntity.getCantidad()<venta.getCantidad()){
			int cantidadAgregar= venta.getCantidad()-ventaEntity.getCantidad();
			cantidad=productoExistente.getCantidad()+cantidadAgregar;

		}
		productoExistente.setCantidad(cantidad);
		 psi.updateProducto(productoExistente);
		 venta.setCantidad(ventaEntity.getCantidad());
		 venta.setPrecioTotal(ventaEntity.getPrecioTotal());
		 vsi.updateVentaCantidad(venta);
		 return new VentaDTO("Producto editado correctamente");
		
    }
    
    



	

}
