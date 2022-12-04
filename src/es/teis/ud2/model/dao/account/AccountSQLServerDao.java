/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.teis.ud2.model.dao.account;

import es.teis.ud2.data.DBCPDataSourceFactory;
import es.teis.ud2.exceptions.InstanceNotFoundException;
import es.teis.ud2.model.Account;
import es.teis.ud2.model.Empleado;
import es.teis.ud2.model.dao.AbstractGenericDao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;

/**
 * @author maria
 */
public class AccountSQLServerDao extends AbstractGenericDao<Account> implements IAccountDao {

    private DataSource dataSource;

    public AccountSQLServerDao() {
        this.dataSource = DBCPDataSourceFactory.getDataSource();
    }

    @Override
    public Account create(Account entity) {

        try (
                Connection conexion = this.dataSource.getConnection(); PreparedStatement pstmt = conexion.prepareStatement(
                "INSERT INTO [dbo].[ACCOUNT]\n"
                        + "           ([EMPNO]\n"
                        + "           ,[AMOUNT])\n"
                        + "     VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS
        );) {

            pstmt.setInt(1, entity.getEmpleado().getEmpleadoId());
            pstmt.setBigDecimal(2, entity.getMontante());

            // Devolverá 0 para las sentencias SQL que no devuelven nada o el número de filas afectadas
            int result = pstmt.executeUpdate();

            ResultSet clavesResultado = pstmt.getGeneratedKeys();

            if (clavesResultado.next()) {
                int accountId = clavesResultado.getInt(1);
                entity.setAccountId(accountId);
            } else {
                entity = null;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("Ha ocurrido una excepción: " + ex.getMessage());
            entity = null;
        }
        return entity;
    }

    @Override
    public Account read(int id) throws InstanceNotFoundException {

        int accountNo;
        int empno;
        BigDecimal amount;
        int contador;
        Account cuenta = null;

        try (
                Connection conexion = this.dataSource.getConnection(); PreparedStatement sentencia
                = conexion.prepareStatement("SELECT  [ACCOUNTNO]\n"
                + "      ,[EMPNO]\n"
                + "      ,[AMOUNT]\n"
                + "  FROM [empresa].[dbo].[ACCOUNT]"
                + "WHERE ACCOUNTNO=?");) {
            sentencia.setInt(1, id);

            ResultSet result = sentencia.executeQuery();
            if (result.next()) {
                contador = 0;

                accountNo = result.getInt(++contador);
                empno = result.getInt(++contador);
                amount = result.getBigDecimal(++contador);

                Empleado empleado = new Empleado();
                empleado.setEmpleadoId(empno);
                cuenta = new Account(accountNo, empleado, amount);

            } else {
                throw new InstanceNotFoundException(id, getEntityClass());
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("Ha ocurrido una excepción: " + ex.getMessage());

        }
        return cuenta;
    }

    @Override
    public boolean update(Account entity) {
        boolean actualizado = false;
        //no vamos a actualizar el empledo
        try (
                Connection conexion = this.dataSource.getConnection(); PreparedStatement pstmt = conexion.prepareStatement(
                "UPDATE [dbo].[ACCOUNT]\n"
                        + "   SET [AMOUNT] = ? \n"
                        + " WHERE ACCOUNTNO = ?")) {

            pstmt.setBigDecimal(1, entity.getMontante());
            pstmt.setInt(2, entity.getAccountId());

            int result = pstmt.executeUpdate();
            actualizado = (result == 1);

            // Devolverá 0 para las sentencias SQL que no devuelven nada o el número de filas afectadas
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("Ha ocurrido una excepción: " + ex.getMessage());

        }
        return actualizado;
    }

    @Override
    public boolean delete(int id) {
        int result = 0;
        try (
                Connection conexion = this.dataSource.getConnection(); PreparedStatement pstmt = conexion.prepareStatement("DELETE FROM ACCOUNT WHERE ACCOUNTNO=?");) {

            pstmt.setInt(1, id);

            result = pstmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("Ha ocurrido una excepción: " + ex.getMessage());

        }
        return (result == 1);
    }

    @Override
    public int transferir(int accIdOrigen, int accIdDestino, BigDecimal amount) {

        Connection con = null;
        boolean exito = false;
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
                         + "           (?, ?, ?, GETDATE())", movId = Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement selectMov = con.prepareStatement("SELECT \n"
                         + "      ,[ACCOUNT_ORIGIN_ID]\n"
                         + "      ,[ACCOUNT_DEST_ID]\n"
                         + "      ,[AMOUNT]\n"
                         + "      ,[DATETIME]\n"
                         + "  FROM [dbo].[ACC_MOVEMENT]\n"
                         + " [ACCOUNT_MOV_ID]=?", Statement.RETURN_GENERATED_KEYS);) {
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
                con.rollback();
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

}
