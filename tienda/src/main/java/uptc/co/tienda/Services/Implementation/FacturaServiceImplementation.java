package uptc.co.tienda.Services.Implementation;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uptc.co.tienda.Entities.FacturaEntity;
import uptc.co.tienda.Entities.UsuarioEntity;
import uptc.co.tienda.Mangement.FacturaMangement;
import uptc.co.tienda.Services.FacturaService;

@Service
@Transactional
public class FacturaServiceImplementation implements FacturaService {


    @Autowired
	@Qualifier("CrudFactura")
	private FacturaMangement fm;

    @Override
    public List<FacturaEntity> getListFacturas() {
        System.out.println("LISTADO");
        List<FacturaEntity> facturas = (List<FacturaEntity>) fm.findAll();

        return facturas;
    }

    @Override
    public FacturaEntity saveFactura(FacturaEntity facturaEntity) {
        FacturaEntity factura= fm.save(facturaEntity);
        // pdfAfiliacionService.generarAfiliacionPDF(estudiante);
     
         return factura;   
     }

    @Override
    public FacturaEntity updateFactura(FacturaEntity facturaEntity) {
                return fm.save(facturaEntity);

    }

    @Override
    public FacturaEntity getFacturaCodigoFactura(int codigoFactura) {

        FacturaEntity factura=fm.findById(codigoFactura).orElseThrow(()->new IllegalArgumentException("El estudiante no existe"));
        return factura;
    }

    @Override
    public FacturaEntity getFacturaCodigoReferencia(String codigoReferencia) {
        return fm.findByCodigoReferencia(codigoReferencia);
    }

    @Override
    public List<FacturaEntity> facturaUsuario_Correo(String correo) {
            return fm.findByUsuario_Correo(correo);
   
    }

    @Override
    public List<FacturaEntity> fechaTransaccion(String fechaTransaccion) {
        return fm.findByFechaTransaccion(fechaTransaccion);

    }

    @Override
    public FacturaEntity usuarioOrderByCodigoFacturaDesc(String correo) {
        // TODO Auto-generated method stub
        return  fm.findFirstByUsuarioCorreoOrderByCodigoFacturaDesc(correo);
    }

    
    

   

      


}
