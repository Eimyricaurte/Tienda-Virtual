package uptc.co.tienda.Services.Implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uptc.co.tienda.Entities.VentaEntity;
import uptc.co.tienda.Mangement.VentaManagement;
import uptc.co.tienda.Services.VentaService;

@Service
@Transactional
public class VentaServiceImplementation implements VentaService {


    @Autowired
	@Qualifier("CrudVenta")
	private VentaManagement vm;

    @Override
    public List<VentaEntity> getListVentas() {
        System.out.println("LISTADO");
        List<VentaEntity> ventas = (List<VentaEntity>) vm.findAll();

        return ventas;
    }

    @Override
    public VentaEntity saveVenta(VentaEntity ventaEntity) {
    VentaEntity venta= vm.save(ventaEntity);
        // pdfAfiliacionService.generarAfiliacionPDF(estudiante);
         return venta;    
    }

    @Override
    public VentaEntity getVentaId(int id) {
            VentaEntity venta=vm.findById(id).orElseThrow(()->new IllegalArgumentException("El producto no existe")); 
            return venta;
    }

    @Override
    public void deleteVenta(int id){
        // Eliminar de la base de datos
        vm.deleteById(id);
    }

    @Override
    public void updateVentaCantidad(VentaEntity venta) {
        vm.save(venta);
        
    }
    

}
