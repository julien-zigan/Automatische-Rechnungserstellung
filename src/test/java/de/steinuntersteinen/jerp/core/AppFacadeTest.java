package de.steinuntersteinen.jerp.core;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class AppFacadeTest {
    @Test
    public void testFacade() throws IOException {
        AppFacade appFacade = new AppFacade();
        appFacade.createUser();
        String userId1 = appFacade.getUserId();
        appFacade.createUser();
        String userId2 = appFacade.getUserId();
        assertNotEquals(userId1, userId2);
    }

}