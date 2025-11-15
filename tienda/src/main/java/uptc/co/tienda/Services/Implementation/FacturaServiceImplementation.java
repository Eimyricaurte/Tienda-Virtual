package uptc.co.tienda.Services.Implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uptc.co.tienda.Entities.FacturaEntity;
import uptc.co.tienda.Mangement.FacturaMangement;
import uptc.co.tienda.Services.FacturaService;
import uptc.co.tienda.Services.PdfFacturaService;

@Service
@Transactional
public class FacturaServiceImplementation implements FacturaService {


    @Autowired
	@Qualifier("CrudFactura")
	private FacturaMangement fm;

    @Autowired
    @Qualifier("generarPDFAfilicacionService")
    private PdfFacturaService pdfFacturaService;


    
 
   //listar facturas para ADMIN
    @Override
    public List<FacturaEntity> comprasRealizadas() {
        // TODO Auto-generated method stub
        List<FacturaEntity> facturas = (List<FacturaEntity>) fm.findByTotalIsNotNull();
        for(FacturaEntity facturasE: facturas){
                 pdfFacturaService.generarPDF(facturasE);
        }
         return facturas;
    }

    // Buscador admin
    @Override
    public List<FacturaEntity> fechaTransaccion(String fechaTransaccion) {
        return fm.findByFechaTransaccionAndTotalIsNotNull(fechaTransaccion);
    }

// -------------------------------Cliente

    //---- Facturas por usuario para crear una nueva factura o no
    @Override
    public FacturaEntity usuarioOrderByCodigoFacturaDesc(String correo) {
        return  fm.findFirstByUsuarioCorreoOrderByCodigoFacturaDesc(correo);
    }

    @Override
    public FacturaEntity saveFactura(FacturaEntity facturaEntity) {
        FacturaEntity factura= fm.save(facturaEntity);     
         return factura;   
    } 

    //Busqueda para terminar proceso de pago
    @Override
    public FacturaEntity getFacturaCodigoReferencia(String codigoReferencia) {
        FacturaEntity factura=fm.findByCodigoReferencia(codigoReferencia);
        return factura;
    }

    @Override
    public FacturaEntity updateFactura(FacturaEntity facturaEntity) {
        pdfFacturaService.generarPDF(facturaEntity);
           return fm.save(facturaEntity);
    }

    @Override
    public List<FacturaEntity> facturasUsuario(String correo) {
        return fm.findByUsuarioCorreoContainingAndCodigoReferenciaIsNotNullOrderByCodigoFacturaDesc(correo);
    }


    
    

 }

    
