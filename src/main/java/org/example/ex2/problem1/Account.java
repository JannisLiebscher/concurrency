package org.example.ex2.problem1;

public class Account {
    private double balance;
    public Account(double balance) {}
    public void deposit(double amount) {
        balance += amount;
    }
    public void withdraw(double amount) {
        balance -= amount;
    }

    public double getBalance() {
        return balance;
    }
}
