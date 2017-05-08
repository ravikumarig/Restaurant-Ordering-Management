/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.iit.sat.itmd4515.rindiragangaram.web;

import edu.iit.sat.itmd4515.rindiragangaram.ejb.ChequeService;
import edu.iit.sat.itmd4515.rindiragangaram.ejb.CustomerService;
import edu.iit.sat.itmd4515.rindiragangaram.ejb.GroupService;
import edu.iit.sat.itmd4515.rindiragangaram.ejb.LineItemService;
import edu.iit.sat.itmd4515.rindiragangaram.ejb.ManagerService;
import edu.iit.sat.itmd4515.rindiragangaram.ejb.MenuItemService;
import edu.iit.sat.itmd4515.rindiragangaram.ejb.StartupBean;
import edu.iit.sat.itmd4515.rindiragangaram.ejb.UserService;
import edu.iit.sat.itmd4515.rindiragangaram.model.Customer;
import edu.iit.sat.itmd4515.rindiragangaram.model.LineItem;
import edu.iit.sat.itmd4515.rindiragangaram.model.Manager;
import edu.iit.sat.itmd4515.rindiragangaram.model.MenuItem;
import edu.iit.sat.itmd4515.rindiragangaram.model.security.Group;
import edu.iit.sat.itmd4515.rindiragangaram.model.security.User;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Ravi Kumar Hazare
 */
@Named
@RequestScoped
public class AdminPortalController extends BaseController {

    private static final Logger LOG = Logger.getLogger(AdminPortalController.class.getName());
    private static List<User> users = new ArrayList<>();

    @EJB
    private UserService userService;

    @EJB
    private StartupBean startupBean;

    @EJB
    private MenuItemService menuItemService;

    @EJB
    private ManagerService managerService;

    @EJB
    private LineItemService lineItemService;

    @EJB
    private GroupService groupService;

    @EJB
    private CustomerService customerService;

    @EJB
    private ChequeService chequeService;

    @Inject
    LoginController loginController;

    @Inject
    CustomerPortalController customerPortalController;

    @Inject
    SignUpController signUpController;

    private Customer customer;
    private Manager manager;
    private User user;
    private Group group;
    private MenuItem menuItem;
    private LineItem lineItem;
    List<LineItem> lineItems;

    @Override
    @PostConstruct
    public void postConstruct() {
        super.postConstruct();
        group = new Group();
        user = new User();
        lineItem = new LineItem();
        customer = new Customer();
        manager = new Manager();
        customer.setUser(user);
        manager.setUser(user);
        users = userService.findAll();
        menuItem = new MenuItem();
    }

    //********************************************Utility methods starts********************************* 
    //Returns all the customers in a form of list.
    public List<Customer> getAllCustomers() {
        return customerService.findAllNotDisabled(false);
    }

    //Returns all the managers in a form of list.
    public List<Manager> getAllManagers() {
        return managerService.findAllNotDisabled(false);
    }

    //Returns all the managers in a form of list.
    public List<MenuItem> getAllMenuItems() {
        return menuItemService.findAll();
    }

    //********************************************Utility methods ends*********************************
    //********************************************Action methods starts*********************************
    // This method will take you to the update customer page by passing the selected customer
    public String updateCustomer(Customer c) {

        this.customer = c;
        this.user = userService.findByUsername(customer.getUser().getUserName(), false);
        LOG.info(this.customer.toString());
        LOG.info(this.user.toString());

        return "/admin/updateCustomer.xhtml";
    }

