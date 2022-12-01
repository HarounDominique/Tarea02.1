/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.teis.ud2.services.departamento;

import es.teis.ud2.exceptions.InstanceNotFoundException;
import es.teis.ud2.model.dao.departamento.IDepartamentoDao;
import es.teis.ud2.model.Departamento;
import java.util.ArrayList;

/**
 *
 * @author maria
 */
public class DepartamentoService implements IDepartamentoService{

    private IDepartamentoDao departamentoDao;

    public DepartamentoService(IDepartamentoDao departamentoDao) {
        this.departamentoDao = departamentoDao;
    }
    
    
    
    @Override
    public ArrayList<Departamento> findAll() {
     return departamentoDao.findAll();
    }
      @Override
     public Departamento findById(int id) throws InstanceNotFoundException{
         return this.departamentoDao.read(id);
     }

    @Override
    public ArrayList<String> getDepartamentNamesByLoc(String ubicacion) {
     return this.departamentoDao.getDepartamentNamesByLoc(ubicacion);
    }
    
}
