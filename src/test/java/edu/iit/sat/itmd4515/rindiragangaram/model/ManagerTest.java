/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.iit.sat.itmd4515.rindiragangaram.model;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.RollbackException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Ravi Kumar Hazare
 */
public class ManagerTest {

    private static EntityManagerFactory emf;
    private static Validator validator;
    private EntityManager em;
    private EntityTransaction et;
    private static final Logger LOG = Logger.getLogger(ManagerTest.class.getName());

    @BeforeClass
    public static void beforeClassTestFixture() {
        emf = Persistence.createEntityManagerFactory("itmd4515PU_TEST");
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @AfterClass
    public static void afterClassTestFixture() {
        emf.close();
    }

    @Before
    public void beforeEachMethodTestFixture() {
        em = emf.createEntityManager();
        et = em.getTransaction();

        // This is to insert a seed data to run rainyDayTest
        Manager seed = new Manager("mockmanager@gmail.com", "Mock Manager", new GregorianCalendar(2011, 8, 30).getTime());
        et.begin();
        em.persist(seed);
        et.commit();
    }

    @After
    public void afterEachMethodTestFixture() {
        // This is for cleanup of the seed data inserted at beforeEachTestMethod
        Manager seed
                = em.createNamedQuery("Manager.findByEmail", Manager.class)
                        .setParameter("email", "mockmanager@gmail.com")
                        .getSingleResult();
        et.begin();
        em.remove(seed);
        et.commit();

        em.close();

    }

    // This is a test for Create operation using entity Manager
    @Test
    public void persistNewManagerTest() {

        Manager fi = new Manager("admin@gmail.com", "Manager", new GregorianCalendar(2011, 8, 30).getTime());

        et.begin();
        assertNull("Manager ID Should be null before persist and commit", fi.getManagerId());
        em.persist(fi);
        assertNull("Manager ID Should be null after persist and before commit", fi.getManagerId());
        et.commit();

        assertTrue("Manager ID should be greater than zero after commit", fi.getManagerId() > 0);

    }

    // This is to test the Update operation of the entity Manager
    @Test
    public void updateManagerTest() {
        Manager fi1 = new Manager("admin1@gmail.com", "Manager1", new GregorianCalendar(2015, 8, 30).getTime());

        et.begin();
        em.persist(fi1);
        em.flush();
        Manager fi2 = em.createNamedQuery("Manager.findByEmail", Manager.class)
                .setParameter("email", "admin1@gmail.com")
                .getSingleResult();
        LOG.info("***PRE-UPDATE: \t" + fi2.toString());
        assertEquals("admin1@gmail.com", fi2.getManagerEmail());
        fi2.setManagerEmail("newadmin1@gmail.com");
        em.persist(fi2);
        em.flush();
        Manager fi3 = em.createNamedQuery("Manager.findByEmail", Manager.class)
                .setParameter("email", "newadmin1@gmail.com")
                .getSingleResult();
        LOG.info("***POST-UPDATE: \t" + fi3.toString());
        assertTrue("*** This should be true if the email of the Manager is updated", fi3.getManagerEmail() == "newadmin1@gmail.com");
        et.commit();

    }

    //This Test method is show the read operations on the Manager entity
    @Test
    public void readManagerTest() {
        List<Manager> fi = em.createNamedQuery("Manager.findAll", Manager.class).getResultList();
        LOG.info("*** Number of select queries executed: " + fi.size());
        assertTrue("*** The ResultList should return a value more than zero: ", fi.size() > 0);
        et.begin();
        for (Manager fi1 : fi) {
            System.out.println("***Reading the records from the Manager table: \n"
                    + em.find(Manager.class, fi1.getManagerId()));
        }
        et.commit();
    }

    //this test medthod will Delete all the records from the Manager entity
    @Test
    public void deleteRecordsFromManagerTest() {
        Manager fi1 = new Manager("admin11@gmail.com", "Manager11", new GregorianCalendar(2011, 8, 30).getTime());
        et.begin();
        em.persist(fi1);
        em.flush();
        Manager fi2 = em.createNamedQuery("Manager.findByEmail", Manager.class).setParameter("email", "admin11@gmail.com").getSingleResult();
        assertTrue("The record exists: ", fi2.getManagerName() == "Manager11");
        LOG.info("***PRE-DELETE: \t" + fi2.toString());

        em.remove(fi2);
        et.commit();
        
        List<Manager> fi3 = em.createNamedQuery("Manager.findByEmail", Manager.class).setParameter("email", "admin11@gmail.com").getResultList();
        assertFalse("*** Since all the records are removed, the ResultList should return a zero value", fi3.size() > 0);
    }

    // This is to verify the seed data that gets inserted beforeEachTextMethod for the use of rainyDayTest
    @Test
    public void verifySeedData() {
        Manager verifySeed
                = em.createNamedQuery("Manager.findByEmail", Manager.class)
                        .setParameter("email", "mockmanager@gmail.com")
                        .getSingleResult();
        assertSame("mockmanager@gmail.com", verifySeed.getManagerEmail());
    }

    // Test cases for bean validation, one sunny day case and one rainy day case
    @Test
    public void validatePastDateAndNoNullSunnyDay() {
        Manager f1 = new Manager("validatemanager@gmail.com", "Manager3", new GregorianCalendar(2014, 1, 15).getTime());
        Set<ConstraintViolation<Manager>> violations = validator.validate(f1);
        assertTrue("The violation should be empty as the date is in past and email ID is not null", violations.isEmpty());
    }

    @Test
    public void validatePastDateAndNoNullRainyDay() {
        Manager f1 = new Manager(null, "Manager4", new GregorianCalendar(2020, 1, 15).getTime());
        Set<ConstraintViolation<Manager>> violations = validator.validate(f1);
        LOG.info("*************Constraint Violations Start*************");
        for (ConstraintViolation<Manager> violation : violations) {
            LOG.info(violation.toString());
        }
        LOG.info("*************Constraint Violations End*************");
        assertTrue("There should be 2 violations w.r.t date in future and email ID being null", violations.size() == 2);
        assertFalse("This should be false as there are 2 violations", violations.isEmpty());
    }

    // This is a RainyDay test case, it is expected to fail but the exception is handled.
    @Test(expected = RollbackException.class)
    public void persistNewManagerShouldFailRainyDayTest() {

        Manager seed1 = new Manager("mockmanager@gmail.com");

        et.begin();
        em.persist(seed1);
        et.commit();
    }
    
    @Test
    public void manyToManyManagerBillTest(){
        Cheque b1 = new Cheque(111.0, Cheque.Status.INACTIVE, 12112323l, new GregorianCalendar(2017, 3, 21).getTime());
        Cheque b2 = new Cheque(51.1, Cheque.Status.INACTIVE, 12112322l, new GregorianCalendar(2017, 2, 21).getTime());
        Manager m1 = new Manager("raviKr@gmail.com", "ManagerA", new GregorianCalendar(2015, 8, 30).getTime());
        
        m1.addBills(b1);
        m1.addBills(b2);
        b2.setManagerBill(m1);
        
        et.begin();
        em.persist(b1);
        em.persist(b2);
        em.persist(m1);
        et.commit();
        
        Manager m2=em.createNamedQuery("Manager.findByEmail", Manager.class).setParameter("email", "raviKr@gmail.com").getSingleResult();
        LOG.info("Manager Table :"+m2.toString());
        assertSame("raviKr@gmail.com", m2.getManagerEmail());
        
        Cheque b3 = em.createNamedQuery("Cheque.findByTransId", Cheque.class).setParameter("transid", 12112323l).getSingleResult();
        assertNotNull("Bill ID should not be null", b3.getBillId());
        
        Cheque b4 = em.createNamedQuery("Cheque.findByTransId", Cheque.class).setParameter("transid", 12112322l).getSingleResult();
        assertEquals(51.1, b4.getBillAmount(), 0);
        LOG.info("Bill Table :"+b4.toString());
    }
}
