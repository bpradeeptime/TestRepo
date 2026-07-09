package com.example;

/**
 * Simple Greeting class that provides customizable greetings
 */
public class Greeting {

    private String name;

    public Greeting() {
        this.name = "World";
    }

    public Greeting(String name) {
        this.name = name;
    }

    public String getGreeting() {
        return "Hello, " + name + "!";
    }

    public static void main(String[] args) {
        Greeting greeting = new Greeting();
        System.out.println(greeting.getGreeting());
    }
}
