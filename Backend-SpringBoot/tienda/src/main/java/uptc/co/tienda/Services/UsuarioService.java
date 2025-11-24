package uptc.co.tienda.Services;

import java.util.List;

import uptc.co.tienda.Entities.UsuarioEntity;

public interface UsuarioService {

   public abstract List<UsuarioEntity> getListUsuario();
   public abstract UsuarioEntity saveUsuario(UsuarioEntity usuarioEntity);
   public abstract UsuarioEntity getUsuarioId(String correo);
   public abstract UsuarioEntity updateUsuario(UsuarioEntity usuarioEntity);
   public abstract void deleteUsuario(String correo);
   public abstract UsuarioEntity login(String correo, String clave);

}
