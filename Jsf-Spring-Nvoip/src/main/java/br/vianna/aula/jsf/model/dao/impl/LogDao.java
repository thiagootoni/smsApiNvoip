/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.vianna.aula.jsf.model.dao.impl;

import br.vianna.aula.jsf.model.dao.IGenericsDao;
import br.vianna.aula.jsf.model.domain.Log;
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
public class LogDao implements IGenericsDao<Log, Integer>{
    
    @Autowired
    EntityManager em;

    @Override
    public Log inserir(Log obj) throws SQLException {
        em.persist(obj);
        return obj;
    }

    @Override
    public Log editar(Log obj) throws SQLException {
        em.merge(obj);
        return obj;
    }

    @Override
    public void apagar(Log obj) throws SQLException {
        obj = em.find(Log.class, obj);
        em.remove(obj);
    }

    @Override
    public Log buscarUm(Integer key) throws SQLException {
        Query q = em.createQuery("select l form Log l where l.id =: id");
        
        q.setParameter("id", key);
        try{
            return (Log) q.getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }

    @Override
    public List<Log> buscarTodos() throws SQLException {
        Query q = em.createQuery("select l form Log l");
        return q.getResultList();
    }
    
}
