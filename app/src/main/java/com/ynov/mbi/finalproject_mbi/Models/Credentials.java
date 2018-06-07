package com.ynov.mbi.finalproject_mbi.Models;

//Stocke email et password pour l'envoyer au back lors de l'authentification
//Stocke l'auth_token pour le rendre accessible dans toute l'application
public class Credentials {

    private String email;
    private String password;
    private static String auth_token;

    public Credentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static String getAuth_token() {
        return auth_token;
    }

    public static void setAuth_token(String auth_token) {
        Credentials.auth_token = auth_token;
    }
}
