/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.vianna.aula.jsf.model.domain.dto;

/**
 *
 * @author thiago
 */
public class ClienteSmsDto {
    
    private String celular;
    private String msg;

    public ClienteSmsDto() {
    }

    public ClienteSmsDto(String celular, String msg) {
        this.celular = celular;
        this.msg = msg;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    
}
