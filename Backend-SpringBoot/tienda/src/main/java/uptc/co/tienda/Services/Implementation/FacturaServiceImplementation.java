package uptc.co.tienda.Services.Implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

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

 
   //listar facturas para ADMIN
    @Override
    @Cacheable("ListarFacturasConPago")
    public List<FacturaEntity> comprasRealizadas() {
        List<FacturaEntity> facturas = (List<FacturaEntity>) fm.findByTotalIsNot(0);
         return facturas;
    }

    // Buscador admin
    @Override
    @Cacheable("ListarFacturasPorRangoFechas")
    public List<FacturaEntity> obtenerFacturasPorRangoFechas(String fechaInicio, String fechaFin) {
        String fechaInicioCompleta = fechaInicio + " 00:00:00";
        String fechaFinCompleta = fechaFin + " 23:59:59";
        
        return fm.findByFechaTransaccionBetweenAndTotalNot(fechaInicioCompleta, fechaFinCompleta, 0);   
    }

    @Override
    @Cacheable("ListarFacturasEstado")
    public List<FacturaEntity> findByEstado(String estado) {
        return fm.findByEstadoAndTotalNot(estado, 0);
    }

// -------------------------------Cliente

    //---- Facturas por usuario para crear una nueva factura o no
    @Override
    @Cacheable("ListarFacturasUsuario")
    public FacturaEntity usuarioOrderByCodigoFacturaDesc(String correo) {
        return  fm.findFirstByUsuarioCorreoOrderByCodigoFacturaDesc(correo);
    }

    @Override
    @CacheEvict(value = { 
    "ListarFacturasConPago", 
    "ListarFacturasPorRangoFechas",
    "ListarFacturasEstado",
    "ListarFacturasUsuario",
    "busquedaFactura",
    "ListarFacturaUsuarioPago",
        "VentaId",
        "VentasSinPago",
        "VentasSinPagoProducto",
        "VentasSinPagoUsuario",
        "VentasPago",
        "VentasPagoProducto",
        "VentasPagoUsuario"
    }, allEntries = true)
    public FacturaEntity saveFactura(FacturaEntity facturaEntity) {
        FacturaEntity factura= fm.save(facturaEntity);     
         return factura;   
    } 

    //Busqueda para terminar proceso de pago
    @Override
    @Cacheable("busquedaFactura")
    public FacturaEntity getFacturaCodigoReferencia(String codigoReferencia) {
        FacturaEntity factura=fm.findByCodigoReferencia(codigoReferencia);
        return factura;
    }

    @Override
    @CacheEvict(value = { 
    "ListarFacturasConPago", 
    "ListarFacturasPorRangoFechas",
    "ListarFacturasEstado",
    "ListarFacturasUsuario",
    "busquedaFactura",
    "ListarFacturaUsuarioPago",
        "VentaId",
        "VentasSinPago",
        "VentasSinPagoProducto",
        "VentasSinPagoUsuario",
        "VentasPago",
        "VentasPagoProducto",
        "VentasPagoUsuario"
    }, allEntries = true)
    public FacturaEntity updateFactura(FacturaEntity facturaEntity) {
        FacturaEntity guardada = fm.save(facturaEntity);
        if(guardada.getEstado()!=null){   
            PdfFacturaService pdfFacturaService=new PdfFacturaService();
           pdfFacturaService.generarPDF(guardada);
        }
        return guardada;
    }

    @Override
    @Cacheable("ListarFacturaUsuarioPago")
    public List<FacturaEntity> facturasUsuario(String correo) {
        return fm.findByUsuarioCorreoContainingAndCodigoReferenciaIsNotNullOrderByCodigoFacturaDesc(correo);
    }

 }

    
