package uptc.co.tienda.Controller;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import uptc.co.tienda.DTO.FacturaDTO;
import uptc.co.tienda.DTO.FechaDTO;
import uptc.co.tienda.Entities.FacturaEntity;
import uptc.co.tienda.Services.Implementation.FacturaServiceImplementation;

import org.springframework.http.HttpHeaders;
import java.io.File;



@RestController
@RequestMapping("/factura")
@CrossOrigin(origins="*")
public class FacturaController {

    private static final Logger logger = LoggerFactory.getLogger(FacturaController.class);

    @Autowired
	@Qualifier("facturaServiceImplementation")
	private FacturaServiceImplementation fsi;

	
    private final String apiKey = "4Vj8eK4rloUd272L48hsrarnUA";             // Sandbox API Key
    private final String merchantId = "508029";     // Sandbox Merchant ID
    private final String accountId = "512321";       // Sandbox Account ID
    private final String payuUrl = "https://sandbox.checkout.payulatam.com/ppp-web-gateway-payu/";



 // ----listar facturas para el ADMIN
    @GetMapping(path="/listar/",produces= MediaType.APPLICATION_JSON_VALUE)
	public FacturaDTO listar(){
        logger.info("Solicitud para listar todas las facturas.");
		return new FacturaDTO( fsi.comprasRealizadas());
	}
 
    
    //Busqueda Admin
    @GetMapping("/fecha/")
    public FacturaDTO obtenerFacturasPorFecha(@RequestBody FechaDTO fechaDTO) {
        logger.info("Solicitud para consultar facturas por rango de fechas.");
        return new FacturaDTO(fsi.obtenerFacturasPorRangoFechas(fechaDTO.getFechaInicio(), fechaDTO.getFechaFin()));
    }

     //Busqueda Admin  Estado de la transacción (4=aprobado, 6=rechazado, 7=pendiente)
    @GetMapping("/estado/{estado}")
    public FacturaDTO obtenerFacturasPorFecha(@PathVariable String estado) {
           logger.info("Solicitud para consultar facturas por estado de transacción.");
        return new FacturaDTO(fsi.findByEstado(estado));
    }
 //----
	
         // ---Pago  
	@PostMapping("/payment")
    public Map<String, String> createPayment(@RequestBody FacturaEntity request) {
       
        logger.info("Solicitud para iniciar proceso de pago.");

        Map<String, String> data = new HashMap<>();

        String referenceCode = UUID.randomUUID().toString();
        String amount = String.format(Locale.US, "%.2f",request.getTotal());
        String currency = "COP";

        request.setCodigoReferencia(referenceCode);
        fsi.updateFactura(request);

        // Generar firma MD5
        String signature = DigestUtils.md5Hex(apiKey + "~" + merchantId + "~" + referenceCode + "~" + amount + "~" + currency);

        data.put("merchantId", merchantId);
        data.put("accountId", accountId);
        data.put("description", "Descipcion...");
        data.put("referenceCode", referenceCode);
        data.put("amount", amount);
        data.put("tax", "0");
        data.put("taxReturnBase", "0");
        data.put("currency", currency);
        data.put("signature", signature);
        data.put("buyerEmail", request.getUsuario().getCorreo()); //npx localtunnel --port 8081 --subdomain proyecto8081
        data.put("responseUrl", "https://proyecto8081.loca.lt/factura/response");
        data.put("confirmationUrl", "https://proyecto8081.loca.lt/factura/confirmation");

        data.put("action", payuUrl); // URL a la que se hace POST

        return data;
    }

    //---Realizo la factura
	@PostMapping("/confirmation")
    public FacturaDTO confirmation(@RequestParam Map<String, String> params) {
         
        logger.info("Confirmación de pago recibida desde el proveedor y actaulizacion en la db.");
       
        // Aquí validas la firma recibida y actualizas tu base de datos
        FacturaEntity facturaEntity=fsi.getFacturaCodigoReferencia(params.get("reference_sale"));
            facturaEntity.setEstado(params.get("state_pol")); // Estado de la transacción (4=aprobado, 6=rechazado, 7=pendiente)
            System.out.println("Estado " + facturaEntity.getEstado());
            facturaEntity.setEstadoTransaccion(params.get("response_message_pol"));
            facturaEntity.setTransaccionId(params.get("transaction_id")); // ID único de la transacción en PayU
            facturaEntity.setOrdenIdPay(params.get("reference_pol")); // ID interno de la orden en PayU
            facturaEntity.setCodigoAutorizacionBanco(params.get("authorization_code")); // Código de autorización del banco
            facturaEntity.setFechaTransaccion(params.get("transaction_date"));
        
            return new FacturaDTO(fsi.updateFactura(facturaEntity));
    }

    //----pagina 
     @GetMapping("/response")
    public String response(@RequestParam Map<String, String> params) {
        logger.info("Solicitud para mostrar el resultado de la transacción al usuario.");
        return "Resultado de la transacción: " + params.get("transactionState");
    }


    //--Compras realizadas por usuario------------//

    @GetMapping("/usuarioFacturas/{correo}")
    public FacturaDTO usuarioFacturas(@PathVariable String correo) {
        logger.info("Solicitud para listar facturas de un usuario.");
        return new FacturaDTO(fsi.facturasUsuario(correo));
    }

    @GetMapping("/descargar/{id}")
	    public ResponseEntity<FileSystemResource> descargarPdf(@PathVariable int id) {
	           logger.info("Solicitud para descargar archivo PDF de factura.");


            String fileName = "Factura_" + id + ".pdf";
	        File file = new File(fileName);

	        if (!file.exists()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	        }

	        FileSystemResource resource = new FileSystemResource(file);
	        HttpHeaders headers = new HttpHeaders();
	        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

	        return ResponseEntity.ok()
	                .headers(headers)
	                .body(resource);
	    }


}
