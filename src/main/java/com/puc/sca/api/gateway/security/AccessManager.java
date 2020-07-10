package com.puc.sca.api.gateway.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.puc.sca.api.gateway.entity.User;

@Component("accessManager")
public class AccessManager {
	
	/**
	 * Regra que permite o update do usuário apenas apenas se o usuário logado (preenchido no token) é o mesmo do usuário a ser updatado. Ou seja garante que o update de usuário
	 * só pode ser feito pelo próprio usuário e não por outros usuários.
	 * @param id
	 * @return
	 */
	public boolean isOwner(Long idUsuarioParaSerAlterado) {
		User userAutenticado = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return userAutenticado.getId().equals(idUsuarioParaSerAlterado);
	}

}
