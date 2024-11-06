package com.wisetime.wisetime.models.organization;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AddressTest {

    @Test
    public void testAddressCreation() {
        Address address = new Address("Main St", "123", "Apt 4", "Springfield", "IL", "62701");
        
        assertEquals("Main St", address.getStreet());
        assertEquals("123", address.getNumber());
        assertEquals("Apt 4", address.getComplement());
        assertEquals("Springfield", address.getCity());
        assertEquals("IL", address.getState());
        assertEquals("62701", address.getZipCode());
    }

    @Test
    public void testAddressSetters() {
        Address address = new Address();
        
        address.setStreet("Elm St");
        address.setNumber("456");
        address.setComplement("Suite B");
        address.setCity("Chicago");
        address.setState("IL");
        address.setZipCode("60616");

        assertEquals("Elm St", address.getStreet());
        assertEquals("456", address.getNumber());
        assertEquals("Suite B", address.getComplement());
        assertEquals("Chicago", address.getCity());
        assertEquals("IL", address.getState());
        assertEquals("60616", address.getZipCode());
    }

    @Test
    public void testAddressEquality() {
        Address address1 = new Address("Main St", "123", "Apt 4", "Springfield", "IL", "62701");
        Address address2 = new Address("Main St", "123", "Apt 4", "Springfield", "IL", "62701");
        
        assertEquals(address1, address2);
    }
}
