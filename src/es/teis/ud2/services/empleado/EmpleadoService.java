/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.teis.ud2.services.empleado;

import es.teis.ud2.exceptions.InstanceNotFoundException;
import es.teis.ud2.exceptions.SaldoInsuficienteException;
import es.teis.ud2.model.Account;
import es.teis.ud2.model.AccountMovement;
import es.teis.ud2.model.Empleado;
import es.teis.ud2.model.dao.empleado.IEmpleadoDao;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;

/**
 * @author maria
 */
public class EmpleadoService implements IEmpleadoServicio {

    private DataSource dataSource;

    private IEmpleadoDao empleadoDao;

    public EmpleadoService(IEmpleadoDao empleadoDao) {
        this.empleadoDao = empleadoDao;

    }

    @Override
    public Empleado create(Empleado empleado) {

        return this.empleadoDao.create(empleado);
    }

    @Override
    public AccountMovement transferir(int empnoOrigen, int empnoDestino, BigDecimal cantidad)
            throws SaldoInsuficienteException, InstanceNotFoundException, UnsupportedOperationException {
        boolean exito = false;
        Connection con = null;
        BigDecimal montante = cantidad;
        java.sql.Timestamp fechaYHora = null;
        int movimientoId = 0;
        AccountMovement movimientoCuenta = null;
        int contador = 0;


        if (montante.longValue() <= 0) {
            throw new UnsupportedOperationException();
        }

        try {
            con = this.dataSource.getConnection();

            try (PreparedStatement updateOrigen = con.prepareStatement("UPDATE [dbo].[ACCOUNT]\n"
                    + "   SET [AMOUNT] = (AMOUNT - ?) \n"
                    + " WHERE EMPNO = ?");
                 PreparedStatement updateDestino = con.prepareStatement("UPDATE [dbo].[ACCOUNT]\n"
                         + "   SET [AMOUNT] = (AMOUNT + ?) \n"
                         + " WHERE EMPNO = ?");
                 PreparedStatement insertMov = con.prepareStatement("INSERT INTO [dbo].[ACC_MOVEMENT]\n"
                         + "           ([ACCOUNT_ORIGIN_ID]\n"
                         + "           ,[ACCOUNT_DEST_ID]\n"
                         + "           ,[AMOUNT]\n"
                         + "           ,[DATETIME])\n"
                         + "     VALUES\n"
                         + "           (?, ?, ?, GETDATE())", movimientoId = Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement selectMov = con.prepareStatement("SELECT [ACCOUNT_MOV_ID]\n" +
                         "      ,[ACCOUNT_ORIGIN_ID]\n" +
                         "      ,[ACCOUNT_DEST_ID]\n" +
                         "      ,[AMOUNT]\n" +
                         "      ,[DATETIME]\n" +
                         "  FROM [dbo].[ACC_MOVEMENT]")) {
                con.setAutoCommit(false);

                updateOrigen.setBigDecimal(1, cantidad);
                updateOrigen.setInt(2, empnoOrigen);
                updateOrigen.executeUpdate();

                updateDestino.setBigDecimal(1, cantidad);
                updateDestino.setInt(2, empnoDestino);
                updateDestino.executeUpdate();

                int sourceAccId = findAccountByEmployeeId(empnoOrigen);
                int destinationAccId = findAccountByEmployeeId(empnoDestino);


                insertMov.setInt(1, sourceAccId);
                insertMov.setInt(2, destinationAccId);
                insertMov.setBigDecimal(3, cantidad);

                insertMov.executeUpdate();

                selectMov.executeQuery();

                ResultSet result = selectMov.executeQuery();
                if (result.next()) {
                    contador = 0;

                    movimientoId = result.getInt(++contador);
                    sourceAccId = result.getInt(++contador);
                    destinationAccId = result.getInt(++contador);
                    montante = result.getBigDecimal(++contador);
                    fechaYHora = result.getTimestamp(++contador);
                    /*
                    Account cuentaOrigen = new Account();
                    Account cuentaDestino = new Account();
                    cuentaOrigen.setAccountId(cuentaOrigenId);
                    cuentaDestino.setAccountId(cuentaDestinoId);
                     */

                } else {
                    System.out.println("¯\\_(ツ)_/¯");
                }

                movimientoCuenta = new AccountMovement(movimientoId, sourceAccId, destinationAccId, montante, fechaYHora);

                con.commit();

                exito = true;

            } catch (SQLException ex) {
                ex.printStackTrace();
                System.err.println("Ha habido una excepción. Se realizará un rollback: " + ex.getMessage());

                //Para forzar una excepción durante rollback  con.setAutoCommit(true);              con.setAutoCommit(true);
                try {

                    con.rollback();
                } catch (SQLException exr) {
                    ex.printStackTrace();
                    System.err.println("Ha habido una excepción haciendo rollback: " + exr.getMessage());

                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("Ha habido una excepción obteniendo la conexión: " + ex.getMessage());

        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);

                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    System.err.println("Ha habido una excepción cerrando la conexión: " + ex.getMessage());
                }
            }
        }
        if (!exito) {
            return null;
        } else {
            return movimientoCuenta;
        }
    }

    public int findAccountByEmployeeId(int employeeId) {

        int cuentaId = 0;
        int empleadoId;
        BigDecimal montante;

        int contador;
        Connection con = null;
        try (PreparedStatement findAcc = con.prepareStatement("SELECT [ACCOUNTNO]\n" +
                "      ,[EMPNO]\n" +
                "      ,[AMOUNT]\n" +
                "  FROM [dbo].[ACCOUNT]"
                + "WHERE ACCOUNTNO=?")) {

            findAcc.setInt(1, employeeId);

            findAcc.executeUpdate();

            ResultSet result = findAcc.executeQuery();

            if (result.next()) {
                contador = 0;

                cuentaId = result.getInt(++contador);
                empleadoId = result.getInt(++contador);
                montante = result.getBigDecimal(++contador);


            } else {
                System.out.println("¯\\_(ツ)_/¯");
            }

            con.commit();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("Ha habido una excepción. Se realizará un rollback: " + ex.getMessage());

            try {

                con.rollback();
            } catch (SQLException exr) {
                ex.printStackTrace();
                System.err.println("Ha habido una excepción haciendo rollback: " + exr.getMessage());

            }
        }

        return cuentaId;
    }

}
