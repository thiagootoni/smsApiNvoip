/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.vianna.aula.jsf.model.domain.api.sms;

/**
 *
 * @author thiago
 */
public class RespostaApiSms {
    
    private String state;
    private String message;

    public RespostaApiSms() {
    }

    public RespostaApiSms(String state, String message) {
        this.state = state;
        this.message = message;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}
