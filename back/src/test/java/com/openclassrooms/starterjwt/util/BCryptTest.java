package com.openclassrooms.starterjwt.util;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptTest {
    
    @Test
    public void generateBCryptHash() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "test!1234";
        String encodedPassword = encoder.encode(password);
        System.out.println("Password: " + password);
        System.out.println("Encoded: " + encodedPassword);
        
        // Vérifier que le hash fonctionne
        boolean matches = encoder.matches(password, encodedPassword);
        System.out.println("Matches: " + matches);
    }
}
