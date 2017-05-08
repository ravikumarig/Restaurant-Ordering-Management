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
@Table(name = "manager")
@NamedQueries({
    @NamedQuery(name = "Manager.findAll", query = "select o from Manager o")
    ,
    @NamedQuery(name = "Manager.findByEmail", query = "select o from Manager o where o.managerEmail = :email")
    ,
    @NamedQuery(name = "Manager.findByUsername", query = "select o from Manager o where o.user.userName = :username")
    ,
    @NamedQuery(name = "Manager.findByUsernameAndDisable",
            query = "select o from Manager o where o.user.userName = :username AND o.user.disabled = :disabled")
    ,
    @NamedQuery(name = "Manager.findAllNotDisabled", query = "select o from Manager o where o.disabled = :disabled")
})
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long managerId;

    @NotNull(message = "The email ID can not be null")
    @Column(unique = true)
    private String managerEmail;

    private String managerName;

    @Past(message = "The Date value must be a Past date")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @OneToMany(mappedBy = "managerBill")
    private List<Cheque> hotelBills = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "USERNAME")
    private User user;
    private boolean disabled;

    public void addBills(Cheque b) {
        this.hotelBills.add(b);
        b.setManagerBill(this);
    }

    public List<Cheque> getHotelBills() {
        return hotelBills;
    }

    public void setHotelBills(List<Cheque> hotelBills) {
        this.hotelBills = hotelBills;
    }

    // default constructor
    public Manager() {
    }

    // constructor 1
    public Manager(String managerEmail, String managerName, Date dateOfBirth) {
        this.managerEmail = managerEmail;
        this.managerName = managerName;
        this.dateOfBirth = dateOfBirth;
    }

    // constructor 2
    public Manager(String managerEmail, String managerName) {
        this.managerEmail = managerEmail;
        this.managerName = managerName;
    }

    // constructor 3
    public Manager(String managerEmail) {
        this.managerEmail = managerEmail;
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
     * Get the value of managerName
     *
     * @return the value of managerName
     */
    public String getManagerName() {
        return managerName;
    }

    /**
     * Set the value of managerName
     *
     * @param managerName new value of managerName
     */
    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    /**
     * Get the value of managerEmail
     *
     * @return the value of managerEmail
     */
    public String getManagerEmail() {
        return managerEmail;
    }

    /**
     * Set the value of managerEmail
     *
     * @param managerEmail new value of managerEmail
     */
    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    /**
     * Get the value of managerId
     *
     * @return the value of managerId
     */
    public Long getManagerId() {
        return managerId;
    }

    /**
     * Set the value of managerId
     *
     * @param managerId new value of managerId
     */
    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    @Override
    public String toString() {
        return "Manager{" + "managerId=" + managerId + ", managerEmail=" + managerEmail + ", managerName=" + managerName 
                + ", dateOfBirth=" + dateOfBirth + '}';
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

}
