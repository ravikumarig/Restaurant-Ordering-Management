/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.iit.sat.itmd4515.rindiragangaram.web;

import edu.iit.sat.itmd4515.rindiragangaram.ejb.CustomerService;
import edu.iit.sat.itmd4515.rindiragangaram.ejb.GroupService;
import edu.iit.sat.itmd4515.rindiragangaram.ejb.UserService;
import edu.iit.sat.itmd4515.rindiragangaram.model.Customer;
import edu.iit.sat.itmd4515.rindiragangaram.model.security.Group;
import edu.iit.sat.itmd4515.rindiragangaram.model.security.User;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Named;

/**
 *
 * @author Ravi Kumar Hazare
 */
@Named
@RequestScoped
public class SignUpController extends BaseController {

    @EJB
    private GroupService groupService;
    @EJB
    private UserService userService;
    @EJB
    private CustomerService customerService;
    
    private static List<User> users = new ArrayList<>();

    private static final Logger LOG = Logger.getLogger(SignUpController.class.getName());

    private Customer customer;
    private User user;
    private Group group;

    public SignUpController() {
    }
    
    @Override
    @PostConstruct
    public void postConstruct() {
        super.postConstruct(); 
        LOG.info("SignUpController postConstruct");
        customer = new Customer();
        user = new User();
        group = new Group();
        users = userService.findAll();
    }

    // This method will perform a successful sign-up tasks and stores the data in database
    public String signUpCustomer(){

        for(User u : users){
            if(this.user.getUserName().equals(u.getUserName())){
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                        "This Username already exists. Please choose another username", 
                        "Username is already taken."));
                return "/signUp.xhtml";
            }
        }

        // Create a group called CUSTOMERS for new signup's
        group.setGroupName("CUSTOMERS");
        group.setGroupDescription("This group contains customers.");
        
        // Set user and group in sec_user_groups
        user.addGroup(group);
        
        // add the new user to the security_user table
        userService.create(user);
        
        // Set uname for customer object
        customer.setUser(user);
        
        customerService.create(customer);
        
        
        
              
        return "/login.xhtml";
    }

    /**
     * Get the value of customer
     *
     * @return the value of customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Set the value of customer
     *
     * @param customer new value of customer
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

}
