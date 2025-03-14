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

        @Test
        void defcon_4() {
            organization.command(Defcon.LEVEL_4);

            assertThat(organization.getCommunicationStatus()).isEqualTo(CommunicationStatus.LIMITED);
        }

        @Test
        void defcon_3() {
            organization.command(Defcon.LEVEL_3);

            assertThat(organization.getCommunicationStatus()).isEqualTo(CommunicationStatus.ENCRYPTED);
        }

        @Test
        void defcon_2() {
            organization.command(Defcon.LEVEL_2);

            assertThat(organization.getCommunicationStatus()).isEqualTo(CommunicationStatus.ENCRYPTED);
        }

        @Test
        void defcon_1() {
            organization.command(Defcon.LEVEL_1);

            assertThat(organization.getCommunicationStatus()).isEqualTo(CommunicationStatus.FALLBACK_READY);
        }
    }
}