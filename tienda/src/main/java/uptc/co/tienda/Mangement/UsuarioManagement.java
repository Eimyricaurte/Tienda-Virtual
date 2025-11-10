package uptc.co.tienda.Mangement;

import org.springframework.stereotype.Repository;

import uptc.co.tienda.Entities.UsuarioEntity;

import org.springframework.data.repository.CrudRepository;

@Repository("CrudUsuario")
public interface UsuarioManagement extends CrudRepository<UsuarioEntity, String>{

}
