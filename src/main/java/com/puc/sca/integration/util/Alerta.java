package com.puc.sca.integration.util;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * TODO - Mover classes e pacotes para um repositório local (nexus por exemplo).
 * @author breno
 *
 */

public class Alerta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8185461749738693924L;

	private NivelAlerta nivel;
	
	private String observacoesTecnicasMonitoramento;

	private String parecerMelhoriasSeguranca;

	private LocalDateTime dataInformacao = LocalDateTime.now();

	private String nomeUsuarioLogado;

	private String emailUsuarioLogado;
	

	public Alerta() {
	}

	public Alerta(NivelAlerta nivel) {
		this.nivel = nivel;
	}

	public NivelAlerta getNivel() {
		return nivel;
	}

	public void setNivel(NivelAlerta nivel) {
		this.nivel = nivel;
	}

	public String getParecerMelhoriasSeguranca() {
		return parecerMelhoriasSeguranca;
	}

	public void setParecerMelhoriasSeguranca(String parecerMelhoriasSeguranca) {
		this.parecerMelhoriasSeguranca = parecerMelhoriasSeguranca;
	}

	public String getObservacoesTecnicasMonitoramento() {
		return observacoesTecnicasMonitoramento;
	}

	public void setObservacoesTecnicasMonitoramento(String observacoesTecnicasMonitoramento) {
		this.observacoesTecnicasMonitoramento = observacoesTecnicasMonitoramento;
	}

	public LocalDateTime getDataInformacao() {
		return dataInformacao;
	}

	public void setDataInformacao(LocalDateTime dataInformacao) {
		this.dataInformacao = dataInformacao;
	}

	public String getNomeUsuarioLogado() {
		return nomeUsuarioLogado;
	}

	public void setNomeUsuarioLogado(String nomeUsuarioLogado) {
		this.nomeUsuarioLogado = nomeUsuarioLogado;
	}

	public String getEmailUsuarioLogado() {
		return emailUsuarioLogado;
	}

	public void setEmailUsuarioLogado(String emailUsuarioLogado) {
		this.emailUsuarioLogado = emailUsuarioLogado;
	}

}
