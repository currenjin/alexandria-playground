package com.currenjin.army.organization;

import com.currenjin.army.command.Defcon;

public class BaseOrganization implements Organization {
    private CommunicationStatus communicationStatus = CommunicationStatus.LIMITED;

    @Override
    public void command(Defcon defcon) {
        switch (defcon) {
            case LEVEL_5 -> this.communicationStatus = CommunicationStatus.NORMAL;
            default -> throw new IllegalStateException("Unexpected value: " + defcon);
        }
    }

    @Override
    public CommunicationStatus getCommunicationStatus() {
        return this.communicationStatus;
    }
}
