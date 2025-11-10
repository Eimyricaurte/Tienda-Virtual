package uptc.co.tienda.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import uptc.co.tienda.DTO.VentaDTO;
import uptc.co.tienda.Entities.FacturaEntity;
import uptc.co.tienda.Entities.VentaEntity;
import uptc.co.tienda.Services.Implementation.FacturaServiceImplementation;
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

        private static final Logger logger = LoggerFactory.getLogger(VentaController.class);


    @GetMapping(path="/listar/",produces= MediaType.APPLICATION_JSON_VALUE)
	public VentaDTO listar(){
		logger.info("El usuario ingreso al sistema");
		return new VentaDTO( vsi.getListVentas());
	}

	@PostMapping("/save/")
    public VentaDTO save(@RequestBody VentaEntity ventaEntity){
		logger.info("El usuario ingreso al sistema--Registrar ---");
		FacturaEntity fact=fsi.usuarioOrderByCodigoFacturaDesc(ventaEntity.getFacturaEntity().getUsuario().getCorreo());
        if(fact.getCodigoReferencia()!=null ){
			 fact = new FacturaEntity(fact.getUsuario());
             fact=fsi.saveFactura(fact); 
		}
		 ventaEntity.setFacturaEntity(fact);
		/** FacturaEntity fact= ventaEntity.getFacturaEntity();
		if(fact.getCodigoFactura()==0 ){
			 fact = new FacturaEntity(fact.getUsuario());
             fact=fsi.saveFactura(fact); 
		}
       */

        return new VentaDTO(vsi.saveVenta(ventaEntity));
    }

	

}
