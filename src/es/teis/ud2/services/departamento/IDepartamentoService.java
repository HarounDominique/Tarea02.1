/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package es.teis.ud2.services.departamento;

import es.teis.ud2.exceptions.InstanceNotFoundException;
import es.teis.ud2.model.Departamento;
import java.util.ArrayList;

/**
 *
 * @author maria
 */
public interface IDepartamentoService {
    
    public ArrayList<Departamento> findAll();
    public Departamento findById(int id) throws InstanceNotFoundException;    
    public ArrayList<String> getDepartamentNamesByLoc(String ubicacion);
}
