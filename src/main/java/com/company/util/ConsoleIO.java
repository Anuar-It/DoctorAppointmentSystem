package com.company.util;

import java.util.Scanner;

public class ConsoleIO {
    private final Scanner sc = new Scanner(System.in);

    public void println(String s) { System.out.println(s); }
    public void print(String s) { System.out.print(s); }

    public String readLine(String prompt) {
        print(prompt);
        return sc.nextLine().trim();
    }

    public int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readLine(prompt));
            } catch (NumberFormatException e) {
                println("Enter a number.");
            }
        }
    }
}
