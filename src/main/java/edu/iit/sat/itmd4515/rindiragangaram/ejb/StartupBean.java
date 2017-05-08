/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.iit.sat.itmd4515.rindiragangaram.ejb;

import edu.iit.sat.itmd4515.rindiragangaram.model.security.Group;
import edu.iit.sat.itmd4515.rindiragangaram.model.security.User;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author Ravi Kumar Hazare
 */
@Startup
@Singleton
public class StartupBean {

    // To make use of the entity manager to persist and other services 
    @EJB
    private GroupService groupService;

    // To make use of the entity manager to persist and other services
    @EJB
    private UserService userService;

    // Default constructor
    public StartupBean() {
    }
    
    @PostConstruct
    public void postConstruct() {

        // create a group called "ADMINISTRATOR"
        Group adminGroup = new Group("ADMINISTRATORS", "The admin group");
        // create a user called "admin"
        User adminUser = new User("admin", "admin");
        // add the admin user to admin group 
        adminUser.addGroup(adminGroup);
        
        groupService.create(adminGroup);
        userService.create(adminUser);
    }
    
}
