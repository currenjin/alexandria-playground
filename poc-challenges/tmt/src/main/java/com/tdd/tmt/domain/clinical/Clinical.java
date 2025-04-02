package com.tdd.tmt.domain.clinical;

public class Clinical {

    int currentPeople;
    int maximumPeople;

    public boolean condition(int age, boolean agreed) {
        if (
                currentPeople < maximumPeople
                && age >= 19
                && age <= 55
                && agreed
        ) {
            return true;
        }

        return false;
    }

    public void setMaximumPeople(int maximumPeople) {
        this.maximumPeople = maximumPeople;
    }

    public void setCurrentPeople(int currentPeople) {
        this.currentPeople = currentPeople;
    }
}
