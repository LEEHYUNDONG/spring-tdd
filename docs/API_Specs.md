# API 명세

## 인증 관련

### 판매자 회원가입
```bash
curl -d '{"email":"seller1@example.com", "username":"seller1", "password":"seller1password", "contactEmail":"contact@example.com"}' \
-H "Content-Type: application/json" \
-X POST http://localhost:8080/seller/signUp
```

**응답**: `204 No Content`

**검증 규칙**:
- 이메일 주소는 유일해야 함
- 사용자 이름은 유일해야 함
- 사용자 이름은 3자 이상의 영문자, 숫자, 하이픈, 밑줄로 구성
- 비밀번호는 8자 이상

### 판매자 토큰 발행
```bash
curl -d '{"email":"seller1@example.com", "password":"seller1password"}' \
-H "Content-Type: application/json" \
-X POST http://localhost:8080/seller/issueToken
```

**응답**: `200 OK`
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 구매자 회원가입
```bash
curl -d '{"email":"shopper1@example.com", "username":"shopper1", "password":"shopper1password"}' \
-H "Content-Type: application/json" \
-X POST http://localhost:8080/shopper/signUp
```

**응답**: `204 No Content`

### 구매자 토큰 발행
```bash
curl -d '{"email":"shopper1@example.com", "password":"shopper1password"}' \
-H "Content-Type: application/json" \
-X POST http://localhost:8080/shopper/issueToken
```

**응답**: `200 OK`
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

## 판매자 API

### 판매자 정보 조회
```bash
curl -X GET http://localhost:8080/seller/me \
-H "Authorization: Bearer {accessToken}"
```

**응답**: `200 OK`
```json
{
  "id": "uuid",
  "email": "seller1@example.com",
  "username": "seller1",
  "contactEmail": "contact@example.com"
}
```

### 문의 이메일 변경
```bash
curl -X POST http://localhost:8080/seller/changeContactEmail \
-H "Content-Type: application/json" \
-H "Authorization: Bearer {accessToken}" \
-d '{"contactEmail": "new-contact@example.com"}'
```

**응답**: `204 No Content`

### 상품 등록
```bash
curl -d '{"name":"상품1", "imageUri":"https://example.com/image.jpg", "description":"상품 설명", "priceAmount":10000, "stockQunatity":100}' \
-H "Content-Type: application/json" \
-H "Authorization: Bearer {accessToken}" \
-X POST http://localhost:8080/seller/products
```

**응답**: `201 Created`
- **Location 헤더**: `/seller/products/{productId}`

### 판매자 상품 목록 조회
```bash
curl -X GET http://localhost:8080/seller/products \
-H "Authorization: Bearer {accessToken}"
```

**응답**: `200 OK`
```json
{
  "items": [
    {
      "id": "uuid",
      "name": "상품1",
      "imageUri": "https://example.com/image.jpg",
      "description": "상품 설명",
      "priceAmount": 10000,
      "stockQuantity": 100,
      "registeredTimeUtc": "2025-01-01T00:00:00.000"
    }
  ]
}
```

### 판매자 상품 상세 조회
```bash
curl -X GET http://localhost:8080/seller/products/{productId} \
-H "Authorization: Bearer {accessToken}"
```

**응답**: `200 OK` (상품 상세 정보)

## 구매자 API

### 구매자 정보 조회
```bash
curl -X GET http://localhost:8080/shopper/me \
-H "Authorization: Bearer {accessToken}"
```

**응답**: `200 OK`
```json
{
  "id": "uuid",
  "email": "shopper1@example.com",
  "username": "shopper1"
}
```

### 상품 탐색 (페이지네이션)
```bash
# 첫 페이지
curl -X GET http://localhost:8080/shopper/products \
-H "Authorization: Bearer {accessToken}"

# 다음 페이지
curl -X GET "http://localhost:8080/shopper/products?continuationToken={token}" \
-H "Authorization: Bearer {accessToken}"
```

**응답**: `200 OK`
```json
{
  "items": [
    {
      "id": "uuid",
      "seller": {
        "id": "uuid",
        "username": "seller1"
      },
      "name": "상품1",
      "imageUri": "https://example.com/image.jpg",
      "description": "상품 설명",
      "priceAmount": 10000,
      "stockQuantity": 100
    }
  ],
  "contunuationToken": "base64-encoded-token"
}
```
