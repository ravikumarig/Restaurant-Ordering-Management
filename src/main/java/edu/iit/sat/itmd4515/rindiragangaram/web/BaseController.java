/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.iit.sat.itmd4515.rindiragangaram.web;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Ravi Kumar Hazare
 */
public abstract class BaseController {

    protected FacesContext context;
    protected HttpServletRequest request;
    
    protected BaseController() {
    }
    
    @PostConstruct
    public void postConstruct(){
        
        context = FacesContext.getCurrentInstance();
        request = (HttpServletRequest) context.getExternalContext().getRequest();
    }
    
}
