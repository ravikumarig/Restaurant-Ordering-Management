/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.iit.sat.itmd4515.rindiragangaram.model;

import edu.iit.sat.itmd4515.rindiragangaram.model.security.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

/**
 *
 * @author Ravi Kumar Hazare
 */
@Entity
@Table(name = "customer")
@NamedQueries({
    @NamedQuery(name = "Customer.findAll", query = "select o from Customer o")
    ,
    @NamedQuery(name = "Customer.findAllNotDisabled", query = "select o from Customer o where o.disabled =:disabled")
    ,
    @NamedQuery(name = "Customer.findByEmail", query = "select o from Customer o where o.customerEmail = :email")
    ,
    @NamedQuery(name = "Customer.findByUsername", query = "select o from Customer o where o.user.userName = :username and o.user.disabled =:disabled")
    ,
    @NamedQuery(name = "Customer.findByUsernameOnly", query = "select o from Customer o where o.user.userName = :username")

})
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @NotNull(message = "The email ID can not be null")
    @Column(unique = true)
    private String customerEmail;

    private String customerName;

    @Past(message = "The BirthDate value must be in the past")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @OneToMany(mappedBy = "customer")
    private List<Cheque> bills = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "USERNAME")
    private User user;

    @OneToMany(mappedBy = "customer")
    private List<LineItem> lineItems = new ArrayList<>();
    private boolean disabled;

    // A convenience method to persist both sides of the relationship
    public void addBill(Cheque b) {
        this.bills.add(b);
        b.setCustomer(this);
    }

    /**
     * Get the value of bills
     *
     * @return the value of bills
     */
    public List<Cheque> getBills() {
        return bills;
    }

    /**
     * Set the value of bills
     *
     * @param bills new value of bills
     */
    public void setBills(List<Cheque> bills) {
        this.bills = bills;
    }

    // default constructor
    public Customer() {
    }

    // consructor1
    public Customer(String customerEmail, String customerName) {
        this.customerEmail = customerEmail;
        this.customerName = customerName;
    }

    // consructor2
    public Customer(String customerEmail, String customerName, Date dateOfBirth) {
        this.customerEmail = customerEmail;
        this.customerName = customerName;
        this.dateOfBirth = dateOfBirth;
    }

    // consructor3
    public Customer(String customerEmail) {
        this.customerEmail = customerEmail;
    } 

    /**
     * Get the value of dateOfBirth
     *
     * @return the value of dateOfBirth
     */
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Set the value of dateOfBirth
     *
     * @param dateOfBirth new value of dateOfBirth
     */
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Get the value of customerEmail
     *
     * @return the value of customerEmail
     */
    public String getCustomerEmail() {
        return customerEmail;
    }

    /**
     * Set the value of customerEmail
     *
     * @param customerEmail new value of customerEmail
     */
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    /**
     * Get the value of customerName
     *
     * @return the value of customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Set the value of customerName
     *
     * @param customerName new value of customerName
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * Get the value of customerId
     *
     * @return the value of customerId
     */
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * Set the value of customerId
     *
     * @param customerId new value of customerId
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    @Override
    public String toString() {
        return "Customer{" + "customerId=" + customerId + ", customerEmail=" + customerEmail 
                + ", customerName=" + customerName + ", dateOfBirth=" + dateOfBirth + '}';
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
    

}
