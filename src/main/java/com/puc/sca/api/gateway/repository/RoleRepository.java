package com.puc.sca.api.gateway.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.puc.sca.api.gateway.entity.Role;

@RepositoryRestResource(path = "permissoes", collectionResourceRel = "permissoes")
public interface RoleRepository extends CrudRepository<Role, Long> {

}
