/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.iit.sat.itmd4515.rindiragangaram.model.security;

import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

/**
 *
 * @author Ravi Kumar Hazare
 */
@Entity
@Table(name = "security_user")
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "select u from User u")
    ,
    @NamedQuery(name = "User.findByUsername", query = "select u from User u where u.userName = :username AND u.disabled =:disabled")
    ,
    @NamedQuery(name = "User.findByUsernameWithoutDisableField", query = "select u from User u where u.userName = :username")

})
public class User {

    @Id
    private String userName;
    private String password;
    private boolean disabled;

    @ManyToMany
    @JoinTable(name = "sec_user_groups", joinColumns = @JoinColumn(name = "USERNAME"),
            inverseJoinColumns = @JoinColumn(name = "GROUPNAME"))
    private List<Group> groups = new ArrayList<>();

    // Default constructor
    public User() {
    }

    // Constructor with two arguments
    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    // Constructor with three arguments
    public User(String userName, String password, boolean enabled) {
        this.userName = userName;
        this.password = password;
        this.disabled = enabled;
    }

    public void addGroup(Group g) {
        if (!this.groups.contains(g)) {
            this.groups.add(g);
        }
        if (!g.getUsers().contains(this)) {
            g.getUsers().add(this);
        }
    }

    /* This method will hash the clear text string stored in the "password" instant variable and reset the variable with a hashed password using
    *  Google guava library, refer : http://www.baeldung.com/sha-256-hashing-java
     */
    @PrePersist
    @PreUpdate
    private void hashingClearTextPassword() {
        String sha256hex = Hashing.sha256()
                .hashString(this.password, StandardCharsets.UTF_8)
                .toString();

        this.password = sha256hex;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return "User{" + "userName=" + userName + ", password=" + password + ", disabled=" + disabled + ", groups=" + groups + '}';
    }

}
