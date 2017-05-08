/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.iit.sat.itmd4515.rindiragangaram.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "menuitem")
@NamedQueries({
    @NamedQuery(name = "MenuItem.findAll", query = "select m from MenuItem m")
    ,
    @NamedQuery(name = "MenuItem.findByName", query = "select m from MenuItem m where m.itemName = :itemname")
})
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @NotNull(message = "The name of the MenuItem can not be null")
    @Column(unique = true)
    private String itemName;

    private Double cost;
    private String category;

    @Past(message = "The date must be in the past or present")
    @Temporal(TemporalType.DATE)
    private Date dateAdded;

    private int quantity;

    // default constructor
    public MenuItem() {
    }

    // constructor 1
    public MenuItem(String itemName, Double cost, String category) {
        this.itemName = itemName;
        this.cost = cost;
        this.category = category;
    }

    // constructor 2
    public MenuItem(String itemName, Double cost, String category, Date dateAdded) {
        this.itemName = itemName;
        this.cost = cost;
        this.category = category;
        this.dateAdded = dateAdded;
    }

    // constructor 3
    public MenuItem(String itemName) {
        this.itemName = itemName;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    @Override
    public String toString() {
        return "MenuItem{" + "itemId=" + itemId + ", itemName=" + itemName + ", cost=" + cost + ", category=" + category + ", dateAdded=" + dateAdded + '}';
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
