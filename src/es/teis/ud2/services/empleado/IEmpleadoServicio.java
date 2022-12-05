/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package es.teis.ud2.services.empleado;

import es.teis.ud2.exceptions.InstanceNotFoundException;
import es.teis.ud2.exceptions.SaldoInsuficienteException;
import es.teis.ud2.model.AccountMovement;
import es.teis.ud2.model.Empleado;

import java.math.BigDecimal;

/**
 * @author maria
 */
public interface IEmpleadoServicio {

    public Empleado create(Empleado empleado);

    //    public Empleado read(int empId);
//    public boolean delete(int empId);
//    public boolean update(Empleado empleado);
    public AccountMovement transferir(int empnoOrigen, int empnoDestino, BigDecimal cantidad) throws SaldoInsuficienteException, InstanceNotFoundException, UnsupportedOperationException;

    public int findAccountByEmployeeId(int employeeId);

}
