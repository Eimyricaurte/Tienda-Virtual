package uptc.co.tienda.Services.Implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import jakarta.transaction.Transactional;
import uptc.co.tienda.Entities.UsuarioEntity;
import uptc.co.tienda.Mangement.UsuarioManagement;
import uptc.co.tienda.Services.UsuarioService;

    @Service
    @Transactional
public class UsuarioServiceImplementation implements UsuarioService {

     @Autowired
	@Qualifier("CrudUsuario")
	private UsuarioManagement um;


    @Override
    @Cacheable("ListarUsuarios")
	public List<UsuarioEntity> getListUsuario() {
        List<UsuarioEntity> usuarios= (List<UsuarioEntity>) um.findAll();
       
		return usuarios;
	}

    @Override
    @CacheEvict(value ={ 
    "ListarUsuarios",
    "usuario",
    "Login"
    }, allEntries = true)
    public UsuarioEntity saveUsuario(UsuarioEntity usuarioEntity){
         UsuarioEntity usuario= um.save(usuarioEntity);
        // pdfAfiliacionService.generarAfiliacionPDF(estudiante);
         return usuario;
    }

    @Override
    @Cacheable("usuario")
    public UsuarioEntity getUsuarioId(String correo){
        UsuarioEntity estudiante=um.findById(correo).orElseThrow(()->new IllegalArgumentException("El usuario no existe"));
        return estudiante;
    }
    
     @Override
    @CacheEvict(value ={ 
    "ListarUsuarios",
    "usuario",
    "Login"
    }, allEntries = true)     
    public UsuarioEntity updateUsuario(UsuarioEntity usuarioEntity){
         return um.save(usuarioEntity);
     }

    @Override
    @CacheEvict(value ={ 
        "ListarUsuarios",
        "usuario",
        "Login"
    }, allEntries = true)    
    public void deleteUsuario(String correo){
        // Eliminar de la base de datos
        um.deleteById(correo);
    }

    @Override
    public String login(String correo, String clave){
       try {
            UsuarioEntity usuarioEntity = getUsuarioId(correo);

            if (usuarioEntity.getClave().equals(clave)) {
                return usuarioEntity.getCorreo(); // login correcto
            } else {
                return "Clave incorrecta"; // clave incorrecta
            }
        } catch (IllegalArgumentException e) {
            // El usuario no existe
            return "El usuario no existe";
        }
    }
}
