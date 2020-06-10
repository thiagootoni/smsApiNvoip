/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.vianna.aula.jsf.model.dao.impl;

import br.vianna.aula.jsf.model.dao.IGenericsDao;
import br.vianna.aula.jsf.model.domain.Cliente;
import java.sql.SQLException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author thiago
 */
@Component
public class ClienteDao implements IGenericsDao<Cliente, Integer>{

    @Autowired
    EntityManager em;
    
    @Override
    public Cliente inserir(Cliente obj) throws SQLException {
        em.persist(obj);
        return obj;
    }

    @Override
    public Cliente editar(Cliente obj) throws SQLException {
        em.merge(obj);
        return obj;
    }

    @Override
    public void apagar(Cliente obj) throws SQLException {
        //obj = em.find(Cliente.class, obj);
        Cliente c = this.buscarUm(obj.getId());
        em.remove(c);
    }

    @Override
    public Cliente buscarUm(Integer key) throws SQLException {
        Query q = em.createQuery("select c from Cliente c where c.id =: id");
        
        q.setParameter("id", key);
        try{
            return (Cliente) q.getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }

    @Override
    public List<Cliente> buscarTodos() throws SQLException {
        Query q = em.createQuery("select c from Cliente c");
        return q.getResultList();
    }
    
    public Cliente buscarPorCpf(String cpf){
        Query q = em.createQuery("select c from Cliente c where c.cpf =: cpf");
        
        q.setParameter("cpf", cpf);
        try{
            return (Cliente) q.getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }
    
    public Cliente buscarPorTelefone(String telefone){
        Query q = em.createQuery("select c from Cliente c where c.telefone =: telefone");
        
        q.setParameter("telefone", telefone);
        try{
            return (Cliente) q.getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }
    
    public Cliente buscarPorEmail(String email){
        Query q = em.createQuery("select c from Cliente c where c.email =: email");
        
        q.setParameter("email", email);
        try{
            return (Cliente) q.getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }
}
