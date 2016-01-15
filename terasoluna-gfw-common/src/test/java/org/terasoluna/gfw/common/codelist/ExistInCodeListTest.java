/*
 * Copyright (C) 2013-2016 terasoluna.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.terasoluna.gfw.common.codelist;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

//import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.terasoluna.gfw.common.codelist.ExistInCodeList;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ExistInCodeListTest {
    // @Inject
    @Autowired
    Validator validator;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void test_allValidStringCode() {
        Person p = new Person();
        p.gender = "F";
        p.lang = "JP";
        Set<ConstraintViolation<Person>> result = validator.validate(p);
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void test_allValidStringCodeWithNull() {
        Person p = new Person();
        p.gender = null;
        p.lang = "JP";
        Set<ConstraintViolation<Person>> result = validator.validate(p);
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void test_allValidStringCodeWithEmpty() {
        Person p = new Person();
        p.gender = "";
        p.lang = "JP";
        Set<ConstraintViolation<Person>> result = validator.validate(p);
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void test_hasInValidStringCode() {
        Person p = new Person();
        p.gender = "G";
        p.lang = "JP";
        Set<ConstraintViolation<Person>> result = validator.validate(p);
        assertThat(result.size(), is(1));
    }

    @Test
    public void test_allInValidStringCode() {
        Person p = new Person();
        p.gender = "G";
        p.lang = "FR";
        Set<ConstraintViolation<Person>> result = validator.validate(p);
        assertThat(result.size(), is(2));
    }

    @Test
    public void test_validCharacterCode() {
        Customer c = new Customer();
        c.gender = 'F';
        c.lang = "JP";
        Set<ConstraintViolation<Customer>> result = validator.validate(c);
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void test_validCharacterCodeWithNull() {
        Customer c = new Customer();
        c.gender = null;
        c.lang = "JP";
        Set<ConstraintViolation<Customer>> result = validator.validate(c);
        assertThat(result.isEmpty(), is(true));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test_hasInValidCharacterCode() {
        Customer c = new Customer();
        c.gender = 'G';
        c.lang = "JP";
        Set<ConstraintViolation<Customer>> result = validator.validate(c);
        assertThat(result.size(), is(1));
        assertThat(((ConstraintViolation<Customer>) result.toArray()[0])
                .getMessage(), is("Does not exist in CD_GENDER"));
    }

    @Test
    public void test_inValidStringAndCharacterCode() {
        Customer c = new Customer();
        c.gender = 'G';
        c.lang = "FR";
        Set<ConstraintViolation<Customer>> result = validator.validate(c);
        assertThat(result.size(), is(2));
    }

    @Test
    public void test_validMultipleExistInCodeList() {
        // check 0~12
        {
            Order order = new Order("0");
            Set<ConstraintViolation<Order>> result = validator.validate(order);
            assertThat(result.isEmpty(), is(true));
        }
        {
            Order order = new Order("6");
            Set<ConstraintViolation<Order>> result = validator.validate(order);
            assertThat(result.isEmpty(), is(true));
        }
        {
            Order order = new Order("12");
            Set<ConstraintViolation<Order>> result = validator.validate(order);
            assertThat(result.isEmpty(), is(true));
        }
    }

    @Test
    public void test_invalidMultipleExistInCodeList() {
        {
            Order order = new Order("1");
            Set<ConstraintViolation<Order>> result = validator.validate(order);
            assertThat(result.size(), is(2));
            SortedSet<String> messages = new TreeSet<String>();
            for (ConstraintViolation<Order> violation : result) {
                messages.add(violation.getMessage());
            }
            Iterator<String> iterator = messages.iterator();
            assertThat(iterator.next(), is("number must be even"));
            assertThat(iterator.next(), is("number must be multiples of 3"));
        }
        {
            Order order = new Order("2");
            Set<ConstraintViolation<Order>> result = validator.validate(order);
            assertThat(result.size(), is(1));
            SortedSet<String> messages = new TreeSet<String>();
            for (ConstraintViolation<Order> violation : result) {
                messages.add(violation.getMessage());
            }
            Iterator<String> iterator = messages.iterator();
            assertThat(iterator.next(), is("number must be multiples of 3"));
        }
        {
            Order order = new Order("4");
            Set<ConstraintViolation<Order>> result = validator.validate(order);
            assertThat(result.size(), is(1));
            SortedSet<String> messages = new TreeSet<String>();
            for (ConstraintViolation<Order> violation : result) {
                messages.add(violation.getMessage());
            }
            Iterator<String> iterator = messages.iterator();
            assertThat(iterator.next(), is("number must be multiples of 3"));
        }
        {
            Order order = new Order("5");
            Set<ConstraintViolation<Order>> result = validator.validate(order);
            assertThat(result.size(), is(2));
            SortedSet<String> messages = new TreeSet<String>();
            for (ConstraintViolation<Order> violation : result) {
                messages.add(violation.getMessage());
            }
            Iterator<String> iterator = messages.iterator();
            assertThat(iterator.next(), is("number must be even"));
            assertThat(iterator.next(), is("number must be multiples of 3"));
        }
        {
            Order order = new Order("7");
            Set<ConstraintViolation<Order>> result = validator.validate(order);
            assertThat(result.size(), is(2));
            SortedSet<String> messages = new TreeSet<String>();
            for (ConstraintViolation<Order> violation : result) {
                messages.add(violation.getMessage());
            }
            Iterator<String> iterator = messages.iterator();
            assertThat(iterator.next(), is("number must be even"));
            assertThat(iterator.next(), is("number must be multiples of 3"));
        }
        {
            Order order = new Order("8");
            Set<ConstraintViolation<Order>> result = validator.validate(order);
            assertThat(result.size(), is(1));
            SortedSet<String> messages = new TreeSet<String>();
            for (ConstraintViolation<Order> violation : result) {
                messages.add(violation.getMessage());
            }
            Iterator<String> iterator = messages.iterator();
            assertThat(iterator.next(), is("number must be multiples of 3"));
        }
        {
            Order order = new Order("9");
            Set<ConstraintViolation<Order>> result = validator.validate(order);
            assertThat(result.size(), is(1));
            SortedSet<String> messages = new TreeSet<String>();
            for (ConstraintViolation<Order> violation : result) {
                messages.add(violation.getMessage());
            }
            Iterator<String> iterator = messages.iterator();
            assertThat(iterator.next(), is("number must be even"));
        }
        {
            Order order = new Order("10");
            Set<ConstraintViolation<Order>> result = validator.validate(order);
            assertThat(result.size(), is(1));
            SortedSet<String> messages = new TreeSet<String>();
            for (ConstraintViolation<Order> violation : result) {
                messages.add(violation.getMessage());
            }
            Iterator<String> iterator = messages.iterator();
            assertThat(iterator.next(), is("number must be multiples of 3"));
        }
        {
            Order order = new Order("11");
            Set<ConstraintViolation<Order>> result = validator.validate(order);
            assertThat(result.size(), is(2));
            SortedSet<String> messages = new TreeSet<String>();
            for (ConstraintViolation<Order> violation : result) {
                messages.add(violation.getMessage());
            }
            Iterator<String> iterator = messages.iterator();
            assertThat(iterator.next(), is("number must be even"));
            assertThat(iterator.next(), is("number must be multiples of 3"));
        }
        {
            // out of range!
            Order order = new Order("18");
            Set<ConstraintViolation<Order>> result = validator.validate(order);
            assertThat(result.size(), is(2));
            SortedSet<String> messages = new TreeSet<String>();
            for (ConstraintViolation<Order> violation : result) {
                messages.add(violation.getMessage());
            }
            Iterator<String> iterator = messages.iterator();
            assertThat(iterator.next(), is("number must be even"));
            assertThat(iterator.next(), is("number must be multiples of 3"));
        }
    }
}

class Person {
    @ExistInCodeList(codeListId = "CD_GENDER")
    public String gender;

    @ExistInCodeList(codeListId = "CD_LANG")
    public String lang;
}

class Customer {
    @ExistInCodeList(codeListId = "CD_GENDER")
    public Character gender;

    @ExistInCodeList(codeListId = "CD_LANG")
    public String lang;
}

class Order {
    @ExistInCodeList.List(value = {
            @ExistInCodeList(codeListId = "CD_MULTIPLES_OF_3", message = "number must be multiples of 3"),
            @ExistInCodeList(codeListId = "CD_EVEN", message = "number must be even") })
    private String orderNumber;

    public Order(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }
}
