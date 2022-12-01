/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.teis.ud2.exceptions;

import java.math.BigDecimal;

/**
 *
 * @author maria
 */
public class SaldoInsuficienteException extends Exception {

    private BigDecimal saldoActual;
    private BigDecimal cantidad;

    public SaldoInsuficienteException(String string, BigDecimal saldoActual, BigDecimal cantidad) {
        super(String.format(string, saldoActual, cantidad));
        this.saldoActual = saldoActual;
        this.cantidad = cantidad;
    }

}
