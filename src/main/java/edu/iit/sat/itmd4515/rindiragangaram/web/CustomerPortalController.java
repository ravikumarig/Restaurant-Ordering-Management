/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.iit.sat.itmd4515.rindiragangaram.web;

import edu.iit.sat.itmd4515.rindiragangaram.ejb.ChequeService;
import edu.iit.sat.itmd4515.rindiragangaram.ejb.CustomerService;
import edu.iit.sat.itmd4515.rindiragangaram.ejb.LineItemService;
import edu.iit.sat.itmd4515.rindiragangaram.ejb.MenuItemService;
import edu.iit.sat.itmd4515.rindiragangaram.ejb.UserService;
import edu.iit.sat.itmd4515.rindiragangaram.model.Cheque;
import edu.iit.sat.itmd4515.rindiragangaram.model.Customer;
import edu.iit.sat.itmd4515.rindiragangaram.model.LineItem;
import edu.iit.sat.itmd4515.rindiragangaram.model.MenuItem;
import edu.iit.sat.itmd4515.rindiragangaram.model.security.User;
import java.util.ArrayList;
import java.util.Date;
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
public class CustomerPortalController extends BaseController {
    
    @EJB
    private UserService userService;
    
    @EJB
    private MenuItemService menuItemService;
    
    @EJB
    private LineItemService lineItemService;
    
    @EJB
    private ChequeService chequeService;
    
    @EJB
    private CustomerService cusService;
    
    @Inject
    LoginController loginController;
    
    private static List<MenuItem> cart = new ArrayList<>();
    private static List<LineItem> activeOrders = new ArrayList<>();
    private static Double finalBillAmount = 0.0;
    
    private static final Logger LOG = Logger.getLogger(CustomerPortalController.class.getName());

    // Using Pojo's as property
    private MenuItem menuItem;
    private LineItem lineItem;
    private Cheque cheque;
    private Customer customer;

    // This element will store the value of the quantity from the viewMenu JSF page
    private Integer quantity;
    private User user;
    
    private Long tempTransId = 0l;

    // Default Constructor
    public CustomerPortalController() {
    }
    
    @Override
    @PostConstruct
    public void postConstruct() {
        super.postConstruct();
        customer = cusService.findByUsername(loginController.getRemoteUser(), false);
        LOG.log(Level.INFO, "Post construct of CustomerPortalController::{0}", customer.toString());
        menuItem = new MenuItem();
        lineItem = new LineItem();
        cheque = new Cheque();
        user = new User();
        finalBillAmount = 0.0;
    }

    //Utility methods 
    //vvvvvvvvvvvvvvvvvvvvv
    public List<Customer> getAllCustomers() {
        return cusService.findAllNotDisabled(false);
    }
    
    public Customer getCustomerByUsername() {
        return cusService.findByUsername(loginController.getRemoteUser(), false);
    }
    
    public List<MenuItem> getAllMenuItems() {
        return menuItemService.findAll();
    }
    
    public List<MenuItem> getItemsFromCart() {
        return cart;
    }
    
    public List<LineItem> getActiveOrders() {
        return CustomerPortalController.activeOrders;
    }

    public List<LineItem> getLineItemByStatusAsBilledAndCustomerId() {
        customer = cusService.findByUsername(loginController.getRemoteUser(), false);
        return lineItemService.findByStatusAndCustId(LineItem.Status.BILLED, customer.getCustomerId());
    }

    public List<Cheque> getChequesWhichAreOnStatusInactiveAndCustomerId() {
        customer = cusService.findByUsername(loginController.getRemoteUser(), false);
        return chequeService.findByStatusAndCustId(Cheque.Status.INACTIVE, customer.getCustomerId());
    }
    
    // Fetches the lineItems with status UNBILLED for a particular customer ID
    public String goToActiveOrdersPage() {
        activeOrders = lineItemService.findByCustomerIDAndStatus(customer.getCustomerId(), LineItem.Status.UNBILLED);
        
        LOG.log(Level.INFO, "The size of Active Orders is : {0}", getActiveOrderSize());
        
        return request.getContextPath() + "/customer/viewActiveOrders.xhtml";
    }

    // 
    public int getActiveOrderSize() {
        return CustomerPortalController.activeOrders.size();
    }

