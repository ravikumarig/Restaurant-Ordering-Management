/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.iit.sat.itmd4515.rindiragangaram.ejb;

import edu.iit.sat.itmd4515.rindiragangaram.model.Manager;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author Ravi Kumar Hazare
 */
@Stateless
public class ManagerService extends BaseService<Manager>{

    public ManagerService() {
        super(Manager.class);
    }

    @Override
    public List<Manager> findAll() {
        return getEntityManager().createNamedQuery("Manager.findAll", Manager.class).getResultList();
    }
    
    public List<Manager> findAllNotDisabled(boolean disabled) {
        return getEntityManager().createNamedQuery("Manager.findAllNotDisabled", Manager.class)
                .setParameter("disabled", disabled)
                .getResultList();
    }
    
    public Manager findByUsername(String username){

        return getEntityManager()
                .createNamedQuery("Manager.findByUsername", Manager.class)
                .setParameter("username", username)
                .getSingleResult();
    }
    
    public Manager findByUsernameAndDisable(String username, boolean disabled){

        return getEntityManager()
                .createNamedQuery("Manager.findByUsernameAndDisable", Manager.class)
                .setParameter("username", username)
                .setParameter("disabled", disabled)
                .getSingleResult();
    }
    
    public Manager findByEmail(String email){

        return getEntityManager()
                .createNamedQuery("Manager.findByEmail", Manager.class)
                .setParameter("email", email)
                .getSingleResult();
    }
}
