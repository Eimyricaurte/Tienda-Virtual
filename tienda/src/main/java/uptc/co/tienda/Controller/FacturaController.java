package uptc.co.tienda.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import uptc.co.tienda.DTO.FacturaDTO;
import uptc.co.tienda.DTO.VentaDTO;
import uptc.co.tienda.Entities.FacturaEntity;
import uptc.co.tienda.Services.Implementation.FacturaServiceImplementation;



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




    @GetMapping(path="/listar/",produces= MediaType.APPLICATION_JSON_VALUE)
	public FacturaDTO listar(){
		logger.info("El usuario ingreso al sistema");
		return new FacturaDTO( fsi.getListFacturas());
	}
    //Listar facturas por usuario
    @GetMapping("/usuarioFacturas/{correo}")
    public List<FacturaEntity> obtenerFacturasPorUsuario(@PathVariable String correo) {
        return fsi.facturaUsuario_Correo(correo);
    }

    @GetMapping("/fecha/{fecha}")
    public List<FacturaEntity> obtenerFacturasPorFecha(@PathVariable String fecha) {
        return fsi.fechaTransaccion(fecha);
    }

	

	@PostMapping("/payment")
    public Map<String, String> createPayment(@RequestBody FacturaEntity request) {
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

	@PostMapping("/confirmation")
    public FacturaDTO confirmation(@RequestParam Map<String, String> params) {
        System.out.println("Confirmación recibida: " + params);
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

    @GetMapping("/response")
    public String response(@RequestParam Map<String, String> params) {
        // Muestra el resultado al usuario
        return "Resultado de la transacción: " + params.get("transactionState");
    }

    @GetMapping("/verVenta")
    public VentaDTO verCarrito(@RequestParam String correo) {


		FacturaEntity fact=fsi.usuarioOrderByCodigoFacturaDesc(correo);

		if(fact.getCodigoReferencia()!=null){
			return new VentaDTO(fact.getListaVentaEntity());

		}
     

	  return new VentaDTO("00");
	}
}
