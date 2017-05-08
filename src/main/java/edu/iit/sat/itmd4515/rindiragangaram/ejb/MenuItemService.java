/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.iit.sat.itmd4515.rindiragangaram.ejb;

import edu.iit.sat.itmd4515.rindiragangaram.model.MenuItem;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author Ravi Kumar Hazare
 */
@Stateless
public class MenuItemService extends BaseService<MenuItem>{

    public MenuItemService() {
        super(MenuItem.class);
    }

    @Override
    public List<MenuItem> findAll() {
        return getEntityManager().createNamedQuery("MenuItem.findAll", MenuItem.class).getResultList();
    }
    
    public MenuItem findByItemName(String itemname){
        
        return getEntityManager()
                .createNamedQuery("MenuItem.findByName", MenuItem.class)
                .setParameter("itemname", itemname)
                .getSingleResult();
        
    }
    
}
