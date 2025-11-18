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
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


@RestController
@RequestMapping("/usuario")
@CrossOrigin(origins="*")
public class UsuarioController {

    	private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);
		private static final String secretKey = "bXktc2VjdXJlLXNlY3JldC1rZXktYmFzZTY0LWF1dGg=";


    @Autowired
	@Qualifier("usuarioServiceImplementation")
	private UsuarioServiceImplementation usi;

	@PostMapping(path="/login/")
	public UsuarioDTO login(@RequestBody LoginDTO loginDTO){

       try {
            UsuarioEntity usuarioEntity = usi.login(loginDTO.getCorreo(), loginDTO.getClave());

            if (usuarioEntity.getClave().equals(loginDTO.getClave())) {
				 String token = Jwts.builder()
                    .setSubject(loginDTO.getCorreo())
                    .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)), SignatureAlgorithm.HS256)
                    .compact();

                return new UsuarioDTO(usuarioEntity.getCorreo(), token); // login correcto

            } else {
                return new UsuarioDTO("Clave incorrecta"); // clave incorrecta
            }
        } catch (IllegalArgumentException e) {
            // El usuario no existe
            return new UsuarioDTO("El usuario no existe");
        }

	}
    
	@GetMapping(path="/listar/",produces= MediaType.APPLICATION_JSON_VALUE)
	public UsuarioDTO listar(){
		logger.info("El administrador ingreso al sistema ---Listar usarios");
		return new UsuarioDTO(usi.getListUsuario());
	}

	@PostMapping("/save/")
    public UsuarioDTO save(@RequestBody UsuarioEntity usuarioEntity){
		logger.info("El usuario ingreso al sistema--Registrar usuarios ---");
        return new UsuarioDTO(usi.saveUsuario(usuarioEntity));
    }

	@PutMapping("/update/")
    public UsuarioDTO update(@RequestBody UsuarioEntity usuarioEntity){
        logger.info("El administrador ingreso al sistema--Editar usuario---");
		try{
           UsuarioEntity existtingUsuario=usi.getUsuarioId(usuarioEntity.getCorreo());
			existtingUsuario.setNumeroDocumento(usuarioEntity.getNumeroDocumento());
			existtingUsuario.setNombre(usuarioEntity.getNombre());
			existtingUsuario.setClave(usuarioEntity.getClave());
			existtingUsuario.setTelefono(usuarioEntity.getTelefono());
			return new UsuarioDTO("Usuario editado correctamente");
		}catch(Exception e){
			 return new UsuarioDTO(e.getMessage());
		}  
    }
	
	@DeleteMapping("/delete/{correo}")
	public UsuarioDTO delete(@PathVariable String correo){
		logger.info("El administrador ingreso al sistema--Eliminar usuario---");
		try{
		usi.deleteUsuario(correo);
		return new UsuarioDTO("El usuario con correo "+correo+" se elimino correctamente");
	    }catch(Exception e){
			 return new UsuarioDTO("El usuario no se puede eliminar");
		}
	}

	@GetMapping(path = "/buscarUsuario/{correo}")
	public UsuarioDTO getUsuarioPorCorreo(@PathVariable String correo) {
		logger.info("El administrador ingreso al sistema--Buscar usuario---");
		try {
			UsuarioEntity usuario = usi.getUsuarioId(correo);
			return new UsuarioDTO(usuario); // Devuelve el usuario encontrado
		} catch (IllegalArgumentException e) {
			return new UsuarioDTO("El usuario con correo "+correo+" no esta registrado"); // Mensaje: "El estudiante no existe"
		} 
	}

}
