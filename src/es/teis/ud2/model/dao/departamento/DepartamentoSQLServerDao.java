/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.teis.ud2.model.dao.departamento;

import es.teis.ud2.data.DBCPDataSourceFactory;
import es.teis.ud2.exceptions.InstanceNotFoundException;
import es.teis.ud2.model.Departamento;
import es.teis.ud2.model.dao.AbstractGenericDao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.sql.DataSource;

/**
 *
 * @author mfernandez
 */
public class DepartamentoSQLServerDao extends AbstractGenericDao<Departamento> implements IDepartamentoDao {

    private DataSource dataSource;

    public DepartamentoSQLServerDao() {
        this.dataSource = DBCPDataSourceFactory.getDataSource();
    }

    @Override
    public Departamento create(Departamento departamento) {

        try (
                 Connection conexion = this.dataSource.getConnection();  PreparedStatement pstmt = conexion.prepareStatement(
                "INSERT INTO [dbo].[DEPT]( DNAME,  LOC) VALUES( ?, ?);", Statement.RETURN_GENERATED_KEYS
        );) {

            pstmt.setString(1, departamento.getDeptName());
            pstmt.setString(2, departamento.getLoc());

            // Devolverá 0 para las sentencias SQL que no devuelven nada o el número de filas afectadas
            int result = pstmt.executeUpdate();

            ResultSet clavesResultado = pstmt.getGeneratedKeys();

            if (clavesResultado.next()) {
                int departamentoId = clavesResultado.getInt(1);
                departamento.setDeptno(departamentoId);
            } else {
                departamento = null;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("Ha ocurrido una excepción: " + ex.getMessage());
            departamento = null;
        }
        return departamento;
    }

    @Override
    public Departamento read(int id) throws InstanceNotFoundException {

        String nombreDept;
        String ubicacion;
        int deptNo;
        int contador;
        Departamento departamento = null;

        try (
                 Connection conexion = this.dataSource.getConnection();  PreparedStatement sentencia
                = conexion.prepareStatement("SELECT [DEPTNO]\n"
                        + "      ,[DNAME]\n"
                        + "      ,[LOC] FROM dbo.DEPT \n"
                        + "WHERE DEPTNO=?");) {
            sentencia.setInt(1, id);

            ResultSet result = sentencia.executeQuery();
            if (result.next()) {
                contador = 0;

                deptNo = result.getInt(++contador);
                nombreDept = result.getString(++contador);
                ubicacion = result.getString(++contador);

                departamento = new Departamento(deptNo,
                        nombreDept, ubicacion);

            } else {
                throw new InstanceNotFoundException(id, getEntityClass());
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("Ha ocurrido una excepción: " + ex.getMessage());

        }
        return departamento;
    }

    @Override
    public boolean update(Departamento departamento) {

        boolean actualizado = false;
        try (
                 Connection conexion = this.dataSource.getConnection();  PreparedStatement pstmt = conexion.prepareStatement(
                "UPDATE [dbo].[DEPT] "
                + " SET DNAME=?,  LOC=? WHERE DEPTNO = ?")) {

            pstmt.setString(1, departamento.getDeptName());
            pstmt.setString(2, departamento.getLoc());
            pstmt.setInt(3, departamento.getDeptno());

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
                 Connection conexion = this.dataSource.getConnection();  PreparedStatement pstmt = conexion.prepareStatement("DELETE FROM dept WHERE DEPTNO=?");) {

            pstmt.setInt(1, id);

            result = pstmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("Ha ocurrido una excepción: " + ex.getMessage());
         

        }
        return (result == 1);
    }

    @Override
    public ArrayList<Departamento> findAll() {
        String nombreDept;
        String ubicacion;
        int deptNo;
        int contador;
        Departamento departamento = null;
        ArrayList<Departamento> departamentos = new ArrayList<>();

        try (
                 Connection conexion = this.dataSource.getConnection();  PreparedStatement pstmt = conexion.prepareStatement("SELECT DEPTNO, DNAME, LOC "
                + "FROM DEPT ORDER BY DNAME");  ResultSet result = pstmt.executeQuery();) {

            while (result.next()) {
                contador = 0;

                deptNo = result.getInt(++contador);
                nombreDept = result.getString(++contador);
                ubicacion = result.getString(++contador);

                departamento = new Departamento(deptNo, ubicacion, ubicacion);

                departamentos.add(departamento);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("Ha ocurrido una excepción: " + ex.getMessage());

        }

        return departamentos;
    }

    @Override
    public ArrayList<String> getDepartamentNamesByLoc(String ubicacion) {
        String nombreDept;
        ArrayList<String> nombres = new ArrayList<>();

        try (
                 Connection conexion = this.dataSource.getConnection();  PreparedStatement pstmt = conexion.prepareStatement("SELECT DNAME "
                + "FROM DEPT WHERE LOC LIKE ? ORDER BY DNAME");) {

            pstmt.setString(1, ubicacion);
            ResultSet result = pstmt.executeQuery();

            while (result.next()) {

                nombreDept = result.getString(1);
                nombres.add(nombreDept);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("Ha ocurrido una excepción: " + ex.getMessage());

        }

        return nombres;
    }

}
