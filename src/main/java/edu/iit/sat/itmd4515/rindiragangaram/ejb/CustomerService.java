/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.iit.sat.itmd4515.rindiragangaram.ejb;

import edu.iit.sat.itmd4515.rindiragangaram.model.Customer;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author Ravi Kumar Hazare
 */
@Stateless
public class CustomerService extends BaseService<Customer> {

    public CustomerService() {
        super(Customer.class);
    }

    @Override
    public List<Customer> findAll() {

        return getEntityManager().createNamedQuery("Customer.findAll", Customer.class).getResultList();
    }

    public List<Customer> findAllNotDisabled(boolean disabled) {
        return getEntityManager().createNamedQuery("Customer.findAllNotDisabled", Customer.class).setParameter("disabled", disabled).getResultList();
    }

    public Customer findByUsername(String username, boolean disabled) {
        return getEntityManager()
                .createNamedQuery("Customer.findByUsername", Customer.class)
                .setParameter("username", username).setParameter("disabled", disabled)
                .getSingleResult();
    }

    public Customer findByUsernameOnly(String userName) {
        return getEntityManager().createNamedQuery("Customer.findByUsernameOnly", Customer.class).setParameter("username", userName).getSingleResult();
    }
}
