package nl.ts.service;

import java.util.Arrays;

/**
 * Created by theos on 22-11-2015.
 */
public class AuthenticationService {

    static String[] authenticatedUsers = {"Theo", "Hetty", "Astrid", "Maaike", "Inge", "root"};

    public static boolean authenticate(String user, String password){
        return Arrays.asList(authenticatedUsers).contains(user);
    }
}
