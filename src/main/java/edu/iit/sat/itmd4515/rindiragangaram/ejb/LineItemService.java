/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.iit.sat.itmd4515.rindiragangaram.ejb;

import edu.iit.sat.itmd4515.rindiragangaram.model.LineItem;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;

/**
 *
 * @author Ravi Kumar Hazare
 */
@Stateless
public class LineItemService extends BaseService<LineItem> {

    private static final Logger LOG = Logger.getLogger(LineItemService.class.getName());

    public LineItemService() {
        super(LineItem.class);
    }

    @Override
    public List<LineItem> findAll() {
        return getEntityManager().createNamedQuery("LineItem.findAll", LineItem.class).getResultList();
    }

    public List<LineItem> findByCustomerIDAndStatus(Long customerId, Enum status) {

        return getEntityManager().createNamedQuery("LineItem.findByCustomerIDAndStatus", LineItem.class)
                .setParameter("customerid", customerId).setParameter("status", status).getResultList();
    }

    @Override
    public void update(LineItem entity) {
        LOG.info("LineItemService: Update: ");
        getEntityManager().merge(entity);
    }

    @Override
    public void create(LineItem entity) {
        LOG.info("LineItemService: Create: ");
        getEntityManager().persist(entity);
    }

    public List<LineItem> findAllByItemId(Long itemId) {
        return getEntityManager().createNamedQuery("LineItem.findAllByItemId", LineItem.class)
                .setParameter("itemid", itemId)
                .getResultList();
    }

    public List<LineItem> findByStatus(Enum status) {

        return getEntityManager().createNamedQuery("LineItem.findByStatus", LineItem.class)
                .setParameter("status", status)
                .getResultList();
    }
    
    public List<LineItem> findByTransId(Long transId) {
        return getEntityManager().createNamedQuery("LineItem.findByTransId", LineItem.class)
                .setParameter("transid", transId)
                .getResultList();
    }
    
    public List<LineItem> findByStatusAndCustId(Enum status, Long custId) {
        return getEntityManager().createNamedQuery("LineItem.findByStatusAndCustId", LineItem.class)
                .setParameter("status", status)
                .setParameter("custid", custId)
                .getResultList();
    }
    
    public LineItem findByStatusAndCustId(String tab) {
        return getEntityManager().createNamedQuery("LineItem.findByTableNo", LineItem.class)
                .setParameter("tab", tab)
                .getSingleResult();
    }

}
