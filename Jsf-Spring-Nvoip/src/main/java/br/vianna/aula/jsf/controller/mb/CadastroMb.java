/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.vianna.aula.jsf.controller.mb;

import br.vianna.aula.jsf.model.dao.impl.ClienteDao;
import br.vianna.aula.jsf.model.domain.Cliente;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author thiago
 */
@Component(value = "cadastro")
@RequestScoped
public class CadastroMb {
    
    Cliente cliente;
    
    @Autowired
    ClienteDao cDao;

    public CadastroMb() {
        this.cliente = new Cliente();
        
    }
    
    @PostConstruct
    public void iniciar() {

    }

    @PreDestroy
    public void destruir() {

    }
    
    public String callViewCadastro(){
        return "cadastro?faces-redirect=true";
    }
    
    
    @Transactional
    public String cadastrarCliente(){
        
        FacesContext ct = FacesContext.getCurrentInstance();
        
        if (validaCamposEssenciais()) {
            ct.addMessage(null, new FacesMessage("Campos essenciais vazios!", ""));
            return "";
        }
        
        if (cliente.getCpf().length() != 11) {
            ct.addMessage(null, new FacesMessage("CPF inválido!", ""));
            return "";
        }
        
        if (!cliente.getEmail().contains("@")) {
            ct.addMessage(null, new FacesMessage("Email inválido!", ""));
            return "";
        }
        
        if (cliente.getTelefone().length() != 13) {
            ct.addMessage(null, new FacesMessage("Telefone inválido! Passe o código do país, código de área e o número", ""));
            return "";
        }
        
        try {
            cDao.inserir(cliente);
        } catch (Exception e) {
            ct.addMessage(null, new FacesMessage("Erro ao Salvar", "Erro :: " + e.getMessage()));
            return "";
        }
        
        this.cliente = new Cliente();
        ct.addMessage(null, new FacesMessage("Cliente cadastrado com sucesso!", ""));
        return "";
    }
    
    private boolean validaCamposEssenciais(){
        return this.cliente.getNome().isEmpty() &&
               this.cliente.getSobrenome().isEmpty() &&
               this.cliente.getEmail().isEmpty() &&
               this.cliente.getCep().isEmpty() &&
               this.cliente.getCpf().isEmpty() &&
               this.cliente.getTelefone().isEmpty();                
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public ClienteDao getcDao() {
        return cDao;
    }

    public void setcDao(ClienteDao cDao) {
        this.cDao = cDao;
    }
    
    
    
}
