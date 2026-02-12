package com.currenjin.archunit.hexagonal

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import com.tngtech.archunit.library.Architectures.onionArchitecture
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Hexagonal Architecture")
class HexagonalArchitectureTest {

    private val classes = ClassFileImporter().importPackages("com.currenjin.archunit.hexagonal")

    @Nested
    @DisplayName("onionArchitecture() DSL")
    inner class OnionArchitectureDsl {

        @Test
        fun `헥사고날 아키텍처 규칙을 만족해야 한다`() {
            val rule = onionArchitecture()
                .domainModels("..hexagonal.domain..")
                .applicationServices("..hexagonal.application..")
                .adapter("in", "..hexagonal.adapter.in..")
                .adapter("out", "..hexagonal.adapter.out..")
                .withOptionalLayers(true)

            rule.check(classes)
        }
    }

    @Nested
    @DisplayName("Domain 레이어 독립성")
    inner class DomainIndependence {

        @Test
        fun `Domain은 Application에 의존하지 않아야 한다`() {
            noClasses()
                .that().resideInAPackage("..hexagonal.domain..")
                .should().dependOnClassesThat()
                .resideInAPackage("..hexagonal.application..")
                .check(classes)
        }

        @Test
        fun `Domain은 Adapter에 의존하지 않아야 한다`() {
            noClasses()
                .that().resideInAPackage("..hexagonal.domain..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..hexagonal.adapter..")
                .check(classes)
        }

        @Test
        fun `Domain은 자기 자신과 표준 라이브러리에만 의존해야 한다`() {
            classes()
                .that().resideInAPackage("..hexagonal.domain..")
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(
                    "..hexagonal.domain..",
                    "java..",
                    "kotlin..",
                    "org.jetbrains..",
                )
                .check(classes)
        }
    }

    @Nested
    @DisplayName("Application 레이어 규칙")
    inner class ApplicationLayer {

        @Test
        fun `Application은 Adapter에 의존하지 않아야 한다`() {
            noClasses()
                .that().resideInAPackage("..hexagonal.application..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..hexagonal.adapter..")
                .check(classes)
        }

        @Test
        fun `Application Service는 Port와 Domain에만 의존해야 한다`() {
            classes()
                .that().resideInAPackage("..hexagonal.application.service..")
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(
                    "..hexagonal.application..",
                    "..hexagonal.domain..",
                    "java..",
                    "kotlin..",
                    "org.jetbrains..",
                )
                .check(classes)
        }
    }

    @Nested
    @DisplayName("Adapter 레이어 규칙")
    inner class AdapterLayer {

        @Test
        fun `Inbound Adapter는 Inbound Port를 통해서만 Application에 접근해야 한다`() {
            noClasses()
                .that().resideInAPackage("..hexagonal.adapter.in..")
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                    "..hexagonal.application.service..",
                    "..hexagonal.application.port.out..",
                )
                .check(classes)
        }

        @Test
        fun `Outbound Adapter는 Outbound Port를 통해서만 Application에 접근해야 한다`() {
            noClasses()
                .that().resideInAPackage("..hexagonal.adapter.out..")
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                    "..hexagonal.application.service..",
                    "..hexagonal.application.port.in..",
                )
                .check(classes)
        }

        @Test
        fun `Inbound Adapter와 Outbound Adapter는 서로 의존하지 않아야 한다`() {
            noClasses()
                .that().resideInAPackage("..hexagonal.adapter.in..")
                .should().dependOnClassesThat()
                .resideInAPackage("..hexagonal.adapter.out..")
                .check(classes)

            noClasses()
                .that().resideInAPackage("..hexagonal.adapter.out..")
                .should().dependOnClassesThat()
                .resideInAPackage("..hexagonal.adapter.in..")
                .check(classes)
        }
    }

    @Nested
    @DisplayName("Port 인터페이스 규칙")
    inner class PortRule {

        @Test
        fun `Inbound Port는 Domain에만 의존해야 한다`() {
            classes()
                .that().resideInAPackage("..hexagonal.application.port.in..")
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(
                    "..hexagonal.application.port.in..",
                    "..hexagonal.domain..",
                    "java..",
                    "kotlin..",
                    "org.jetbrains..",
                )
                .check(classes)
        }

        @Test
        fun `Outbound Port는 Domain에만 의존해야 한다`() {
            classes()
                .that().resideInAPackage("..hexagonal.application.port.out..")
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(
                    "..hexagonal.application.port.out..",
                    "..hexagonal.domain..",
                    "java..",
                    "kotlin..",
                    "org.jetbrains..",
                )
                .check(classes)
        }
    }
}
