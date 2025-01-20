package com.currenjin.concurrency;

public class FSOAccount {
    private double balance;

    public FSOAccount(double openingBalance) {
        this.balance = openingBalance;
    }

    public synchronized void deposit(int amount) {
        balance += amount;
    }

    public double getBalance() {
        return balance;
    }

    public synchronized boolean transferTo(FSOAccount other, int amount) {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (amount >= balance) {
            balance -= amount;
            other.deposit(amount);
            return true;
        }

        return false;
    }
}
