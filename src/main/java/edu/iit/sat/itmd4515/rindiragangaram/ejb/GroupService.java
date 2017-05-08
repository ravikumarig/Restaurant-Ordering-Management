/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.iit.sat.itmd4515.rindiragangaram.ejb;

import edu.iit.sat.itmd4515.rindiragangaram.model.security.Group;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author Ravi Kumar Hazare
 */
@Stateless
public class GroupService extends BaseService<Group> {

    public GroupService() {
        super(Group.class);
    }

    @Override
    public List findAll() {
        return getEntityManager().createNamedQuery("Group.findAll", Group.class).getResultList();
    }
    
}
