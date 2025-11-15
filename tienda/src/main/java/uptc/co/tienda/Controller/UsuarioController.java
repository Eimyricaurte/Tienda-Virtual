package uptc.co.tienda.Controller;

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

import uptc.co.tienda.DTO.LoginDTO;
import uptc.co.tienda.DTO.UsuarioDTO;
import uptc.co.tienda.Entities.UsuarioEntity;
import uptc.co.tienda.Services.Implementation.UsuarioServiceImplementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/usuario")
@CrossOrigin(origins="*")
public class UsuarioController {

    	private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
	@Qualifier("usuarioServiceImplementation")
	private UsuarioServiceImplementation usi;

	@PostMapping(path="/login/")
	public UsuarioDTO login(@RequestBody LoginDTO loginDTO){
		logger.info("El usuario ingreso al sistema");
		return new UsuarioDTO(usi.login(loginDTO.getCorreo(), loginDTO.getClave()));
	}
    
	@GetMapping(path="/listar/",produces= MediaType.APPLICATION_JSON_VALUE)
	public UsuarioDTO listar(){
		logger.info("El usuario ingreso al sistema");
		return new UsuarioDTO(usi.getListUsuario());
	}

	@PostMapping("/save/")
    public UsuarioDTO save(@RequestBody UsuarioEntity usuarioEntity){
		logger.info("El usuario ingreso al sistema--Registrar ---");
        return new UsuarioDTO(usi.saveUsuario(usuarioEntity));
    }

	@PutMapping("/update/")
    public UsuarioDTO update(@RequestBody UsuarioEntity usuarioEntity){
        logger.info("El usuario ingreso al sistema--Editar ---");
		try{
           UsuarioEntity existtingUsuario=usi.getUsuarioId(usuarioEntity.getCorreo());
			existtingUsuario.setNumeroDocumento(usuarioEntity.getNumeroDocumento());
			existtingUsuario.setNombre(usuarioEntity.getNombre());
			existtingUsuario.setClave(usuarioEntity.getClave());
			existtingUsuario.setTelefono(usuarioEntity.getTelefono());
			return new UsuarioDTO(usi.updateUsuario(existtingUsuario));
		}catch(Exception e){
			 return new UsuarioDTO(e.getMessage());
		}  
    }
	
	@DeleteMapping("/delete/{id}")
	public UsuarioDTO delete(@PathVariable String correo){
		logger.info("El usuario ingreso al sistema--Eliminar ---");
		try{
		UsuarioEntity existtingEstudiante=usi.getUsuarioId(correo);
		usi.deleteUsuario(correo);
		return new UsuarioDTO(existtingEstudiante);
	    }catch(Exception e){
			 return new UsuarioDTO(e.getMessage());
		}
	}

	@GetMapping(path = "/usuario/{correo}")
	public UsuarioDTO getUsuarioPorCorreo(@PathVariable String correo) {
		try {
			UsuarioEntity usuario = usi.getUsuarioId(correo);
			return new UsuarioDTO(usuario); // Devuelve el usuario encontrado
		} catch (IllegalArgumentException e) {
			return new UsuarioDTO(e.getMessage()); // Mensaje: "El estudiante no existe"
		} 
	}

}
