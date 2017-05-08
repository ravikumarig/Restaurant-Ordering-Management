/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.iit.sat.itmd4515.rindiragangaram.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "cheque")
@NamedQueries({
    @NamedQuery(name = "Cheque.findAll", query = "select c from Cheque c")
    ,
    @NamedQuery(name = "Cheque.findByStatus", query = "select c from Cheque c where c.billStatus =:status")
    ,
    @NamedQuery(name = "Cheque.findByStatusAndCustId",
            query = "select c from Cheque c where c.billStatus =:status AND c.customer.customerId =:custid")
    ,
    @NamedQuery(name = "Cheque.findByStatusAndMangId",
            query = "select c from Cheque c where c.billStatus =:status AND c.managerBill.managerId =:mangid"),
    @NamedQuery(name = "Cheque.findByTransId",
            query = "select c from Cheque c where c.transactionId =:transid")
})
public class Cheque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billId;

    private Double billAmount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(10) default 'ACTIVE'")
    private Status billStatus;

    @Column(unique = true)
    private Long transactionId;

    public Status getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(Status billStatus) {
        this.billStatus = billStatus;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public enum Status {
        ACTIVE,
        INACTIVE
    }

    @Past(message = "The date must be in the past or present")
    @Temporal(TemporalType.DATE)
    private Date billGeneratedDate;

    @ManyToOne
    @JoinColumn(name = "MANAGER_ID")
    private Manager managerBill;

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;

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

    // Default constructor
    public Cheque() {
    }
    
    public Cheque(Double billAmount, Status billStatus, Long transactionId, Date billGeneratedDate) {
        this.billAmount = billAmount;
        this.billStatus = billStatus;
        this.transactionId = transactionId;
        this.billGeneratedDate = billGeneratedDate;
    }
    

    /**
     * Get the value of billGeneratedDate
     *
     * @return the value of billGeneratedDate
     */
    public Date getBillGeneratedDate() {
        return billGeneratedDate;
    }

    /**
     * Set the value of billGeneratedDate
     *
     * @param billGeneratedDate new value of billGeneratedDate
     */
    public void setBillGeneratedDate(Date billGeneratedDate) {
        this.billGeneratedDate = billGeneratedDate;
    }

    /**
     * Get the value of billAmount
     *
     * @return the value of billAmount
     */
    public Double getBillAmount() {
        return billAmount;
    }

    /**
     * Set the value of billAmount
     *
     * @param billAmount new value of billAmount
     */
    public void setBillAmount(Double billAmount) {
        this.billAmount = billAmount;
    }

    /**
     * Get the value of billId
     *
     * @return the value of billId
     */
    public Long getBillId() {
        return billId;
    }

    /**
     * Set the value of billId
     *
     * @param billId new value of billId
     */
    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public Manager getManagerBill() {
        return managerBill;
    }

    public void setManagerBill(Manager managerBill) {
        this.managerBill = managerBill;
    }

    @Override
    public String toString() {
        return "Cheque{" + "billId=" + billId + ", billAmount=" + billAmount + ", billStatus=" + 
                billStatus + ", billGeneratedDate=" + billGeneratedDate + '}';
    }

}
