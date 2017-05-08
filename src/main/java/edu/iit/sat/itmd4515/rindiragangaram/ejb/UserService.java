/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.iit.sat.itmd4515.rindiragangaram.ejb;

import edu.iit.sat.itmd4515.rindiragangaram.model.security.User;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author Ravi Kumar Hazare
 */
@Stateless
public class UserService extends BaseService<User> {

    public UserService() {
        super(User.class);
    }

    @Override
    public List<User> findAll() {
        return getEntityManager().createNamedQuery("User.findAll", User.class).getResultList();
    }

    public User findByUsername(String userName, boolean disabled) {
        return getEntityManager().createNamedQuery("User.findByUsername", User.class)
                .setParameter("username", userName)
                .setParameter("disabled", disabled)
                .getSingleResult();
    }

    public User findByUsernameWithoutDisableField(String userName) {
        return getEntityManager().createNamedQuery("User.findByUsernameWithoutDisableField", User.class)
                .setParameter("username", userName)
                .getSingleResult();
    }
}
