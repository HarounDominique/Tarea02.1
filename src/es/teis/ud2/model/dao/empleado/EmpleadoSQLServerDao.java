/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.teis.ud2.model.dao.empleado;

import es.teis.ud2.data.DBCPDataSourceFactory;
import es.teis.ud2.exceptions.InstanceNotFoundException;
import es.teis.ud2.model.Departamento;
import es.teis.ud2.model.Empleado;
import es.teis.ud2.model.dao.AbstractGenericDao;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import javax.sql.DataSource;

/**
 *
 * @author mfernandez
 */
public class EmpleadoSQLServerDao extends AbstractGenericDao<Empleado>
        implements IEmpleadoDao {

    private DataSource dataSource;

    public EmpleadoSQLServerDao() {
        this.dataSource = DBCPDataSourceFactory.getDataSource();
    }

    @Override
    public Empleado create(Empleado entity) {
        try (
                 Connection conexion = this.dataSource.getConnection();  PreparedStatement pstmt = conexion.prepareStatement(
                "INSERT INTO [dbo].[EMP]\n"
                + "           ([ENAME]\n"
                + "           ,[JOB]\n"
                + "           ,[MGR]\n"
                + "           ,[HIREDATE]\n"
                + "           ,[SAL]\n"
                + "           ,[COMM]\n"
                + "           ,[DEPTNO])\n"
                + "     VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS
        );) {

            pstmt.setString(1, entity.getNombre());
            pstmt.setString(2, entity.getPuesto());
            pstmt.setInt(3, entity.getJefe().getEmpleadoId());
            pstmt.setDate(4, Date.valueOf(entity.getFechaContratacion()));
            pstmt.setBigDecimal(5, entity.getSalario());
            pstmt.setBigDecimal(6, entity.getComision());
            pstmt.setInt(7, entity.getDepartamento().getDeptno());

            // Devolverá 0 para las sentencias SQL que no devuelven nada o el número de filas afectadas
            int result = pstmt.executeUpdate();

            ResultSet clavesResultado = pstmt.getGeneratedKeys();

            if (clavesResultado.next()) {
                int empleadoId = clavesResultado.getInt(1);
                entity.setEmpleadoId(empleadoId);
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
    public Empleado read(int id) throws InstanceNotFoundException {
        String nombreEmpleado;
        String job;
        int empno, deptno;
        int contador;
        int jefeId;
        LocalDate fechaContratacion;
        BigDecimal salario, comision;
        Empleado empleado = null;
        Empleado jefe = null;
        Departamento departamento = null;

        try (
                 Connection conexion = this.dataSource.getConnection(); 
                PreparedStatement sentencia
                = conexion.prepareStatement("SELECT [EMPNO]\n"
                        + "      ,[ENAME]\n"
                        + "      ,[JOB]\n"
                        + "      ,[MGR]\n"
                        + "      ,[HIREDATE]\n"
                        + "      ,[SAL]\n"
                        + "      ,[COMM]\n"
                        + "      ,[DEPTNO]\n"
                        + "  FROM [empresa].[dbo].[EMP]\n"
                        + " WHERE EMPNO = ?");) {
            sentencia.setInt(1, id);

            ResultSet result = sentencia.executeQuery();
            if (result.next()) {
                contador = 0;

                empno = result.getInt(++contador);
                nombreEmpleado = result.getString(++contador);
                job = result.getString(++contador);
                jefeId = result.getInt(++contador);
                fechaContratacion = result.getDate(++contador).toLocalDate();
                salario = result.getBigDecimal(++contador);
                comision = result.getBigDecimal(++contador);
                deptno = result.getInt(++contador);

                jefe = new Empleado();
                jefe.setEmpleadoId(jefeId);

                departamento = new Departamento();
                departamento.setDeptno(deptno);

                empleado = new Empleado(nombreEmpleado, job,
                        jefe, fechaContratacion, salario,
                        comision, departamento);
                empleado.setEmpleadoId(empno);

            } else {
                throw new InstanceNotFoundException(id,
                        getEntityClass());
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("Ha ocurrido una excepción: " + ex.getMessage());

        }
        return empleado;
    }

    @Override
    public boolean update(Empleado entity) {
        int contador = 0;
         int filasAfectadas =0;
        try (
                 Connection conexion = this.dataSource.getConnection();
                PreparedStatement pstmt = 
                        conexion.prepareStatement("UPDATE [dbo].[EMP]\n"
                + "   SET [ENAME] = ? \n"
                + "      ,[JOB] = ? \n"
                + "      ,[MGR] = ? \n"
                + "      ,[HIREDATE] = ? \n"
                + "      ,[SAL] =  ? \n"
                + "      ,[COMM] = ? \n"
                + "      ,[DEPTNO] =? \n"
                + " WHERE EMPNO = ? ");) {

            pstmt.setString(++contador, entity.getNombre());
            pstmt.setString(++contador, entity.getPuesto());
            pstmt.setInt(++contador, entity.getJefe().getEmpleadoId());
            pstmt.setDate(++contador, Date.valueOf(entity.getFechaContratacion()));
            pstmt.setBigDecimal(++contador, entity.getSalario());
            pstmt.setBigDecimal(++contador, entity.getComision());
            pstmt.setInt(++contador, entity.getDepartamento().getDeptno());
            pstmt.setInt(++contador, entity.getEmpleadoId());
            
            filasAfectadas= pstmt.executeUpdate();
            

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("Ha ocurrido una excepción: " + ex.getMessage());

        }

        return (filasAfectadas==1);

    }

    @Override
    public boolean delete(int id) {
        int result = 0;
        try (
                 Connection conexion = this.dataSource.getConnection();
                PreparedStatement pstmt = 
                        conexion.prepareStatement("DELETE FROM EMP WHERE EMPNO=?");) {

            pstmt.setInt(1, id);

            result = pstmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("Ha ocurrido una excepción: " + ex.getMessage());

        }
        return (result == 1);
    }

}
