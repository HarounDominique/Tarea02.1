/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package es.teis.ud2.model.dao.departamento;

import es.teis.ud2.model.dao.IGenericDao;
import es.teis.ud2.exceptions.InstanceNotFoundException;
import es.teis.ud2.model.Departamento;
import java.util.ArrayList;


public interface IDepartamentoDao extends IGenericDao<Departamento> {
    
    @Override
    public Departamento create(Departamento departamento);
    
    @Override
    public Departamento read(int id)  throws InstanceNotFoundException;
    
    @Override
    public boolean update(Departamento departamento);
    
    
    
    @Override
    public boolean delete(int id);
    
    
    public ArrayList<Departamento> findAll();
    
    public ArrayList<String> getDepartamentNamesByLoc(String ubicacion);
    
}
