package com.openclassrooms.starterjwt.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "test!1234";
        String encodedPassword = encoder.encode(password);
        System.out.println("Password: " + password);
        System.out.println("Encoded: " + encodedPassword);
    }
}
