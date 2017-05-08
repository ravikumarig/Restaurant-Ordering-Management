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
public class CustomerTest {

    private static EntityManagerFactory emf;
    private static Validator validator;
    private EntityManager em;
    private EntityTransaction et;
    private static final Logger LOG = Logger.getLogger(CustomerTest.class.getName());

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
        Customer seed = new Customer("mock@gmail.com", "Mock Customer", new GregorianCalendar(2002, 8, 30).getTime());
        et.begin();
        em.persist(seed);
        et.commit();
    }

    @After
    public void afterEachMethodTestFixture() {
        // This is for cleanup of the seed data inserted at beforeEachTestMethod
        Customer seed
                = em.createNamedQuery("Customer.findByEmail", Customer.class)
                        .setParameter("email", "mock@gmail.com").getSingleResult();

        et.begin();
        em.remove(seed);
        et.commit();

        em.close();

    }

    // This is a test for Create operation using entity manager
    @Test
    public void persistNewCustomerTest() {

        Customer fi = new Customer("ravi@gmail.com", "Ravi Kumar", new GregorianCalendar(2000, 7, 18).getTime());

        et.begin();
        assertNull("Customer ID Should be null before persist and commit", fi.getCustomerId());
        em.persist(fi);
        assertNull("Customer ID Should be null after persist and before commit", fi.getCustomerId());
        et.commit();

        assertTrue("Customer ID should be greater than zero after commit", fi.getCustomerId() > 0);

    }

    // This is to test the Update operation of the entity Customer
    @Test
    public void updateCustomerTest() {
        Customer fi1 = new Customer("sunil@gmail.com", "Sunil Hazare", new GregorianCalendar(2001, 1, 15).getTime());

        et.begin();
        em.persist(fi1);
        em.flush();
        Customer fi2 = em.createNamedQuery("Customer.findByEmail", Customer.class)
                .setParameter("email", "sunil@gmail.com")
                .getSingleResult();
        LOG.info("***PRE-UPDATE: \t" + fi2.toString());
        assertEquals("sunil@gmail.com", fi2.getCustomerEmail());
        fi2.setCustomerEmail("sun@gmail.com");
        em.persist(fi2);
        em.flush();
        Customer fi3 = em.createNamedQuery("Customer.findByEmail", Customer.class)
                .setParameter("email", "sun@gmail.com")
                .getSingleResult();
        LOG.info("***POST-UPDATE: \t" + fi3.toString());
        assertTrue("*** This should be true if the email is updated to new one", fi3.getCustomerEmail() == "sun@gmail.com");
        et.commit();

    }

    //This Test method is show the read operations on the Customer entity
    @Test
    public void readCustomerTest() {
        List<Customer> fi = em.createNamedQuery("Customer.findAll", Customer.class).getResultList();
        LOG.info("*** Number of select queries executed: " + fi.size());
        assertTrue("*** The ResultList should return a value more than zero: ", fi.size() > 0);
        et.begin();
        for (Customer fi1 : fi) {
            System.out.println("***Reading the records from the FoodItem table: \n"
                    + em.find(Customer.class, fi1.getCustomerId()));
        }
        et.commit();
    }

    //this test medthod will Delete all the records from the Customer entity
    @Test
    public void deleteRecordsFromCustomerTest() {

        Customer fi1 = new Customer("rahul@gmail.com", "rahul", new GregorianCalendar(2001, 1, 15).getTime());

        et.begin();
        em.persist(fi1);
        em.flush();

        Customer fi2 = em.createNamedQuery("Customer.findByEmail", Customer.class).setParameter("email", "rahul@gmail.com").getSingleResult();
        LOG.info("***PRE-Delete: \t" + fi2.toString());
        assertTrue("The record exists ", fi2.getCustomerName() == "rahul");
        em.remove(fi2);
        et.commit();
        List<Customer> fi3 = em.createNamedQuery("Customer.findByEmail", Customer.class).setParameter("email", "rahul@gmail.com").getResultList();
        assertFalse("*** Since all the records are removed, the ResultList should return a zero value", fi3.size() > 0);
    }

    // This is to verify the seed data that gets inserted beforeEachTextMethod for the use of rainyDayTest
    @Test
    public void verifySeedData() {
        Customer verifySeed
                = em.createNamedQuery("Customer.findByEmail", Customer.class)
                        .setParameter("email", "mock@gmail.com")
                        .getSingleResult();
        assertEquals("Mock Customer", verifySeed.getCustomerName());
    }

    // Test cases for bean validation, one sunny day case and one rainy day case
    @Test
    public void validateFutureDateAndNoNullSunnyDay() {
        Customer f1 = new Customer("validate@gmail.com", "Bean Validation", new GregorianCalendar(2000, 1, 15).getTime());
        Set<ConstraintViolation<Customer>> violations = validator.validate(f1);
        assertTrue("The violation should be empty as the date is in past and email ID is not null", violations.isEmpty());
    }

    @Test
    public void validateFutureDateAndNoNullRainyDay() {
        Customer f1 = new Customer(null, "Bean Validation", new GregorianCalendar(2029, 1, 15).getTime());
        Set<ConstraintViolation<Customer>> violations = validator.validate(f1);
        LOG.info("*************Constraint Violations Start*************");
        for (ConstraintViolation<Customer> violation : violations) {
            LOG.info(violation.toString());
        }
        LOG.info("*************Constraint Violations End*************");
        assertTrue("There should be 2 violations w.r.t date in future and email ID being null", violations.size() == 2);
        assertFalse("This should be false as there are 2 violations", violations.isEmpty());
    }

    // This is a RainyDay test case, it is expected to fail but the exception is handled.
    @Test(expected = RollbackException.class)
    public void persistNewCustomerShouldFailRainyDayTest() {

        Customer seed1 = new Customer("mock@gmail.com");

        et.begin();
        em.persist(seed1);
        et.commit();
    }

    @Test
    public void oneToManyCustomerBillTest() {
        Cheque b1 = new Cheque(111.0, Cheque.Status.INACTIVE, 12112323l, new GregorianCalendar(2017, 3, 21).getTime());
        Cheque b2 = new Cheque(51.1, Cheque.Status.INACTIVE, 12112322l, new GregorianCalendar(2017, 2, 21).getTime());
        Customer c1 = new Customer("var@gmail.com", "Dummy", new GregorianCalendar(2014, 3, 21).getTime());

        c1.addBill(b1);
        c1.addBill(b2);

        et.begin();
        em.persist(b1);
        em.persist(b2);
        em.persist(c1);
        et.commit();

        Cheque b3 = em.createNamedQuery("Cheque.findByTransId", Cheque.class).setParameter("transid", 12112323l).getSingleResult();
        assertNotNull("Bill Id after persist should not be null", b3.getBillId());
        LOG.info("Bill Table :" + b3.toString());
         Cheque b4 = em.createNamedQuery("Cheque.findByTransId", Cheque.class).setParameter("transid", 12112322l).getSingleResult();
        assertNotNull("Bill ID after persist should not be null", b4.getBillId());
        LOG.info("Bill Table :" + b4.toString());
        
        Customer c2 = em.createNamedQuery("Customer.findByEmail", Customer.class).setParameter("email", "var@gmail.com").getSingleResult();
        assertSame("Dummy", c2.getCustomerName());
        LOG.info("Customer Tbale :" + c2.toString());

    }
}