    //
    public void requestForBillAmount() {
        
        finalBillAmount = 0.0;
        
        for (LineItem li : activeOrders) {
            finalBillAmount += li.getOrderAmount();
        }
        
        this.cheque.setBillAmount(finalBillAmount);
        this.cheque.setBillGeneratedDate(new Date());
        this.cheque.setBillStatus(Cheque.Status.ACTIVE);
        this.cheque.setCustomer(customer);
        tempTransId = System.currentTimeMillis();
        this.cheque.setTransactionId(tempTransId);
        LOG.info(cheque.toString());
        
        chequeService.create(cheque);
        
        LOG.info(activeOrders.toString());
        
        for (LineItem li : activeOrders) {
            
            li.setTransId(tempTransId);
            li.setStatus(LineItem.Status.BILL_REQUESTED);
            
            LOG.info(li.toString());
            lineItemService.update(li);            
        }
        
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
                "Bill has been requested. Please pay the Total Bill Amount to the Manager!", 
                "Cheque table and the LineItem table have been updated."));
    }

    // The method provide the size of the Menu Items cart
    public int getCartCount() {
        return cart.size();
    }
    //^^^^^^^^^^^^^^^^^^^^^

    // Action methods
    // This method will add the Menu Items to cart
    public String addToCart(MenuItem m, String value) {
        LOG.info(m.toString() + "Quantity is: " + value);
        
        try {
            this.quantity = Integer.parseInt(value);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "NumberFormatException: ", e);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Quantity cannot be a NON-INTEGER value or NULL!",
                    "Quantity cannot be a NON-INTEGER value or NULL! Please enter a valid inetger."));
            return "/customer/viewMenu.xhtml";
            
        }
        if (quantity <= 0) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Please enter a valid positive integer value for quantity !",
                    "Quantity should be a valid positive integer."));
            return "/customer/viewMenu.xhtml";
        }
        
        this.menuItem = m;
        this.menuItem.setQuantity(quantity);
        cart.add(menuItem);
        
        return "/customer/viewMenu.xhtml";
    }

    // This method will display the items in the cart
    public String viewCart() {
        if (cart.isEmpty()) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Your cart is empty, Please add items to view them.",
                    "The User has not added items to the cart."));
        }
        return "/customer/viewCart.xhtml";
    }

    // This method will place the items in the cart to the lineitem table in db
    public void placeOrder(List<MenuItem> placedOrder, String tableNo) {
        
        if (!tableNo.equals("Choose one")) {
            for (MenuItem m : placedOrder) {
                this.lineItem.setQuantity(m.getQuantity());
                this.lineItem.setStatus(LineItem.Status.UNBILLED);
                this.lineItem.setTransId(null);
                this.lineItem.setTableNo(tableNo);
                this.lineItem.setMenuItems(m);
                this.lineItem.setOrderAmount(m.getQuantity() * m.getCost());
                this.lineItem.setCustomer(customer);
                this.lineItem.setOrderDate(new Date());
                
                LOG.log(Level.INFO, lineItem.toString());
                
                lineItemService.create(lineItem);
            }
            
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Order placed successfully !!",
                    "Entity is persisted"));
            
            emptyCart();
            
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Please choose a Table number to place an order",
                    "Table number is not selected"));
        }
    }

    // Remove item from cart
    public void removeItemsFromCart(MenuItem m) {
        LOG.log(Level.INFO, "Logging cart removal:   {0}", m.toString());
        cart.remove(m);
    }

    // The method will take the customer to the update profile page 
    public String goToUpdateCustomerProfilePage(Customer c) {
        
        this.user = userService.findByUsername(loginController.getRemoteUser(),false);
        this.customer = c;
        this.customer.setUser(this.user);
        LOG.log(Level.INFO, "CustomerPortalController:updateCustomerProfile: {0}", customer.toString());
        LOG.log(Level.INFO, "CustomerPortalController:updateCustomerProfile: {0}", user.toString());
        return "/customer/updateCustomer.xhtml";
    }

    // The method will update the changes the customer made and will save to customer table in the database
    public String updateCustomerProfileToDatabase() {
        LOG.log(Level.INFO, "CustomerPortalController:updateCustomerProfileToDatabase: {0}", this.customer.toString());
        
        this.user = userService.findByUsername(loginController.getRemoteUser(),false);
        this.user.setPassword(this.customer.getUser().getPassword());
        LOG.log(Level.INFO, "CustomerPortalController:updateCustomerProfileToDatabase: {0}", user.toString());
        this.customer.setUser(this.user);
        LOG.log(Level.INFO, "CustomerPortalController:updateCustomerProfileToDatabase: {0}", customer.toString());
        
        userService.update(this.user);
        cusService.update(this.customer);
        
        String result = loginController.doLogout();
        
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Your Profile is updated successfully ! Please login again to see the changes.",
                "Updates made will be availabe post relogin."));
        return result;
    }

    // Setters and getters
    public MenuItem getMenuItem() {
        return menuItem;
    }
    
    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
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
    
    public Customer getCustomer() {
        return customer;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public void emptyCart() {
        this.cart.clear();
    }
    
    public Double getFinalBillAmount() {
        return finalBillAmount;
    }
    
    public void setFinalBillAmount(Double finalBillAmount) {
        CustomerPortalController.finalBillAmount = finalBillAmount;
    }
    
    public Long getTempTransId() {
        return tempTransId;
    }
    
    public void setTempTransId(Long tempTransId) {
        this.tempTransId = tempTransId;
    }
    
}
