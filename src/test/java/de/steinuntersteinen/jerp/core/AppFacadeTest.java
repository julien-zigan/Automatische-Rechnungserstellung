package de.steinuntersteinen.jerp.core;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class AppFacadeTest {
    @Test
    public void testFacade() throws Exception {
        AppFacade jerp = new AppFacade();
        jerp.createUser();
        jerp.setFirstName("John");
        jerp.setLastName("Smith");
        jerp.setProfession("Smith");
        jerp.setPhoneNumber("1234567890");
        jerp.setEmail("john.smith@steinuntersteinen.de");
        jerp.setWebsite("www.steinuntersteinen.de");
        jerp.setStreet("Steinuntersteinen-Str. 40 B");
        jerp.setCity("San Francisco");
        jerp.setZipCode("9410");
        jerp.setBankName("SteinuntersteinenBank");
        jerp.setIban("123456789");
        jerp.setBic("123456789");
        jerp.setTaxNumber("123456789");
        jerp.setPathToDocumentDirectory("/home/julien/IdeaProjects/Jerp/Invoices/");
        jerp.saveUser();
    }


    @Test
    public void testCreateUsers() throws IOException {
        AppFacade appFacade = new AppFacade();
        appFacade.createUser();
        String userId1 = appFacade.getUserId();
        appFacade.createUser();
        String userId2 = appFacade.getUserId();
        assertNotEquals(userId1, userId2);
    }

}