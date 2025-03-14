package com.currenjin.army.organization;

import com.currenjin.army.command.Defcon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrganizationTest {


    @Nested
    class BaseOrganizationTest {
        Organization organization;

        @BeforeEach
        void setUp() {
            organization = new BaseOrganization();
        }

        @Test
        void constructor() {
            assertThat(organization.getCommunicationStatus()).isEqualTo(CommunicationStatus.LIMITED);
        }

        @Test
        void defcon_5() {
            organization.command(Defcon.LEVEL_5);

            assertThat(organization.getCommunicationStatus()).isEqualTo(CommunicationStatus.NORMAL);
        }
    }
}