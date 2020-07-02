package com.puc.sca.api.gateway.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.puc.sca.api.gateway.entity.User;

@Repository
@RepositoryRestResource(path = "usuarios", collectionResourceRel = "usuarios")
public interface UserRepository extends CrudRepository<User, Long> {

	public User findByUsername(String username);

}
