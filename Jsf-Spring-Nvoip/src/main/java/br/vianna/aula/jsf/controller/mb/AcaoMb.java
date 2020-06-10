/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.vianna.aula.jsf.controller.mb;

import br.vianna.aula.jsf.model.dao.impl.ClienteDao;
import br.vianna.aula.jsf.model.dao.impl.LogDao;
import br.vianna.aula.jsf.model.domain.Cliente;
import br.vianna.aula.jsf.model.domain.Log;
import br.vianna.aula.jsf.model.domain.api.sms.RespostaApiSms;
import br.vianna.aula.jsf.model.domain.dto.ClienteSmsDto;
import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author thiago
 */
@Component(value = "acao")
@ViewScoped
public class AcaoMb implements Serializable {

    private Cliente cliente;
    private String cpfEmailOuTelefone;
    private EStatusPagina statusPagina = EStatusPagina.BUSCA;

    @Autowired
    ClienteDao cDao;
    
    @Autowired
    LogDao lDao;

    public AcaoMb() {
        this.cliente = new Cliente();
    }

    public String callViewAcao() {
        return "acao?faces-redirect=true";
    }

    public String buscaCliente() {

        FacesContext ct = FacesContext.getCurrentInstance();

        if (this.cpfEmailOuTelefone.length() == 11) {
            this.cliente = cDao.buscarPorCpf(cpfEmailOuTelefone);
        } else if (this.cpfEmailOuTelefone.length() == 13) {
            this.cliente = cDao.buscarPorTelefone(cpfEmailOuTelefone);
        } else if (this.cpfEmailOuTelefone.contains("@")) {
            this.cliente = cDao.buscarPorEmail(cpfEmailOuTelefone);
        } else {
            ct.addMessage(null, new FacesMessage("Dados incorretos! Para cpf e telefone, coloque apenas números e para o e-mail, verifique se não faltou o @!", ""));
            return "";
        }

        if (cliente == null) {
            ct.addMessage(null, new FacesMessage("Nada encontrado para estes parâmetros!", ""));
            return "";
        } else {
            this.statusPagina = EStatusPagina.ACAO;
            return "";
        }
    }

    public boolean isStatusAcao() {
        return this.statusPagina.equals(EStatusPagina.ACAO);
    }

    @Transactional
    public String alterarCliente() {

        FacesContext ct = FacesContext.getCurrentInstance();
        
        if (validaCamposEssenciais()) {
            ct.addMessage(null, new FacesMessage("Campos essenciais vazios!", ""));
            this.statusPagina = EStatusPagina.ACAO;
            return "";
        }
        
        if (cliente.getCpf().length() != 11) {
            ct.addMessage(null, new FacesMessage("CPF inválido!", ""));
            this.statusPagina = EStatusPagina.ACAO;
            return "";
        }
        
        if (!cliente.getEmail().contains("@")) {
            ct.addMessage(null, new FacesMessage("Email inválido!", ""));
            this.statusPagina = EStatusPagina.ACAO;
            return "";
        }
        
        if (cliente.getTelefone().length() != 13) {
            ct.addMessage(null, new FacesMessage("Telefone inválido! Passe o código do país, código de área e o número", ""));
            this.statusPagina = EStatusPagina.ACAO;
            return "";
        }

        try {
            cliente = cDao.editar(cliente);
            ct.addMessage(null, new FacesMessage("Cliente alterado com sucesso!", ""));
        } catch (SQLException ex) {
            ct.addMessage(null, new FacesMessage("Erro ao alterar", "Erro :: " + ex.getMessage()));
            this.statusPagina = EStatusPagina.ACAO;
        }

        return "";
    }

    @Transactional
    public String apagarCliente() {
        FacesContext ct = FacesContext.getCurrentInstance();

        try {
            cDao.apagar(cliente);
        } catch (SQLException ex) {
            ct.addMessage(null, new FacesMessage("Erro ao Deletar", "Erro :: " + ex.getMessage()));
            return "";
        }

        ct.addMessage(null, new FacesMessage("Cliente apagado com sucesso!", ""));
        this.cliente = new Cliente();
        this.statusPagina = EStatusPagina.BUSCA;
        return "";

    }
    
    @Transactional
    public String enviarSms() {
        FacesContext ct = FacesContext.getCurrentInstance();

        RestTemplate rest = new RestTemplate();

        String url = "https://api.nvoip.com.br/v1/sms";
        String token = "211718131b8d420b13943298bbfb09b71ddd1";
        String msg = "teste api sms";
        String telefone = cliente.getTelefone();
        String telefone2 = cliente.getTelefone().substring(2);

//        HttpEntity<ClienteSmsDto> request = new HttpEntity<>(new ClienteSmsDto(telefone2, msg));
//        request.getHeaders().add("Content-Type", "application/json");
//        request.getHeaders().add("token_auth", token);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Content-Type", "application/json");        
        headers.set("token_auth", token);        
        HttpEntity<ClienteSmsDto> entity = new HttpEntity<ClienteSmsDto>(new ClienteSmsDto(telefone2, msg), headers);

        ResponseEntity<RespostaApiSms> resp = rest.exchange(url, HttpMethod.POST, entity, RespostaApiSms.class);

        if (resp.getStatusCodeValue() >= 200 && resp.getStatusCodeValue() <= 300) {
            
            ct.addMessage(null, new FacesMessage("Sms disparado com sucesso!", ""));
            
            Log log = new Log(0, cliente, LocalDateTime.now(), "sms", Integer.toString(resp.getStatusCodeValue()));
            
            try {
                lDao.inserir(log);
            } catch (SQLException ex) {
                Logger.getLogger(AcaoMb.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } else {
            ct.addMessage(null, new FacesMessage("Falha no envio do sms!", ""));
        }

        this.statusPagina = EStatusPagina.ACAO;
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

    public String getCpfEmailOuTelefone() {
        return cpfEmailOuTelefone;
    }

    public void setCpfEmailOuTelefone(String cpfEmailOuTelefone) {
        this.cpfEmailOuTelefone = cpfEmailOuTelefone;
    }

    public EStatusPagina getStatusPagina() {
        return statusPagina;
    }

    public void setStatusPagina(EStatusPagina statusPagina) {
        this.statusPagina = statusPagina;
    }

    public ClienteDao getcDao() {
        return cDao;
    }

    public void setcDao(ClienteDao cDao) {
        this.cDao = cDao;
    }

}
