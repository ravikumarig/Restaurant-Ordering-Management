/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.iit.sat.itmd4515.rindiragangaram.ejb;

import edu.iit.sat.itmd4515.rindiragangaram.model.Cheque;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author Ravi Kumar Hazare
 */
@Stateless
public class ChequeService extends BaseService<Cheque> {

    public ChequeService() {
        super(Cheque.class);
    }

    @Override
    public List<Cheque> findAll() {
        return getEntityManager().createNamedQuery("Cheque.findAll", Cheque.class).getResultList();
    }

    public List<Cheque> findByStatus(Enum status) {
        return getEntityManager().createNamedQuery("Cheque.findByStatus", Cheque.class)
                .setParameter("status", status)
                .getResultList();
    }
    
    public List<Cheque> findByStatusAndCustId(Enum status, Long custId) {
        return getEntityManager().createNamedQuery("Cheque.findByStatusAndCustId", Cheque.class)
                .setParameter("status", status)
                .setParameter("custid", custId)
                .getResultList();
    }
    
    public List<Cheque> findByStatusAndMangId(Enum status, Long mangId) {
        return getEntityManager().createNamedQuery("Cheque.findByStatusAndMangId", Cheque.class)
                .setParameter("status", status)
                .setParameter("mangid", mangId)
                .getResultList();
    }
    
    public Cheque findByTransId(Long transId) {
        return getEntityManager().createNamedQuery("Cheque.findByTransId", Cheque.class)
                .setParameter("transid", transId)
                .getSingleResult();
    }

}
