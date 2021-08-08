package org.my.automationtest.utils;

import java.util.Base64;
import java.util.Scanner;

public class EncryptUtil {

    public static void main(String[] args) {
        // Mock encrypting a password by using Base64 encoding
        Scanner scanner = new Scanner(System.in);
        String inputPassword = scanner.nextLine();
        byte[] encodedByteArr = Base64.getEncoder().encode(inputPassword.getBytes());
        String encoded = new String(encodedByteArr);
        System.out.println("Encoded password string is: " + encoded);
    }
}
