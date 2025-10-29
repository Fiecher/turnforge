package com.github.fiecher.turnforge.presentation.cli.input;

import java.util.Scanner;

public class InputReader {
    private final Scanner scanner;

    public InputReader() {
        this.scanner = new Scanner(System.in);
    }

    public String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.next();
    }

    public Integer readInt(String prompt) {
        System.out.print(prompt);
        return scanner.nextInt();
    }

    public void close(){
        scanner.close();
    }

}
