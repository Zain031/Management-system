package com.abpgroup.managementsystem.utils.exeptions;

public class AuthenticationExeption extends RuntimeException {

    public AuthenticationExeption(String message) {
        super(message);
    }
}
