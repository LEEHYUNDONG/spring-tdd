## 프로젝트 개요

Java 21과 Spring Boot 3.5로 구축된 이커머스 마켓플레이스 애플리케이션으로, JWT 인증을 사용하는 판매자(seller)와 구매자(shopper) 사용자 유형을 특징으로 합니다. 이 코드베이스는 TDD 방법론을 따르며 포괄적인 통합 테스트를 포함하고 있습니다.

## 명령어

### 빌드 및 실행
```bash
./gradlew build        # 애플리케이션 빌드
./gradlew bootRun      # 애플리케이션 실행
```

### 테스트
```bash
./gradlew test                                              # 모든 테스트 실행
./gradlew test --tests "ClassName"                         # 특정 테스트 클래스 실행
./gradlew test --tests "ClassName.methodName"              # 특정 테스트 메서드 실행
```

예시:
```bash
./gradlew test --tests "com.demo.book.springtdd.integration.seller.singup.POST_specs"
```

## 아키텍처

이 애플리케이션은 CQRS 방식의 커맨드/쿼리 분리와 함께 **헥사고날 아키텍처(Ports and Adapters)**를 구현합니다.

### 레이어 구조

```
src/main/java/com/demo/book/springtdd/
├── domain/                      # 도메인 엔티티와 비즈니스 로직
│   ├── Seller.java, Shopper.java, Product.java (JPA 엔티티)
│   ├── model/
│   │   ├── command/            # 커맨드 실행자 (쓰기 작업)
│   │   └── query/              # 쿼리 프로세서 (읽기 작업)
│   └── exception/              # 예외 핸들러
├── application/                # 유스케이스
│   ├── usecase/               # 비즈니스 로직 오케스트레이션
│   └── port/
│       ├── in/                # 인바운드 포트 (애플리케이션이 할 수 있는 것)
│       └── out/               # 아웃바운드 포트 (애플리케이션이 필요로 하는 것)
├── adapter/
│   ├── in/                    # 인바운드 어댑터
│   │   ├── controller/        # REST 엔드포인트
│   │   ├── dto/
│   │   │   ├── command/       # 쓰기 작업 DTO
│   │   │   ├── query/         # 읽기 작업 DTO
│   │   │   ├── view/          # 응답 DTO
│   │   │   └── result/        # 결과 래퍼
│   │   └── support/           # 검증 유틸리티
│   └── out/
│       └── persistence/       # 아웃바운드 어댑터 (JPA 리포지토리)
├── infrastructure/            # Security, beans 설정
└── config/                    # 애플리케이션 전용 설정
```

### 핵심 아키텍처 컨셉

**1. 헥사고날 아키텍처 플로우:**
- Controllers (어댑터) → Use Cases (애플리케이션) → Domain Logic → Repositories (어댑터)
- 도메인은 인프라 관심사로부터 격리됨
- 의존성은 도메인을 향해 안쪽으로 향함

**2. 포트와 어댑터 패턴:**
- **인바운드 포트**: `application/port/in/`에 있는 유스케이스 계약을 정의하는 인터페이스 (예: `ForCreatingSeller`)
- **아웃바운드 포트**: `application/port/out/`에 있는 영속성 계약을 정의하는 인터페이스 (예: `CreateSellerPort`, `ReadSellerPort`)
- **어댑터**: 구체적인 구현
  - 인바운드: `adapter/in/controller/`의 컨트롤러
  - 아웃바운드: `adapter/out/persistence/`의 영속성 어댑터 (예: `SellerCommandAdapter`, `SellerQueryAdapter`)

**3. 커맨드/쿼리 분리:**
- **커맨드** (`domain/model/command/`): 상태를 변경하는 비즈니스 작업 실행
  - 예시: `RegisterProductCommandExecutor`는 커맨드 검증, 엔티티 생성, 주입된 consumer를 통한 저장 수행
  - 커맨드 DTO를 받아 검증 수행 후 상태 변경
- **쿼리** (`domain/model/query/`): 읽기를 위한 데이터 조회 및 변환
  - 예시: `GetProductPageQueryProcessor`는 커서 기반 continuation token으로 페이지네이션 처리
  - 복잡한 쿼리에는 `EntityManager` 사용, view DTO로 매핑
- DTO도 이러한 분리를 반영하여 `adapter/in/dto/command/`와 `adapter/in/dto/query/`로 나뉨

**4. 도메인 엔티티 패턴:**
- JPA 엔티티는 **이중 ID 전략**을 사용:
  - `dataKey` (Long): 데이터베이스 작업 및 커서 기반 페이지네이션을 위한 자동 증가 기본 키
  - `id` (UUID): API에 노출되는 비즈니스 식별자
- 이는 DB 관심사를 도메인 식별자로부터 분리함

### 인증 및 보안

- Spring Security OAuth2 Resource Server를 사용한 JWT 기반 인증
- 두 가지 사용자 스코프: `seller`와 `shopper`
- `application.yaml`의 설정:
  ```yaml
  security.jwt.secret: <secret>
  security.jwt.expiration-hours: 24
  security.jwt.issuer: spring-tdd-book
  ```
- `infrastructure/SecurityConfig.java`의 보안 규칙:
  - Public: `/seller/signUp`, `/seller/issueToken`, `/shopper/signUp`, `/shopper/issueToken`
  - Protected: `/seller/**`는 `seller` 스코프 필요, `/shopper/**`는 `shopper` 스코프 필요
- 비밀번호 인코딩: PBKDF2 (Spring Security v5.8 기본값)

### 테스트 아키텍처

