# Project Convention

## 모듈 구조

```
spring-tdd/
├── core/                    # 공통 모듈 (보안, 유틸리티, 예외 처리)
├── seller/                  # 판매자 도메인 모듈
├── shopper/                 # 구매자 도메인 모듈
├── product/                 # 상품 도메인 모듈
├── order/                   # 주문 도메인 모듈
└── docs/                    # 문서
```

### 모듈 의존성

```
seller, shopper, product, order
           ↓
         core
```

- 모든 도메인 모듈은 `core` 모듈에 의존
- 도메인 모듈 간에는 직접 의존하지 않음

---

## 패키지 구조 (헥사고날 아키텍처)

각 도메인 모듈은 다음 패키지 구조를 따릅니다:

```
{module}/src/main/java/com/demo/book/springtdd/{module}/
├── domain/                     # 도메인 엔티티
├── adapter/
│   ├── in/
│   │   ├── controller/        # REST 컨트롤러
│   │   └── dto/
│   │       ├── command/       # 쓰기 작업 DTO
│   │       ├── query/         # 읽기 작업 DTO
│   │       ├── view/          # 응답 DTO
│   │       └── result/        # 결과 래퍼
│   └── out/
│       └── persistence/       # 영속성 어댑터
│           └── repository/    # JPA 리포지토리
├── application/
│   ├── port/
│   │   ├── in/               # 인바운드 포트 (유스케이스 인터페이스)
│   │   └── out/              # 아웃바운드 포트 (영속성 인터페이스)
│   └── usecase/              # 유스케이스 구현
└── {Module}Application.java   # Spring Boot 애플리케이션 진입점
```

---

## 네이밍 컨벤션

### 클래스명

| 유형 | 패턴 | 예시 |
|------|------|------|
| 엔티티 | `{Name}` | `Seller`, `Product` |
| 컨트롤러 | `{Name}{Action}Controller` | `SellerSignUpController` |
| 유스케이스 | `{Name}{Action}Usecase` | `SellerSignUpUsecase` |
| 인바운드 포트 | `For{Action}{Name}` | `ForCreatingSeller` |
| 아웃바운드 포트 | `{Action}{Name}Port` | `CreateSellerPort`, `ReadSellerPort` |
| 커맨드 DTO | `{Action}{Name}Command` | `CreateSellerCommand` |
| 쿼리 DTO | `{Action}{Name}` / `Issue{Name}Token` | `IssueSellerToken` |
| 뷰 DTO | `{Name}View` / `{Name}MeView` | `SellerView`, `SellerMeView` |
| 영속성 어댑터 | `{Name}CommandAdapter` / `{Name}QueryAdapter` | `SellerCommandAdapter` |

### 테스트 클래스명

| 유형 | 패턴 | 예시 |
|------|------|------|
| 통합 테스트 | `{HTTP_METHOD}_specs` | `POST_specs.java`, `GET_specs.java` |

---

## 테스트 구조

### 테스트 디렉토리 구조

```
{module}/src/test/java/com/demo/book/springtdd/{module}/
├── integration/                  # 통합 테스트
│   ├── signup/
│   │   └── POST_specs.java
│   ├── issuetoken/
│   │   └── POST_specs.java
│   └── me/
│       └── GET_specs.java
└── support/                      # 테스트 지원 클래스
    ├── IntegrationTest.java      # 통합 테스트 어노테이션
    ├── TestFixture.java          # 테스트 픽스처
    ├── TestFixtureConfiguration.java
    └── JwtAssertions.java        # JWT 검증 유틸리티
```

### 공통 테스트 유틸리티 (core 모듈)

```
core/src/testFixtures/java/com/demo/book/springtdd/testutils/
├── EmailGenerator.java
├── UsernameGenerator.java
├── PasswordGenerator.java
└── TestDatasource.java
```

### 테스트 어노테이션

```java
@IntegrationTest
@DisplayName("POST /seller/signUp")
public class POST_specs {
    // ...
}
```

- `@IntegrationTest`: 통합 테스트를 위한 메타 어노테이션
  - Spring Boot 테스트 컨텍스트 로드
  - `RANDOM_PORT`로 웹 서버 시작
  - `TestFixtureConfiguration` 포함

### 테스트 메서드 컨벤션

```java
@Test
void 올바르게_요청하면_204_No_Content_상태코드를_반환한다(@Autowired TestRestTemplate client) {
    //arrange
    var command = new CreateSellerCommand(...);

    //act
    ResponseEntity<Void> response = client.postForEntity("/seller/signUp", command, Void.class);

    //assert
    assertThat(response.getStatusCode().value()).isEqualTo(204);
}
```

- 메서드명: 한글로 행위를 설명
- AAA 패턴: `//arrange`, `//act`, `//assert` 주석 사용
- 의존성 주입: 메서드 파라미터로 `@Autowired` 사용

---

## Gradle 설정

### 도메인 모듈 build.gradle

```groovy
plugins {
    id 'org.springframework.boot'
}

dependencies {
    implementation project(':core')
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.4'
    runtimeOnly 'com.h2database:h2'

    testImplementation project(':core')
    testImplementation(testFixtures(project(':core')))
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}

bootJar {
    mainClass = 'com.demo.book.springtdd.{module}.{Module}Application'
}
```

### core 모듈 build.gradle

```groovy
plugins {
    id 'java-library'
    id 'java-test-fixtures'  // testFixtures 기능 활성화
}

dependencies {
    api 'org.springframework.boot:spring-boot-starter-web'
    api 'org.springframework.boot:spring-boot-starter-security'
    // ...

    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

---

## API 엔드포인트 컨벤션

### URL 패턴

| 패턴 | 용도 | 예시 |
|------|------|------|
| `/{role}/signUp` | 회원가입 | `POST /seller/signUp` |
| `/{role}/issueToken` | 토큰 발급 | `POST /seller/issueToken` |
| `/{role}/me` | 내 정보 조회 | `GET /seller/me` |
| `/{role}/products` | 상품 목록 | `GET /seller/products` |
| `/{role}/product/{id}` | 상품 상세 | `GET /seller/product/{id}` |

### HTTP 상태 코드

| 상태 코드 | 용도 |
|----------|------|
| `200 OK` | 조회 성공, 토큰 발급 성공 |
| `204 No Content` | 생성/수정 성공 (응답 본문 없음) |
| `400 Bad Request` | 유효성 검사 실패, 비즈니스 규칙 위반 |
| `401 Unauthorized` | 인증 실패 |
| `403 Forbidden` | 권한 없음 |

---

## 엔티티 컨벤션

### 이중 ID 전략

```java
@Entity
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dataKey;           // DB 내부용 (페이지네이션, 성능)

    @Column(unique = true)
    private UUID id;                // 비즈니스 식별자 (API 노출)

    // ...
}
```

- `dataKey`: 자동 증가 기본 키 (DB 작업, 커서 기반 페이지네이션용)
- `id`: UUID 비즈니스 식별자 (API에 노출)

---

## 커맨드/쿼리 분리 (CQRS)

### Command (쓰기 작업)

- 위치: `adapter/in/dto/command/`
- 역할: 상태 변경 요청
- 예시: `CreateSellerCommand`, `RegisterProductCommand`

### Query (읽기 작업)

- 위치: `adapter/in/dto/query/`
- 역할: 데이터 조회 요청
- 예시: `IssueSellerToken`

### View (응답)

- 위치: `adapter/in/dto/view/`
- 역할: API 응답 데이터
- 예시: `SellerMeView`, `ProductView`
