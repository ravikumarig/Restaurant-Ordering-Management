/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.iit.sat.itmd4515.rindiragangaram.web;

import edu.iit.sat.itmd4515.rindiragangaram.ejb.ChequeService;
import edu.iit.sat.itmd4515.rindiragangaram.ejb.GroupService;
import edu.iit.sat.itmd4515.rindiragangaram.ejb.LineItemService;
import edu.iit.sat.itmd4515.rindiragangaram.ejb.ManagerService;
import edu.iit.sat.itmd4515.rindiragangaram.ejb.MenuItemService;
import edu.iit.sat.itmd4515.rindiragangaram.ejb.UserService;
import edu.iit.sat.itmd4515.rindiragangaram.model.Cheque;
import edu.iit.sat.itmd4515.rindiragangaram.model.Customer;
import edu.iit.sat.itmd4515.rindiragangaram.model.LineItem;
import edu.iit.sat.itmd4515.rindiragangaram.model.Manager;
import edu.iit.sat.itmd4515.rindiragangaram.model.MenuItem;
import edu.iit.sat.itmd4515.rindiragangaram.model.security.Group;
import edu.iit.sat.itmd4515.rindiragangaram.model.security.User;
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
public class ManagerPortalController extends BaseController {

    @EJB
    private MenuItemService menuItemService;
    @EJB
    private LineItemService lineItemService;
    @EJB
    private GroupService groupService;
    @EJB
    private UserService userService;
    @EJB
    private ManagerService manService;
    @EJB
    private ChequeService chequeService;

    private static final Logger LOG = Logger.getLogger(ManagerPortalController.class.getName());

    private User user;
    private Group group;
    private Customer customer;
    private Manager manager;
    private LineItem lineItem;
    private Cheque cheque;
    private MenuItem meniItems;

    @Inject
    LoginController loginController;

    @Override
    @PostConstruct
    public void postConstruct() {
        super.postConstruct();
        manager = new Manager();
        user = new User();
        manager.setUser(user);
        group = new Group();
        lineItem = new LineItem();
        meniItems = new MenuItem();
        lineItem.setMenuItems(meniItems);
        cheque = new Cheque();

    }

    // Default constructor
    public ManagerPortalController() {
    }

    //Utility methods 
    //vvvvvvvvvvvvvvvvvvvvv
    public String getRemoteUser() {

        return context.getExternalContext().getRemoteUser();
    }

    public List<Manager> getAllManagers() {
        return manService.findAll();

    }

    public Manager getManagerByUsername() {
        return manService.findByUsername(getRemoteUser());
    }

    public List<LineItem> getLineItemByStatusAsBillRequested() {
        return lineItemService.findByStatus(LineItem.Status.BILL_REQUESTED);
    }

    public List<Cheque> getChequesWhichAreOnStatusActive() {

        return chequeService.findByStatus(Cheque.Status.ACTIVE);
    }
    
     public List<Cheque> getChequesWhichAreClosedByTheLoggedInManagerId() {

         manager = manService.findByUsername(loginController.getRemoteUser());
        return chequeService.findByStatusAndMangId(Cheque.Status.INACTIVE, manager.getManagerId());
    }
    //^^^^^^^^^^^^^^^^^^^^^  

    //*******************Action methods starts******************************
    // The method will take the customer to the update profile page 
    public String goToUpdateManagerProfilePage(Manager m) {

        this.user = userService.findByUsername(loginController.getRemoteUser(), false);
        this.manager = m;
        this.manager.setUser(this.user);
        LOG.log(Level.INFO, "ManagerPortalController:updateManagerProfile: {0}", manager.toString());
        LOG.log(Level.INFO, "ManagerPortalController:updateManagerProfile: {0}", user.toString());
        return "/manager/updateManager.xhtml";
    }

    // The method will update the changes the customer made and will save to customer table in the database
    public String updateManagerProfileToDatabase() {
        LOG.log(Level.INFO, "ManagerPortalController:updateManagerProfileToDatabase: {0}", this.manager.toString());

        this.user = userService.findByUsername(loginController.getRemoteUser(), false);
        this.user.setPassword(this.manager.getUser().getPassword());
        LOG.log(Level.INFO, "ManagerPortalController:updateManagerProfileToDatabase: {0}", user.toString());
        this.manager.setUser(this.user);
        LOG.log(Level.INFO, "ManagerPortalController:updateManagerProfileToDatabase: {0}", manager.toString());

        userService.update(this.user);
        manService.update(this.manager);

        String result = loginController.doLogout();

        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Your Profile is updated successfully ! Please login again to see the changes.",
                "Updates made will be availabe post relogin."));
        return result;
    }

    // This method will mark the selected cheque as inactive and all its associated line-items as billed
    public String closeCheque(Cheque actCheque) {

        this.cheque = actCheque;
        this.manager = manService.findByUsername(loginController.getRemoteUser());
        cheque.setManagerBill(manager);
        cheque.setBillStatus(Cheque.Status.INACTIVE);
        LOG.info(cheque.toString());
        chequeService.update(cheque);
        
        for(LineItem l: lineItemService.findByTransId(cheque.getTransactionId())){
            
            l.setStatus(LineItem.Status.BILLED);
            lineItemService.update(l);
        }
        
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "The Cheque is marked as Paid and closed.",
                "The Cheque is marked as Paid and closed."));
        

        return "/manager/manageLineItemCheque.xhtml";
    }

    //*******************Action methods ends******************************
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

    public LineItem getLineItem() {
        return lineItem;
    }

    public void setLineItem(LineItem lineItem) {
        this.lineItem = lineItem;
    }

    public Cheque getCheque() {
        return cheque;
    }

    public void setCheque(Cheque cheque) {
        this.cheque = cheque;
    }

    public MenuItem getMeniItems() {
        return meniItems;
    }

    public void setMeniItems(MenuItem meniItems) {
        this.meniItems = meniItems;
    }
}
