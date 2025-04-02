package com.currenjin.army.organization;

import com.currenjin.army.command.Defcon;

public class BaseOrganization implements Organization {
    private CommunicationStatus communicationStatus = CommunicationStatus.LIMITED;

    @Override
    public void command(Defcon defcon) {
        switch (defcon) {
            case LEVEL_5 -> this.communicationStatus = CommunicationStatus.NORMAL;
            case LEVEL_4 -> this.communicationStatus = CommunicationStatus.LIMITED;
            case LEVEL_3, LEVEL_2 -> this.communicationStatus = CommunicationStatus.ENCRYPTED;
            case LEVEL_1 -> this.communicationStatus = CommunicationStatus.FALLBACK_READY;
            default -> throw new IllegalStateException("Unexpected value: " + defcon);
        }
    }

    @Override
    public CommunicationStatus getCommunicationStatus() {
        return this.communicationStatus;
    }
}
