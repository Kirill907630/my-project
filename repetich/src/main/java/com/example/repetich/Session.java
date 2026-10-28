package com.example.repetich;

public class Session {
    public static String currentUser = null;  // логин
    public static String role = null;         // "ADMIN" или "USER"

    public static boolean isAdmin() {
        return "ADMIN".equals(role);
    }

    public static void clear() {
        currentUser = null;
        role = null;
    }
}