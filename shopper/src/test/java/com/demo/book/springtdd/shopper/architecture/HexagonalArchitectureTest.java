package com.demo.book.springtdd.shopper.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

class HexagonalArchitectureTest {

    private static final String BASE_PACKAGE = "com.demo.book.springtdd.shopper";
    private static JavaClasses classes;

    @BeforeAll
    static void setUp() {
        classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(BASE_PACKAGE);
    }

    @Nested
    @DisplayName("레이어 의존성 규칙")
    class LayerDependencyRules {

        @Test
        @DisplayName("도메인은 외부 레이어에 의존하지 않는다")
        void domain_should_not_depend_on_outer_layers() {
            ArchRule rule = noClasses()
                    .that().resideInAPackage("..domain..")
                    .should().dependOnClassesThat()
                    .resideInAnyPackage("..adapter..", "..application..");

            rule.check(classes);
        }

        @Test
        @DisplayName("application은 adapter에 의존하지 않는다")
        void application_should_not_depend_on_adapters() {
            ArchRule rule = noClasses()
                    .that().resideInAPackage("..application..")
                    .should().dependOnClassesThat()
                    .resideInAPackage("..adapter..");

            rule.check(classes);
        }

        @Test
        @DisplayName("헥사고날 아키텍처 레이어 검증")
        void hexagonal_architecture_is_respected() {
            ArchRule rule = layeredArchitecture()
                    .consideringAllDependencies()
                    .layer("Domain").definedBy("..domain..")
                    .layer("Application").definedBy("..application..")
                    .layer("AdapterIn").definedBy("..adapter.in..")
                    .layer("AdapterOut").definedBy("..adapter.out..")
                    .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application", "AdapterIn", "AdapterOut")
                    .whereLayer("Application").mayOnlyBeAccessedByLayers("AdapterIn", "AdapterOut")
                    .whereLayer("AdapterIn").mayNotBeAccessedByAnyLayer()
                    .whereLayer("AdapterOut").mayNotBeAccessedByAnyLayer();

            rule.check(classes);
        }
    }

    @Nested
    @DisplayName("네이밍 컨벤션")
    class NamingConventionRules {

        @Test
        @DisplayName("Controller 클래스는 controller 패키지에 있어야 한다")
        void controllers_should_be_in_controller_package() {
            ArchRule rule = classes()
                    .that().haveSimpleNameEndingWith("Controller")
                    .should().resideInAPackage("..controller..");

            rule.check(classes);
        }

        @Test
        @DisplayName("Usecase 클래스는 usecase 패키지에 있어야 한다")
        void usecases_should_be_in_usecase_package() {
            ArchRule rule = classes()
                    .that().haveSimpleNameEndingWith("Usecase")
                    .should().resideInAPackage("..port.in..");

            rule.check(classes);
        }

        @Test
        @DisplayName("Repository 인터페이스는 repository 패키지에 있어야 한다")
        void repositories_should_be_in_repository_package() {
            ArchRule rule = classes()
                    .that().haveSimpleNameEndingWith("Repository")
                    .should().resideInAPackage("..repository..");

            rule.check(classes);
        }

        @Test
        @DisplayName("Port 인터페이스는 port 패키지에 있어야 한다")
        void ports_should_be_in_port_package() {
            ArchRule rule = classes()
                    .that().haveSimpleNameEndingWith("Port")
                    .should().resideInAPackage("..port..");

            rule.check(classes);
        }
    }

    @Nested
    @DisplayName("순환 참조 방지")
    class CyclicDependencyRules {

        @Test
        @DisplayName("패키지 간 순환 참조가 없어야 한다")
        void no_cyclic_dependencies_between_packages() {
            ArchRule rule = com.tngtech.archunit.library.dependencies.SlicesRuleDefinition
                    .slices()
                    .matching(BASE_PACKAGE + ".(*)..")
                    .should().beFreeOfCycles();

            rule.check(classes);
        }
    }
}
