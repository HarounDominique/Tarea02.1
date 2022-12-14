package es.teis.ud2.model.dao.accountMovement;

import es.teis.ud2.exceptions.InstanceNotFoundException;
import es.teis.ud2.model.Account;
import es.teis.ud2.model.AccountMovement;
import es.teis.ud2.model.Empleado;
import es.teis.ud2.model.dao.AbstractGenericDao;
import es.teis.ud2.model.dao.account.IAccountDao;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;

public class AccountMovementSQLServerDao extends AbstractGenericDao<AccountMovement> implements IAccountMovementDao {

    private DataSource dataSource;

    public AccountMovement create(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public AccountMovement create(AccountMovement entity) {
        return null;
    }

    public AccountMovement read(int id) throws InstanceNotFoundException {

        int accountMovementId;
        int cuentaOrigenId;
        int cuentaDestinoId;
        BigDecimal montante;
        Timestamp fechaYHora;
        int contador;

        AccountMovement movimiento = null;

        try (
                Connection conexion = this.dataSource.getConnection();
                PreparedStatement sentencia = conexion.prepareStatement("SELECT  [ACCOUNT_MOV_ID]\n"
                + "      ,[ACCOUNT_DEST_ID]\n"
                + "      ,[ACCOUNT_ORIGIN_ID]\n"
                + "      ,[AMOUNT]\n"
                + "      ,[DATETIME]\n"
                + "  FROM [empresa].[dbo].[ACC_MOVEMENT]"
                + "WHERE ACCOUNT_MOV_ID=?");) {

            sentencia.setInt(1, id);

            ResultSet result = sentencia.executeQuery();
            if (result.next()) {
                contador = 0;

                accountMovementId = result.getInt(++contador);
                cuentaOrigenId = result.getInt(++contador);
                cuentaDestinoId = result.getInt(++contador);
                montante = result.getBigDecimal(++contador);
                fechaYHora = result.getTimestamp(++contador);

                Account cuentaOrigen = new Account();
                Account cuentaDestino = new Account();
                cuentaOrigen.setAccountId(cuentaOrigenId);
                cuentaDestino.setAccountId(cuentaDestinoId);
                movimiento = new AccountMovement(accountMovementId, cuentaOrigenId, cuentaDestinoId, montante, fechaYHora);

            } else {
                throw new InstanceNotFoundException(id, getEntityClass());
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("Ha ocurrido una excepci??n: " + ex.getMessage());

        }
        return movimiento;
    }

    @Override
    public boolean update(AccountMovement entity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean delete(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
