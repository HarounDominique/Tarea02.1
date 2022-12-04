/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.teis.ud2;

import es.teis.ud2.model.AccountMovement;
import es.teis.ud2.model.dao.account.AccountSQLServerDao;

import java.math.BigDecimal;

/**
 *
 * @author maria
 */
public class Main {

    public static void main(String[] args) {

        AccountMovement accMovement = transferirDineroEntreEmpleados(7369, 7499, new BigDecimal(1500));
        if (accMovement != null) {
            System.out.println("Se ha creado el registro: " + accMovement);
        }
    }


    private static AccountMovement transferirDineroEntreEmpleados(int empnoOrigen, int empnoDestino, BigDecimal cantidad) {
        AccountMovement accMovement = null;
        //Completa para crear el servicio  y llamar a su método  transferir(int empnoOrigen, int empnoDestino, BigDecimal cantidad)
        AccountSQLServerDao accountDao = new AccountSQLServerDao();
        accountDao.transferir(empnoOrigen, empnoDestino, cantidad);
        return accMovement;
    }
}
