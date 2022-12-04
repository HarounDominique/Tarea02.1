package es.teis.ud2.model.dao.accountMovement;

import es.teis.ud2.exceptions.InstanceNotFoundException;
import es.teis.ud2.model.AccountMovement;
import es.teis.ud2.model.Departamento;
import es.teis.ud2.model.dao.IGenericDao;

public interface IAccountMovementDao extends IGenericDao<AccountMovement> {
    @Override
    public AccountMovement create(AccountMovement movimiento);
    @Override
    public AccountMovement read(int id)  throws InstanceNotFoundException;
    @Override
    public boolean update(AccountMovement movimiento);
    @Override
    public boolean delete(int id);
}
