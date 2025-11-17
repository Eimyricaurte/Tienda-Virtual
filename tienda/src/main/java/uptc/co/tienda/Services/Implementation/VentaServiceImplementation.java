package uptc.co.tienda.Services.Implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import uptc.co.tienda.Entities.VentaEntity;
import uptc.co.tienda.Mangement.VentaManagement;
import uptc.co.tienda.Services.VentaService;

@Service
@Transactional
public class VentaServiceImplementation implements VentaService {


    @Autowired
	@Qualifier("CrudVenta")
	private VentaManagement vm;

    @CacheEvict(value ={ 
        "VentaId",
        "VentasSinPago",
        "VentasSinPagoProducto",
        "VentasSinPagoUsuario",
        "VentasPago",
        "VentasPagoProducto",
        "VentasPagoUsuario"
    }, allEntries = true)
    @Override
    public VentaEntity saveVenta(VentaEntity ventaEntity) {
    VentaEntity venta= vm.save(ventaEntity);
        // pdfAfiliacionService.generarAfiliacionPDF(estudiante);
         return venta;    
    }

    @Override
    @Cacheable("VentaId")
    public VentaEntity getVentaId(int id) {
            VentaEntity venta=vm.findById(id).orElseThrow(()->new IllegalArgumentException("El producto no existe")); 
            return venta;
    }

    @Override
    @CacheEvict(value ={ 
        "VentaId",
        "VentasSinPago",
        "VentasSinPagoProducto",
        "VentasSinPagoUsuario",
        "VentasPago",
        "VentasPagoProducto",
        "VentasPagoUsuario"
    }, allEntries = true)
    public void deleteVenta(int id){
        // Eliminar de la base de datos
        vm.deleteById(id);
    }

    @Override
    @CacheEvict(value ={ 
        "VentaId",
        "VentasSinPago",
        "VentasSinPagoProducto",
        "VentasSinPagoUsuario",
        "VentasPago",
        "VentasPagoProducto",
        "VentasPagoUsuario"
    }, allEntries = true)
    public void updateVentaCantidad(VentaEntity venta) {
        vm.save(venta);
        
    }

    @Override
    @Cacheable("VentasSinPago")
    public List<VentaEntity> ventasSinPago() {
          return vm.findByFacturaEntityCodigoReferenciaIsNull();
    }

    @Override
    @Cacheable("VentasSinPagoProducto")
    public List<VentaEntity> ventasSinPagoProducto(String nombre) {
       return vm.findByFacturaEntityCodigoReferenciaIsNullAndProductoNombre(nombre);
    }

    @Override
    @Cacheable("VentasSinPagoUsuario")
    public List<VentaEntity> ventasSinPagoUsuario(String correo) {
       return vm.findByFacturaEntityCodigoReferenciaIsNullAndFacturaEntityUsuarioCorreo(correo);
    }

    @Override
    @Cacheable("VentasPago")
    public List<VentaEntity> ventasPago() {
        return vm.findByFacturaEntityCodigoReferenciaIsNotNull();  
    }

    @Override
    @Cacheable("VentasPagoProducto")
    public List<VentaEntity> ventasPagoProducto(String nombre) {
        return vm.findByFacturaEntityCodigoReferenciaIsNotNullAndProductoNombre(nombre);
    }

    @Override
    @Cacheable("VentasPagoUsuario")
    public List<VentaEntity> ventasPagoUsuario(String correo) {
       return vm.findByFacturaEntityCodigoReferenciaIsNotNullAndFacturaEntityUsuarioCorreo(correo);
    }
    

}
