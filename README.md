# Spring TDD 이커머스 프로젝트

TDD 방법론과 헥사고날 아키텍처를 적용한 이커머스 마켓플레이스 애플리케이션입니다.

## 프로젝트 개요

판매자(Seller)와 구매자(Shopper)를 위한 이커머스 플랫폼으로, JWT 인증 기반의 사용자 관리와 상품 등록/조회 기능을 제공합니다.

### 기술 스택

- **언어**: Java 21
- **프레임워크**: Spring Boot 3.5.0
- **인증**: JWT (Spring Security OAuth2 Resource Server)
- **데이터베이스**: H2 (In-memory)
- **ORM**: JPA / Hibernate
- **빌드 도구**: Gradle
- **테스트**: JUnit 5, Spring Boot Test

## 목표

- [x] 켄트백/Test Driven Development 독서
- [x] TDD 관련 강의 수강
- [x] TDD 기반 API 개발
- [x] 통합 테스트 작성 (100% 커버리지)
- [x] 헥사고날 아키텍처 설계
- [ ] 클린 아키텍처 리팩토링 (진행 중 - 30%)
- [ ] UseCase 전면 적용
- [ ] 단위 테스트 추가 작성
- [ ] 불필요한 의존성 제거
- [ ] 코틀린으로 전환

## 아키텍처

### 헥사고날 아키텍처 (Ports and Adapters)

이 프로젝트는 클린 아키텍처의 헥사고날 아키텍처 패턴을 따릅니다:

```
┌─────────────────────────────────────────────────────────┐
│                    Adapter (In)                         │
│              Controllers (REST API)                     │
└───────────────────────┬─────────────────────────────────┘
                        │ depends on
                        ↓
┌─────────────────────────────────────────────────────────┐
│                 Application (Core)                      │
│  ┌──────────────────┐      ┌──────────────────┐         │
│  │  Inbound Ports   │      │   Use Cases      │         │
│  │  (Interfaces)    │←─────│  (Business Logic)│         │
│  └──────────────────┘      └──────────────────┘         │
│                                    │                    │
│                                    ↓ depends on         │
│  ┌──────────────────┐      ┌──────────────────┐         │
│  │ Outbound Ports   │      │     Domain       │         │
│  │  (Interfaces)    │      │   (Entities)     │         │
│  └──────────────────┘      └──────────────────┘         │
└───────────────────────┬─────────────────────────────────┘
                        │ implements
                        ↓
┌─────────────────────────────────────────────────────────┐
│                   Adapter (Out)                         │
│        Persistence (JPA Repositories)                   │
└─────────────────────────────────────────────────────────┘
```

**핵심 원칙:**
- 의존성은 항상 안쪽(도메인)을 향함
- 도메인은 외부 기술에 의존하지 않음
- 인터페이스(Port)를 통한 느슨한 결합

### CQRS 패턴 적용

Command(쓰기)와 Query(읽기) 작업을 명확히 분리:
- **Command**: `domain/model/command/` - 상태 변경 로직
- **Query**: `domain/model/query/` - 데이터 조회 로직

## 리팩토링 진행 상황

### Controller 헥사고날 아키텍처 전환

| Controller | 상태 | Port 사용 | Repository 직접 의존 제거 | 비고 |
|-----------|------|----------|------------------------|------|
| SellerIssueTokenController | ✅ 완료 | ForIssuingSellerToken | ✅ | class 전환 |
| ShopperIssueTokenController | ✅ 완료 | ForIssuingShopperToken | ✅ | class 전환 |
| SellerSignUpController | ✅ 완료 | ForCreatingSeller | ✅ | 불필요한 import 정리 필요 |
| ShopperSignUpController | 🔄 대기 | - | ❌ | record → class 전환 필요 |
| SellerMeController | 🔄 대기 | - | ❌ | UseCase 생성 필요 |
| ShopperMeViewController | 🔄 대기 | - | ❌ | UseCase 생성 필요 |
| SellerChangeContactEmailController | 🔄 대기 | - | ❌ | Command UseCase 필요 |
| SellerProductsController | 🔄 대기 | - | ❌ | Command UseCase 필요 |
| SellerProductViewController | 🔄 대기 | - | ❌ | Query UseCase 필요 |
| ShopperProductsController | 🔄 대기 | - | ❌ | Query UseCase 필요 |

**진행률**: 3/10 (30%)

### UseCase 구현 현황

