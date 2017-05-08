/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.iit.sat.itmd4515.rindiragangaram.web;

import edu.iit.sat.itmd4515.rindiragangaram.ejb.CustomerService;
import edu.iit.sat.itmd4515.rindiragangaram.ejb.ManagerService;
import edu.iit.sat.itmd4515.rindiragangaram.ejb.UserService;
import edu.iit.sat.itmd4515.rindiragangaram.model.Customer;
import edu.iit.sat.itmd4515.rindiragangaram.model.Manager;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.validation.constraints.*;

/**
 *
 * @author Ravi Kumar Hazare
 */
@Named
@RequestScoped
public class LoginController extends BaseController {

    @EJB
    private ManagerService managerService;

    @EJB
    private CustomerService customerService;

    private static final Logger LOG = Logger.getLogger(LoginController.class.getName());
    @NotNull(message = "Username should not be empty")
    private String userName;
    @NotNull(message = "Password should not be empty")
    private String password;

    @Inject
    CustomerPortalController customerPortalController;

    /**
     * Default Constructor
     */
    public LoginController() {
    }

    //Utility methods 
    //vvvvvvvvvvvvvvvvvvvvv
    public String getRemoteUser() {

        return context.getExternalContext().getRemoteUser();
    }

    public boolean isCustomer() {

        return context.getExternalContext().isUserInRole("CUST_ROLE");
    }

    public boolean isManager() {

        return context.getExternalContext().isUserInRole("MANG_ROLE");
    }

    public boolean isManagerAndCustomer() {
        return context.getExternalContext().isUserInRole("MANG_ROLE") && context.getExternalContext().isUserInRole("CUST_ROLE");
    }

    public boolean isAdmin() {

        return context.getExternalContext().isUserInRole("ADMIN_ROLE");
    }

    //^^^^^^^^^^^^^^^^^^^^
    // Action methods 
    // vvvvvvvvvvvvvvvvvvvvv
    // method call for the initial login
    public String doLogin() {

        Customer c = new Customer();
        Manager m = new Manager();

        try {
            request.login(userName, password);

            if (isCustomer()) {
                c = customerService.findByUsernameOnly(userName);

                LOG.info(c.toString());
                if (c.isDisabled()) {

                    request.logout();
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Your account is disabled, Please contact the administrator.",
                            "Your account is disabled, Please contact the administrator."));
                    // Redirect to the login page again. 
                    return "/login.xhtml";
                }
            } else if(isManager()){
                m = managerService.findByUsername(userName);
                
                LOG.info(m.toString());
                if(m.isDisabled()){
                    
                    request.logout();
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Your account is disabled, Please contact the administrator.",
                            "Your account is disabled, Please contact the administrator."));
                    // Redirect to the login page again. 
                    return "/login.xhtml";
                    
                }
                
            }

        } catch (ServletException ex) {
            // Log the exception if login fails
            LOG.log(Level.SEVERE, null, ex);
            // Provide a readable error to the end users
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Login Failed ! Please verify your username and/or password.",
                    "Please refer the console for the full log of the error/exception"));
            // Redirect to the login page again. 
            return "/login.xhtml";
        }
        // Render the below page if login is successful
        return "/welcome.xhtml?faces-redirect=true";
    }

    // method call for the logout
    public String doLogout() {

        if (isCustomer()) {
            customerPortalController.emptyCart();
        }
        try {
            request.logout();
        } catch (ServletException ex) {
            // Logging the exception if logout fails.
            LOG.log(Level.SEVERE, null, ex);
            // Pass message to end users 
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Logout was not successful !",
                    "Oops !! Something went wrong, please log back in and logout."));
            return "/login.xhtml";
        }
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "You have successfully logged out !!",
                "Logout successful, close the browser or login back.."));
        return "/login.xhtml";
    }
    //^^^^^^^^^^^^^^^^^^^^^

    /**
     * Get the value of userName
     *
     * @return the value of userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Set the value of userName
     *
     * @param userName new value of userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

}
