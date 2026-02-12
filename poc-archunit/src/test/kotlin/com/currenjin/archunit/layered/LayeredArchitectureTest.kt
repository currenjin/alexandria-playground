package com.currenjin.archunit.layered

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import com.tngtech.archunit.library.Architectures.layeredArchitecture
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Layered Architecture")
class LayeredArchitectureTest {

    private val classes = ClassFileImporter().importPackages("com.currenjin.archunit.layered")

    @Nested
    @DisplayName("layeredArchitecture() DSL")
    inner class LayeredArchitectureDsl {

        @Test
        fun `레이어 아키텍처 규칙을 만족해야 한다`() {
            val rule = layeredArchitecture()
                .consideringAllDependencies()
                .layer("Controller").definedBy("..layered.controller..")
                .layer("Service").definedBy("..layered.service..")
                .layer("Repository").definedBy("..layered.repository..")
                .layer("Domain").definedBy("..layered.domain..")
                .layer("Infrastructure").definedBy("..layered.infrastructure..")
                .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
                .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller")
                .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service", "Infrastructure")
                .whereLayer("Domain").mayOnlyBeAccessedByLayers(
                    "Controller", "Service", "Repository", "Infrastructure",
                )

            rule.check(classes)
        }
    }

    @Nested
    @DisplayName("레이어 간 역방향 의존 금지")
    inner class NoReverseDependency {

        @Test
        fun `Service는 Controller에 의존하지 않아야 한다`() {
            noClasses()
                .that().resideInAPackage("..layered.service..")
                .should().dependOnClassesThat().resideInAPackage("..layered.controller..")
                .check(classes)
        }

        @Test
        fun `Repository는 Controller에 의존하지 않아야 한다`() {
            noClasses()
                .that().resideInAPackage("..layered.repository..")
                .should().dependOnClassesThat().resideInAPackage("..layered.controller..")
                .check(classes)
        }

        @Test
        fun `Repository는 Service에 의존하지 않아야 한다`() {
            noClasses()
                .that().resideInAPackage("..layered.repository..")
                .should().dependOnClassesThat().resideInAPackage("..layered.service..")
                .check(classes)
        }

        @Test
        fun `Infrastructure는 Controller에 의존하지 않아야 한다`() {
            noClasses()
                .that().resideInAPackage("..layered.infrastructure..")
                .should().dependOnClassesThat().resideInAPackage("..layered.controller..")
                .check(classes)
        }

        @Test
        fun `Infrastructure는 Service에 의존하지 않아야 한다`() {
            noClasses()
                .that().resideInAPackage("..layered.infrastructure..")
                .should().dependOnClassesThat().resideInAPackage("..layered.service..")
                .check(classes)
        }
    }

    @Nested
    @DisplayName("Domain 레이어 독립성")
    inner class DomainIndependence {

        @Test
        fun `Domain은 다른 레이어에 의존하지 않아야 한다`() {
            noClasses()
                .that().resideInAPackage("..layered.domain..")
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                    "..layered.controller..",
                    "..layered.service..",
                    "..layered.repository..",
                    "..layered.infrastructure..",
                )
                .check(classes)
        }

        @Test
        fun `Domain은 자기 자신과 표준 라이브러리에만 의존해야 한다`() {
            classes()
                .that().resideInAPackage("..layered.domain..")
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(
                    "..layered.domain..",
                    "java..",
                    "kotlin..",
                    "org.jetbrains..",
                )
                .check(classes)
        }
    }

    @Nested
    @DisplayName("Controller 레이어 접근 규칙")
    inner class ControllerAccessRule {

        @Test
        fun `Controller는 Service에만 의존해야 한다 (Repository 직접 접근 금지)`() {
            noClasses()
                .that().resideInAPackage("..layered.controller..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..layered.repository..", "..layered.infrastructure..")
                .check(classes)
        }
    }
}
