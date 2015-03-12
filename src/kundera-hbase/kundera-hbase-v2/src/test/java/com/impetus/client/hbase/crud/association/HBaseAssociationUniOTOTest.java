/*******************************************************************************
 * * Copyright 2015 Impetus Infotech.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 ******************************************************************************/
package com.impetus.client.hbase.crud.association;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.impetus.client.hbase.testingutil.HBaseTestingUtils;

/**
 * The Class HBaseAssociationUniOTOTest.
 * 
 * @author Devender Yadav
 */
public class HBaseAssociationUniOTOTest
{

    /** The Constant SCHEMA. */
    protected static final String SCHEMA = "HBaseNew";

    /** The Constant HBASE_PU. */
    protected static final String HBASE_PU = "associationTest";

    /** The emf. */
    protected static EntityManagerFactory emf;

    /** The em. */
    protected  EntityManager em;

    /**
     * Sets the up before class.
     * 
     * @throws Exception
     *             the exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        emf = Persistence.createEntityManagerFactory(HBASE_PU);
    }

    /**
     * Sets the up.
     * 
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception
    {
        em = emf.createEntityManager();
    }

    /**
     * Test assocations unidirectional one to one.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public void testAssocationsUniOTO() throws Exception
    {
        testInsert();
        testUpdate();
        testDelete();
    }

    /**
     * Test insert.
     */
    private void testInsert()
    {

        insertPersonAndAddress();

        em.clear();
        PersonUniOTO p = em.find(PersonUniOTO.class, 1);
        Assert.assertNotNull(p);
        Assert.assertEquals(1, p.getPersonId());
        Assert.assertEquals("Devender", p.getPersonName());
        Assert.assertEquals(23, p.getAge());
        Assert.assertNotNull(p.getAddress());
        Assert.assertEquals(101, p.getAddress().getAddressId());
        Assert.assertEquals("Noida", p.getAddress().getCity());

        em.clear();
        AddressUniOTO add = em.find(AddressUniOTO.class, 101);
        Assert.assertNotNull(add);
        Assert.assertEquals(101, add.getAddressId());
        Assert.assertEquals("Noida", add.getCity());
    }

    /**
     * Test update.
     */
    private void testUpdate()
    {
        insertPersonAndAddress();

        em.clear();
        PersonUniOTO p = em.find(PersonUniOTO.class, 1);
        p.setPersonName("Pragalbh");
        p.getAddress().setCity("Indore");

        em.clear();
        em.merge(p);

        em.clear();
        PersonUniOTO p1 = em.find(PersonUniOTO.class, 1);
        em.clear();
        AddressUniOTO add1 = em.find(AddressUniOTO.class, 101);

        Assert.assertEquals("Pragalbh", p1.getPersonName());
        Assert.assertEquals("Indore", p1.getAddress().getCity());
        Assert.assertEquals("Indore", add1.getCity());

    }

    /**
     * Test delete.
     */
    private void testDelete()
    {
        insertPersonAndAddress();
        em.clear();
        em.remove(em.find(PersonUniOTO.class, 1));
        
        em.clear();
        PersonUniOTO p = em.find(PersonUniOTO.class, 1);
        
        em.clear();
        AddressUniOTO add = em.find(AddressUniOTO.class, 101);
        
        Assert.assertNull(p);
        Assert.assertNull(add);

        insertPersonAndAddress();
        em.clear();
        em.remove(em.find(AddressUniOTO.class, 101));
        
        em.clear();
        AddressUniOTO add1 = em.find(AddressUniOTO.class, 101);
        Assert.assertNull(add1);
        
        em.clear();
        PersonUniOTO p1 = em.find(PersonUniOTO.class, 1);
        Assert.assertNull(p1.getAddress());
    }

    /**
     * Insert person and address.
     */
    private void insertPersonAndAddress()
    {
        PersonUniOTO person = new PersonUniOTO(1, "Devender", 23);
        AddressUniOTO address = new AddressUniOTO(101, "Noida");
        person.setAddress(address);
        em.persist(person);
    }

    /**
     * Tear down.
     * 
     * @throws Exception
     *             the exception
     */
    @After
    public void tearDown() throws Exception
    {
        em.close();
    }

    /**
     * Tear down after class.
     * 
     * @throws Exception
     *             the exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception
    {
        emf.close();
        emf = null;
        HBaseTestingUtils.dropSchema(SCHEMA);
    }

}
