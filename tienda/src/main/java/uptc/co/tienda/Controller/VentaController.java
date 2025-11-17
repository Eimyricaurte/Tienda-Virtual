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


   
    @GetMapping(path="/listarVentasSinPago/",produces= MediaType.APPLICATION_JSON_VALUE)
	public VentaDTO listar(){
        logger.info("Solicitud para listar ventas sin pago.");
		return new VentaDTO( vsi.ventasSinPago());
	}


    @GetMapping(path="/ventasSinPagoProducto/{nombre}",produces= MediaType.APPLICATION_JSON_VALUE)
	public VentaDTO ventasSinPagoProducto(@PathVariable String nombre){
		logger.info("Solicitud para obtener ventas sin pago filtradas por producto.");
		return new VentaDTO(vsi.ventasSinPagoProducto(nombre));
	}

    @GetMapping(path="/ventasSinPagoUsuario/{correo}",produces= MediaType.APPLICATION_JSON_VALUE)
	public VentaDTO ventasSinPagoUsuario(@PathVariable String correo){
		logger.info("Solicitud para obtener ventas sin pago filtradas por usuario.");
		return new VentaDTO(vsi.ventasSinPagoUsuario(correo));
	}

	@GetMapping(path="/listarVentasPago/",produces= MediaType.APPLICATION_JSON_VALUE)
	public VentaDTO listarVentasPago(){
        logger.info("Solicitud para listar ventas pagadas.");
		return new VentaDTO( vsi.ventasPago());
	}

    @GetMapping(path="/ventasPagoProducto/{nombre}",produces= MediaType.APPLICATION_JSON_VALUE)
	public VentaDTO ventasPagoProducto(@PathVariable String nombre){
		    logger.info("Solicitud para obtener ventas pagadas filtradas por producto.");
		return new VentaDTO(vsi.ventasPagoProducto(nombre));
	}

    @GetMapping(path="/ventasPagoUsuario/{correo}",produces= MediaType.APPLICATION_JSON_VALUE)
	public VentaDTO ventasPagoUsuario(@PathVariable String correo){
		    logger.info("Solicitud para obtener ventas pagadas filtradas por usuario.");
		return new VentaDTO(vsi.ventasPagoUsuario(correo));
	}


// Registro producto en el carrito
	@PostMapping("/saveProductoCompra/")
    public VentaDTO saveProductoCompra(@RequestBody VentaEntity ventaEntity){
       
		logger.info("Solicitud para registrar un producto en el carrito.");
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
        logger.info("Solicitud para visualizar productos del carrito.");

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
      logger.info("Solicitud para eliminar un producto del carrito.");

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
           logger.info("Solicitud para editar la cantidad de un producto en el carrito.");


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
