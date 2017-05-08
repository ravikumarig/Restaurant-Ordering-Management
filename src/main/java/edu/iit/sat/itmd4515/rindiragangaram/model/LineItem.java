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
import javax.persistence.JoinColumns;
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
@Table(name = "lineitem")
@NamedQueries({
    @NamedQuery(name = "LineItem.findAll", query = "select l from LineItem l")
    ,
    @NamedQuery(name = "LineItem.findByCustomerIDAndStatus",
            query = "select l from LineItem l where l.customer.customerId =:customerid AND l.status =:status")
    ,
    @NamedQuery(name = "LineItem.findAllByItemId", query = "select l from LineItem l where l.menuItem.itemId =:itemid")
    ,
    @NamedQuery(name = "LineItem.findByStatus", query = "select l from LineItem l where l.status =:status")
    ,
    @NamedQuery(name = "LineItem.findByTransId", query = "select l from LineItem l where l.transId =:transid")
    ,
    @NamedQuery(name = "LineItem.findByStatusAndCustId",
            query = "select l from LineItem l where l.status =:status AND l.customer.customerId =:custid"),
    @NamedQuery(name = "LineItem.findByTableNo",
            query = "select l from LineItem l where l.tableNo =:tab")
})
public class LineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20) default 'UNBILLED'")
    private Status status;
    private Double orderAmount;

    private Long transId;

    public enum Status {
        UNBILLED,
        BILLED,
        CANCELLED,
        BILL_REQUESTED
    }

    @NotNull
    private String tableNo;

    private Integer quantity;

    @Past(message = "The date must be in the past or present")
    @Temporal(TemporalType.DATE)
    private Date orderDate;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "ITEM_ID", referencedColumnName = "ITEMID")
        ,
        @JoinColumn(name = "ITEM_NAME", referencedColumnName = "ITEMNAME")
    })
    private MenuItem menuItem;

    @ManyToOne
    @JoinColumn(name = "CUST_ID")
    private Customer customer;

    public MenuItem getMenuItems() {
        return menuItem;
    }

    public void setMenuItems(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    // default constructor
    public LineItem() {
    }

    // consructor1
    public LineItem(String tableNo, Integer quantity, Date orderDate) {
        this.tableNo = tableNo;
        this.quantity = quantity;
        this.orderDate = orderDate;
    }

    // consructor2
    public LineItem(String tableNo) {
        this.tableNo = tableNo;
    }

    /**
     * Get the value of orderDate
     *
     * @return the value of orderDate
     */
    public Date getOrderDate() {
        return orderDate;
    }

    /**
     * Set the value of orderDate
     *
     * @param orderDate new value of orderDate
     */
    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    /**
     * Get the value of quantity
     *
     * @return the value of quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Set the value of quantity
     *
     * @param quantity new value of quantity
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * Get the value of tableNo
     *
     * @return the value of tableNo
     */
    public String getTableNo() {
        return tableNo;
    }

    /**
     * Set the value of tableNo
     *
     * @param tableNo new value of tableNo
     */
    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    /**
     * Get the value of orderId
     *
     * @return the value of orderId
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * Set the value of orderId
     *
     * @param orderId new value of orderId
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Double getOrderAmount() {
        return orderAmount;
    }

    public Long getTransId() {
        return transId;
    }

    public void setTransId(Long transId) {
        this.transId = transId;
    }

    @Override
    public String toString() {
        return "LineItem{" + "status=" + status + ", orderAmount=" + orderAmount + ", orderId=" + orderId + ", tableNo=" + tableNo + ", "
                + "quantity=" + quantity + ", orderDate=" + orderDate + '}';
    }

}
