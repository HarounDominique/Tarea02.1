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
        int movId = 0;
        try {
            con = this.dataSource.getConnection();

            try (PreparedStatement updateOrigen = con.prepareStatement("UPDATE [dbo].[ACCOUNT]\n"
                    + "   SET [AMOUNT] = (AMOUNT - ?) \n"
                    + " WHERE ACCOUNTNO = ?");
                 PreparedStatement updateDestino = con.prepareStatement("UPDATE [dbo].[ACCOUNT]\n"
                         + "   SET [AMOUNT] = (AMOUNT + ?) \n"
                         + " WHERE ACCOUNTNO = ?");
                 PreparedStatement insertMov = con.prepareStatement("INSERT INTO [dbo].[ACC_MOVEMENT]\n"
                         + "           ([ACCOUNT_ORIGIN_ID]\n"
                         + "           ,[ACCOUNT_DEST_ID]\n"
                         + "           ,[AMOUNT]\n"
                         + "           ,[DATETIME])\n"
                         + "     VALUES\n"
                         + "           (?, ?, ?, GETDATE())", movId = Statement.RETURN_GENERATED_KEYS);) {
                con.setAutoCommit(false);

                updateOrigen.setBigDecimal(1, amount);
                updateOrigen.setInt(2, accIdOrigen);
                updateOrigen.executeUpdate();

                updateDestino.setBigDecimal(1, amount);
                updateDestino.setInt(2, accIdDestino);
                updateDestino.executeUpdate();

                insertMov.setInt(1, accIdOrigen);
                insertMov.setInt(2, accIdDestino);
                insertMov.setBigDecimal(3, amount);

                insertMov.executeUpdate();

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
            return -1;
        } else {
            return movId;
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
