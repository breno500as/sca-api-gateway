package com.puc.sca.api.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("teste")
public class TesteSegurancaController {

	//@Secured("ADMIN")
	@GetMapping("metodo")
	@ResponseBody
	public String testeMetodoAutenticado() {
		System.out.println("aaaaa");
		return "{\"resposta\": \"XYZ\"}";
	}

}