**테스트 구조:**
```
src/test/java/com/demo/book/springtdd/
├── integration/               # 엔드포인트 경로와 HTTP 메서드로 조직됨
│   ├── seller/
│   │   ├── signup/POST_specs.java
│   │   ├── products/GET_specs.java, POST_specs.java
│   │   └── ...
│   ├── shopper/
│   └── utils/                # 테스트 데이터 생성기
└── support/                  # 테스트 인프라
    ├── testfixture/TestFixture.java
    └── utils/ApiTest.java, assertions
```

**핵심 테스트 컴포넌트:**

1. **@ApiTest 어노테이션** (`support/utils/ApiTest.java`):
   - 통합 테스트를 위한 메타 어노테이션
   - `RANDOM_PORT`로 Spring Boot 시작
   - `TestFixtureConfiguration`과 `PasswordEncoderConfiguration` 로드

2. **TestFixture** (`support/testfixture/TestFixture.java`):
   - 사전 설정된 `TestRestTemplate`을 가진 중앙 테스트 헬퍼
   - 사용자 작업: `createSeller()`, `createShopper()`, `issueSellerToken()`, `issueShopperToken()`
   - 빠른 설정: `createSellerThenSetAsDefaultUser()`는 사용자 생성 후 자동으로 인증 헤더 설정
   - 상품 작업: `registerProduct()`, `registerProducts(count)`, `deleteAllProducts()`
   - JWT 유틸리티: `parseTokenClaims()`, `getTokenSubject()`, `getTokenScope()` 등
   - 테스트 데이터: `integration/utils/`의 생성기 사용 (EmailGenerator, UsernameGenerator 등)

3. **테스트 관례:**
   - 테스트는 동작을 설명하는 한글 메서드명 사용 (예: `올바르게_요청하면_204_No_Content_상태코드를_반환한다`)
   - `@DisplayName`은 엔드포인트 표시 (예: "POST /seller/signUp")
   - AAA 패턴 따름: `//arrange`, `//act`, `//assert` 주석
   - 테스트 메서드 파라미터로 의존성 주입: `@Autowired TestRestTemplate client`, `@Autowired TestFixture fixture`

**테스트 패턴 예시:**
```java
@ApiTest
@DisplayName("POST /seller/signUp")
public class POST_specs {
    @Test
    void 올바르게_요청하면_204_No_Content_상태코드를_반환한다(
        @Autowired TestRestTemplate client
    ) {
        //arrange
        var command = new CreateSellerCommand(generateEmail(), ...);

        //act
        ResponseEntity<Void> response = client.postForEntity("/seller/signUp", command, Void.class);

        //assert
        assertThat(response.getStatusCode().value()).isEqualTo(204);
    }
}
```

## 개발 패턴

### 새로운 기능 추가하기

1. **커맨드/쿼리 분리 준수**: 쓰기 작업은 `command/`에, 읽기는 `query/`에 배치
2. **포트 먼저 정의**: `application/port/in/`에 인바운드 포트 인터페이스 생성, `application/port/out/`에 아웃바운드 포트 생성
3. **유스케이스 구현**: `application/usecase/`에 생성, 아웃바운드 포트 주입
4. **어댑터 생성**:
   - `adapter/in/controller/`에 컨트롤러 생성
   - `adapter/out/persistence/`에 영속성 어댑터 생성 (쓰기용 CommandAdapter, 읽기용 QueryAdapter)
5. **통합 테스트 먼저 작성** (TDD): `integration/{endpoint-path}/{HTTP_METHOD}_specs.java`에 테스트 클래스 생성

### 검증 패턴

- 컨트롤러에서 `adapter/in/support/UserPropertyValidator`의 정적 검증기를 사용한 입력 검증
- 커맨드 실행자에서 도메인 검증 (예: `RegisterProductCommandExecutor.isValidUri()`의 URI 검증)
- 검증 실패 시 `400 Bad Request` 반환
- 예외 처리를 위해 `domain/exception/`의 `@RestControllerAdvice` 사용

### 페이지네이션 패턴

- continuation token을 사용한 커서 기반 페이지네이션
- `dataKey` (Long)가 효율적인 keyset 페이지네이션을 위한 커서 역할
- Base64를 사용한 커서 인코딩/디코딩
- `GetProductPageQueryProcessor`의 예시: pageSize + 1 조회, 마지막 `dataKey`를 다음 토큰으로 인코딩

### 데이터베이스

- H2 인메모리 데이터베이스
- Spring Data 리포지토리를 사용하는 JPA 엔티티
- 비즈니스 필드(email, username)에 유니크 제약조건
- 비즈니스 작업에는 `findById(UUID)` 사용, 페이지네이션에는 `dataKey` 사용

## API 참조 요약

curl 예시를 포함한 완전한 API 문서는 README.md를 참조하세요. 주요 엔드포인트:

**판매자:**
- POST `/seller/signUp` - 판매자 회원가입 (email, username, password, contactEmail)
- POST `/seller/issueToken` - JWT 토큰 발급
- GET `/seller/me` - 판매자 정보 조회 (인증 필요)
- POST `/seller/products` - 상품 등록 (seller 스코프 필요)
- GET `/seller/products` - 판매자의 상품 목록 조회
- GET `/seller/product/{id}` - 상품 상세 조회
- PUT `/seller/me/email` - 문의 이메일 변경

**구매자:**
- POST `/shopper/signUp` - 구매자 회원가입
- POST `/shopper/issueToken` - JWT 토큰 발급
- GET `/shopper/me` - 구매자 정보 조회 (인증 필요)
- GET `/shopper/products` - 상품 탐색 (페이지네이션, continuationToken 쿼리 파라미터 사용)
