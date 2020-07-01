package com.puc.sca.api.gateway.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.puc.sca.api.gateway.entity.Usuario;

@Repository
@RepositoryRestResource(path = "usuarios", collectionResourceRel = "usuarios")
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

	public Usuario findByEmailAndPassword(String email, String password);

}
