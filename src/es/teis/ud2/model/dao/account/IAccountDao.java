/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package es.teis.ud2.model.dao.account;

import es.teis.ud2.model.Account;
import es.teis.ud2.model.dao.IGenericDao;
import java.math.BigDecimal;

/**
 *
 * @author mfernandez
 */
public interface IAccountDao extends IGenericDao<Account> {

     boolean transferir(int accIdOrigen, int accIdDestino, BigDecimal amount);
     
     
}
