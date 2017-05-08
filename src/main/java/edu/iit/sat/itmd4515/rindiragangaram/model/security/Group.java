/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.iit.sat.itmd4515.rindiragangaram.model.security;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Ravi Kumar Hazare
 */
@Entity
@Table(name = "security_group")
@NamedQueries(
        @NamedQuery(name = "Group.findAll", query = "select g from Group g"))
public class Group {

    @Id
    private String groupName;
    private String groupDescription;

    @ManyToMany(mappedBy = "groups")
    private List<User> users = new ArrayList<>();

    // Default constructor
    public Group() {
    }

    // Constructor with two arguments
    public Group(String groupName, String groupDescription) {
        this.groupName = groupName;
        this.groupDescription = groupDescription;
    }

    // Constructor with three arguments
    public Group(String groupName, String groupDescription, List<User> users) {
        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.users = users;
    }

    /**
     * Get the value of groupName
     *
     * @return the value of groupName
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Set the value of groupName
     *
     * @param groupName new value of groupName
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Group{" + "groupName=" + groupName + ", groupDescription=" + groupDescription + ", users=" + users + '}';
    }

}
