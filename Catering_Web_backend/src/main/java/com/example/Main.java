package com.example;

import com.example.initializer.ServerInitializer;

import java.io.IOException;


public class Main {
    public static void main(String[] args) {
        try {
            ServerInitializer.initialize();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}