    // The customer details are updated on clicking update button
    public String doUpdateCustomerProfileToDatabase() {

        LOG.info(this.customer.toString());
        LOG.info(this.user.toString());

        this.user = userService.findByUsername(customer.getUser().getUserName(), false);
        this.user.setPassword(this.customer.getUser().getPassword());
        LOG.log(Level.INFO, "AdminPortalController:updateCustomerProfileToDatabase: {0}", user.toString());
        this.customer.setUser(this.user);
        LOG.log(Level.INFO, "AdminPortalController:updateCustomerProfileToDatabase: {0}", customer.toString());

        userService.update(this.user);
        customerService.update(this.customer);

        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "The selected customer details are updated successfully.",
                "The selected customer details are updated successfully."));

        return "/admin/welcome.xhtml";
    }

    // The customer account is disabled on clicking delete button
    public String doDeleteCustomerProfileToDatabase() {

        LOG.info(this.customer.toString());
        LOG.info(this.user.toString());

        this.user = userService.findByUsername(customer.getUser().getUserName(), false);
        this.user.setPassword(this.customer.getUser().getPassword());
        LOG.log(Level.INFO, "AdminPortalController:updateCustomerProfileToDatabase: {0}", user.toString());
        this.customer.setUser(this.user);
        this.customer.setDisabled(true);
        LOG.log(Level.INFO, "AdminPortalController:updateCustomerProfileToDatabase: {0}", customer.toString());

        customerService.update(this.customer);
        userService.update(this.user);

        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "The selected customer is disabled.",
                "The selected customer is disabled ."));

        return "/admin/welcome.xhtml";
    }

    // Manager action methods
    // This method will take you to the update manager page by passing the selected manager
    public String manageManager(Manager m) {

        this.manager = m;

        this.user = userService.findByUsername(manager.getUser().getUserName(), false);
        LOG.info(this.manager.toString());
        LOG.info(this.user.toString());

        return "/admin/manageManager.xhtml";
    }

    // The manager details are updated on clicking update button
    public String doUpdateManagerProfileToDatabase() {

        LOG.info(this.manager.toString());
        LOG.info(this.user.toString());

        this.user = userService.findByUsername(manager.getUser().getUserName(), false);
        this.user.setPassword(this.manager.getUser().getPassword());
        LOG.log(Level.INFO, "AdminPortalController:updateManagerProfileToDatabase: {0}", user.toString());
        this.manager.setUser(this.user);
        LOG.log(Level.INFO, "AdminPortalController:updateManagerProfileToDatabase: {0}", manager.toString());

        userService.update(this.user);
        managerService.update(this.manager);

        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "The selected manager details are updated successfully.",
                "The selected manager details are updated successfully."));

        return "/admin/welcome.xhtml";
    }

    // The manager account is disabled on clicking delete button
    public String doDeleteManagerProfileToDatabase() {

        LOG.info(this.manager.toString());
        LOG.info(this.user.toString());

        this.user = userService.findByUsername(manager.getUser().getUserName(), false);
        this.user.setPassword(this.manager.getUser().getPassword());
        LOG.log(Level.INFO, "AdminPortalController:updateCustomerProfileToDatabase: {0}", user.toString());
        this.manager.setUser(this.user);
        this.manager.setDisabled(true);
        LOG.log(Level.INFO, "AdminPortalController:updateCustomerProfileToDatabase: {0}", manager.toString());

        managerService.update(this.manager);
        userService.update(this.user);

        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "The selected manager is disabled.",
                "The selected manager is disabled ."));

        return "/admin/welcome.xhtml";
    }

    // Add new customer action - for admin only
    public String goToAddNewCustomerPage() {
        return "/admin/newCustomer.xhtml";
    }

    public String addNewCustomer() {

        for (User u : users) {
            if (this.user.getUserName().equals(u.getUserName())) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "This Username already exists. Please choose another username",
                        "Username is already taken."));
                return "/admin/newCustomer.xhtml";
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

        return "/admin/welcome.xhtml";
    }

    // Add new manager action - for admin only
    public String goToAddNewManagerPage() {
        return "/admin/newManager.xhtml";
    }

    public String addNewManager() {

        for (User u : users) {
            if (this.user.getUserName().equals(u.getUserName())) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "This Username already exists. Please choose another username",
                        "Username is already taken."));
                return "/admin/newManager.xhtml";
            }
        }

        // Create a group called CUSTOMERS for new signup's
        group.setGroupName("MANAGERS");
        group.setGroupDescription("This group contains managers.");

        // Set user and group in sec_user_groups
        user.addGroup(group);

        // add the new user to the security_user table
        userService.create(user);

        // Set uname for customer object
        manager.setUser(user);

        managerService.create(manager);

        return "/admin/welcome.xhtml";
    }

    // View operation on Menuitems
    public String viewMenuItems(MenuItem m) {
        this.menuItem = m;
        return "/admin/viewMenuItem.xhtml";
    }

    // Update operation on Menuitems
    public String updateMenuItems(MenuItem m) {
        this.menuItem = m;
        return "/admin/updateMenuItem.xhtml";
    }

    public String updateMenuItemsToDatabase() {

        lineItems = new ArrayList<>();
        lineItems = lineItemService.findAllByItemId(menuItem.getItemId());

        for (LineItem l : lineItems) {
            l.setMenuItems(menuItem);
            LOG.info(menuItem.toString());
            LOG.info(l.toString());

            menuItemService.update(menuItem);
            lineItemService.update(l);
        }

        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Menu Item updated successfully.",
                "Menu Item updated successfully."));

        return "/admin/updateMenuItem.xhtml";
    }

    // Delete menu items
    public String deleteMenuItems(MenuItem m) {
        this.menuItem = m;
        LOG.info(menuItem.toString());
        lineItems = lineItemService.findAllByItemId(menuItem.getItemId());

        for (LineItem l : lineItems) {
            LOG.info(l.toString());

            lineItemService.remove(l);
        }

        menuItemService.remove(menuItem);

        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Menu Item deleted successfully.",
                "Menu Item deleted successfully."));

        return "/admin/manageMenuItems.xhtml";
    }

    // Add new menu item action - for admin only
    public String goToAddNewMenuItem() {
        return "/admin/newMenuItem.xhtml";
    }

    public String addNewMenuItem() {

        LOG.info(menuItem.toString());

        for (MenuItem m : menuItemService.findAll()) {
            if (m.getItemId().equals(menuItem.getItemId())) {

                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "A Menu Item with this ID already exist, please enter a differnt value.",
                        "A Menu Item with this ID already exist, please enter a differnt value."));

                return "/admin/newMenuItem.xhtml";
            }
        }

        menuItemService.create(menuItem);

        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "New Menu Item added successfully.",
                "New Menu Item added successfully."));

        return "/admin/manageMenuItems.xhtml";
    }

    //********************************************Action methods ends*********************************
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
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

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

}
