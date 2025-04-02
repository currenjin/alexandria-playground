package com.currenjin.army.organization;

import com.currenjin.army.command.Defcon;

public interface Organization {
    void command(Defcon defcon);

    CommunicationStatus getCommunicationStatus();
}
