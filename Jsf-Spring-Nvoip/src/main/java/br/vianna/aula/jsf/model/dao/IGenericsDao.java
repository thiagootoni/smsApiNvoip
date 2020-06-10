/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.vianna.aula.jsf.model.dao;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author thiago
 */
public interface IGenericsDao<C, K> {
    
    public C inserir(C obj)throws SQLException;
    public C editar(C obj)throws SQLException;
    public void apagar(C obj)throws SQLException;
    public C buscarUm(K key)throws SQLException;
    public List<C> buscarTodos()throws SQLException;
}