| UseCase | 상태 | Inbound Port | Outbound Port | 비고 |
|---------|------|--------------|---------------|------|
| SellerSignUpUsecase | ✅ | ForCreatingSeller | CreateSellerPort | HTTP 의존성 제거 필요 |
| SellerIssueTokenUsecase | ✅ | ForIssuingSellerToken | ReadSellerPort | - |
| ShopperIssueTokenUsecase | ✅ | ForIssuingShopperToken | ReadShopperPort | - |
| ShopperSignUpUsecase | 🔄 | - | - | 생성 필요 |
| SellerMeUsecase | 🔄 | - | - | 생성 필요 |
| ShopperMeUsecase | 🔄 | - | - | 생성 필요 |
| ChangeContactEmailUsecase | 🔄 | - | - | 생성 필요 |
| RegisterProductUsecase | 🔄 | - | - | Executor → UseCase 전환 |
| GetSellerProductsUsecase | 🔄 | - | - | Processor → UseCase 전환 |
| GetProductPageUsecase | 🔄 | - | - | Processor → UseCase 전환 |

**진행률**: 3/10 (30%) 

## TDD에 대한 생각

TDD는 꼭 필요한 로직이나 비즈니스 요구사항이 명확한 경우에 큰 효과를 발휘합니다. 모든 코드에 TDD를 적용하는 것은 개발 속도와 유연성 측면에서 Trade-off가 있으므로, 프로젝트 상황에 맞게 선택적으로 적용하는 것이 좋습니다.

## 빌드 및 실행

### 애플리케이션 빌드
```bash
./gradlew build
```

### 애플리케이션 실행
```bash
./gradlew bootRun
```

### 테스트 실행
```bash
./gradlew test                                              # 모든 테스트 실행
./gradlew test --tests "ClassName"                         # 특정 테스트 클래스 실행
./gradlew test --tests "ClassName.methodName"              # 특정 테스트 메서드 실행
```

## API 명세

자세한 API 명세는 [docs/API_Specs.md](docs/API_Specs.md)를 참조하세요.

## 테스트 커버리지

모든 API 엔드포인트에 대한 통합 테스트가 작성되어 있으며, 주요 테스트 시나리오는 다음과 같습니다:

- ✅ 정상 요청 처리
- ✅ 입력 검증 (이메일 형식, 사용자 이름 규칙, 비밀번호 길이)
- ✅ 중복 데이터 검증 (이메일, 사용자 이름)
- ✅ 인증/인가 검증 (JWT 토큰, 스코프)
- ✅ 비즈니스 로직 검증 (비밀번호 암호화, 상품 권한 등)
- ✅ 페이지네이션 동작 확인

## 프로젝트 구조

```
src/main/java/com/demo/book/springtdd/
├── adapter/
│   ├── in/
│   │   ├── controller/          # REST API 컨트롤러
│   │   ├── dto/
│   │   │   ├── command/         # 쓰기 작업 DTO
│   │   │   ├── query/           # 읽기 작업 DTO
│   │   │   ├── view/            # 응답 DTO
│   │   │   └── result/          # 결과 래퍼
│   │   └── support/             # 검증 유틸리티
│   └── out/
│       └── persistence/         # JPA 리포지토리 어댑터
├── application/
│   ├── port/
│   │   ├── in/                  # 인바운드 포트 (유스케이스 인터페이스)
│   │   └── out/                 # 아웃바운드 포트 (영속성 인터페이스)
│   └── usecase/                 # 유스케이스 구현
├── domain/
│   ├── model/
│   │   ├── command/             # 커맨드 실행자
│   │   └── query/               # 쿼리 프로세서
│   ├── exception/               # 도메인 예외
│   └── *.java                   # 도메인 엔티티 (Seller, Shopper, Product)
├── infrastructure/              # 인프라 설정 (Security, JWT)
└── config/                      # 애플리케이션 설정
```

## 다음 단계

1. **남은 Controller 리팩토링** (7개)
   - record → class 전환
   - Repository 직접 의존성 제거
   - UseCase/Port 패턴 적용

2. **UseCase 계층 완성** (7개)
   - 모든 비즈니스 로직을 UseCase로 이동
   - Command/Query Processor를 UseCase로 전환

3. **코드 품질 개선**
   - 불필요한 import 제거
   - HTTP 관심사 분리 (UseCase에서 ResponseEntity 제거)
   - 단위 테스트 추가

4. **Kotlin 전환**
   - Java → Kotlin 마이그레이션
   - data class, sealed class 활용
   - Coroutine 적용 검토

## 라이센스

이 프로젝트는 학습 목적으로 작성되었습니다.