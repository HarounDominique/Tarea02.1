/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.teis.ud2.services.empleado;

import es.teis.ud2.model.Empleado;
import es.teis.ud2.model.dao.empleado.IEmpleadoDao;

/**
 *
 * @author maria
 */
public class EmpleadoService implements IEmpleadoServicio {

    private IEmpleadoDao empleadoDao;

    public EmpleadoService(IEmpleadoDao empleadoDao) {
        this.empleadoDao = empleadoDao;

    }

    @Override
    public Empleado create(Empleado empleado) {

        return this.empleadoDao.create(empleado);
    }

}
