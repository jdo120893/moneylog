# dietlog REST API 명세서

## 1. 인증 (Auth)
| 메서드 | 경로 | 설명 | 요청 바디 | 응답 | 인증 |
|---|---|---|---|---|---|
| POST | /api/auth/signup | 회원가입 | {email, password, nickname} | 201 · 생성된 사용자 요약 | ❌ |
| POST | /api/auth/login | 로그인(JWT 발급) | {email, password} | 200 · {accessToken} | ❌ |

## 2. 카테고리 (Category)
| 메서드 | 경로 | 설명 | 요청 바디 | 응답 | 인증 |
|---|---|---|---|---|---|
| GET | /api/categories | 내 카테고리 목록 | — | 200 · [{id, name, mealType, foodGroup}] | ✅ |
| POST | /api/categories | 카테고리 추가 | {name, mealType, foodGroup} | 201 · 생성된 카테고리 | ✅ |
| PUT | /api/categories/{id} | 카테고리 수정 | {name, mealType, foodGroup} | 200 · 수정된 카테고리 | ✅ |
| DELETE | /api/categories/{id} | 카테고리 삭제 | — | 204 / 200 | ✅ |

- `mealType`은 BREAKFAST/LUNCH/DINNER/SNACK (ENUM)
- `foodGroup`은 한식/양식/분식/디저트 등 자유 문자열

## 3. 식단 기록 (FoodLog)
| 메서드 | 경로 | 설명 | 요청 바디 | 응답 | 인증 |
|---|---|---|---|---|---|
| GET | /api/foodlogs?yearMonth=2026-07&mealType=LUNCH&categoryId=3&page=0&size=20 | 목록(월/끼니/카테고리 필터 + 페이징) | — | 200 · 페이지 응답 | ✅ |
| POST | /api/foodlogs | 식단 기록 등록 | {mealType, calorie, categoryId, memo, logDate} | 201 · 생성된 기록 | ✅ |
| GET | /api/foodlogs/{id} | 기록 상세 | — | 200 · 기록 1건 | ✅ |
| PUT | /api/foodlogs/{id} | 기록 수정 | {mealType, calorie, categoryId, memo, logDate} | 200 · 수정된 기록 | ✅ |
| DELETE | /api/foodlogs/{id} | 기록 삭제 | — | 204 / 200 | ✅ |

- 쿼리 파라미터는 모두 선택. 없으면 전체(또는 기본값 page=0&size=20) 반환
- `calorie`는 long(kcal 단위), `logDate`는 LocalDate("2026-07-08") 형식

## 4. 통계 (Statistics)
| 메서드 | 경로 | 설명 | 요청 바디 | 응답 | 인증 |
|---|---|---|---|---|---|
| GET | /api/statistics/monthly?yearMonth=2026-07 | 월별 통계 | — | 200 · {totalCalorie, byMealType:[{mealType, total}], byFoodGroup:[{foodGroup, total}]} | ✅ |

## 5. (도전) 목표 칼로리 / 검색 / 내보내기
| 메서드 | 경로 | 설명 | 요청/응답 요약 | 인증 |
|---|---|---|---|---|
| GET | /api/goals?yearMonth=2026-07 | 월 목표 조회 | [{categoryId, targetCalorie}] | ✅ |
| POST | /api/goals | 목표 설정 | {categoryId, yearMonth, targetCalorie} | ✅ |
| GET | /api/foodlogs/search?keyword=닭가슴살&from=2026-07-01&to=2026-07-31&minCalorie=100&maxCalorie=800 | 기록 검색(키워드/기간/칼로리범위) | 페이지 응답 | ✅ |
| GET | /api/foodlogs/export?yearMonth=2026-07 | CSV 내보내기 | text/csv 파일 | ✅ |

---

## 6. 요청/응답 예시

### 로그인 — POST /api/auth/login
요청:
```json
{
  "email": "hong@dietlog.com",
  "password": "pass1234!"
}
```
응답 (200 OK):
```json
{
  "success": true,
  "message": "로그인에 성공했습니다.",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiZXhwIjoxNzUxOTk5OTk5fQ.abc123def456"
  }
}
```

### 식단 기록 등록 — POST /api/foodlogs
요청 (헤더에 Authorization: Bearer ... 포함):
```json
{
  "mealType": "LUNCH",
  "calorie": 650,
  "categoryId": 3,
  "memo": "제육볶음 정식",
  "logDate": "2026-07-08"
}
```
응답 (201 Created):
```json
{
  "success": true,
  "message": "식단 기록이 등록되었습니다.",
  "data": {
    "id": 42,
    "mealType": "LUNCH",
    "calorie": 650,
    "categoryId": 3,
    "categoryName": "한식",
    "memo": "제육볶음 정식",
    "logDate": "2026-07-08",
    "createdAt": "2026-07-08T12:31:05"
  }
}
```

### 식단 기록 목록 — GET /api/foodlogs?yearMonth=2026-07&page=0&size=20
응답 (200 OK):
```json
{
  "success": true,
  "message": "식단 기록 목록을 조회했습니다.",
  "data": {
    "foodLogs": [
      {
        "id": 42,
        "mealType": "LUNCH",
        "calorie": 650,
        "categoryId": 3,
        "categoryName": "한식",
        "memo": "제육볶음 정식",
        "logDate": "2026-07-08"
      },
      {
        "id": 41,
        "mealType": "BREAKFAST",
        "calorie": 320,
        "categoryId": 7,
        "categoryName": "양식",
        "memo": "토스트, 계란후라이",
        "logDate": "2026-07-08"
      }
    ]
  },
  "meta": {
    "pagination": {
      "page": 0,
      "size": 20,
      "totalItems": 42,
      "totalPages": 3,
      "hasNext": true,
      "hasPrev": false
    }
  }
}
```

### 월별 통계 — GET /api/statistics/monthly?yearMonth=2026-07
응답 (200 OK):
```json
{
  "success": true,
  "message": "월별 통계를 조회했습니다.",
  "data": {
    "totalCalorie": 62500,
    "byMealType": [
      { "mealType": "BREAKFAST", "total": 12000 },
      { "mealType": "LUNCH", "total": 22000 },
      { "mealType": "DINNER", "total": 20500 },
      { "mealType": "SNACK", "total": 8000 }
    ],
    "byFoodGroup": [
      { "foodGroup": "한식", "total": 35000 },
      { "foodGroup": "양식", "total": 15000 },
      { "foodGroup": "분식", "total": 12500 }
    ]
  }
}
```

---

## 7. 공통 응답 규약

### 인증 헤더

- 헤더가 없거나 토큰이 만료되면 → 401 Unauthorized
- 토큰은 유효하지만 남의 자원을 건드리면 → 403 Forbidden

### 성공 응답 봉투
```json
{
  "success": true,
  "message": "...에 성공했습니다.",
  "data": { }
}
```

### 에러 응답 봉투
```json
{
  "success": false,
  "code": "FOODLOG_NOT_FOUND",
  "message": "식단 기록을 찾을 수 없습니다.",
  "data": null
}
```

### 표준 에러 코드
| HTTP 상태 | code | 상황 |
|---|---|---|
| 400 | VALIDATION_ERROR | 입력 검증 실패(칼로리 ≤ 0, 날짜 누락 등) |
| 401 | INVALID_CREDENTIALS | 로그인 시 이메일/비밀번호 불일치 |
| 401 | UNAUTHORIZED | 토큰 없음/만료 |
| 403 | FORBIDDEN | 본인 데이터가 아님 |
| 409 | DUPLICATE_EMAIL | 이미 가입된 이메일 |
| 404 | CATEGORY_NOT_FOUND | 존재하지 않는 카테고리 |
| 404 | FOODLOG_NOT_FOUND | 존재하지 않는 기록 |

---

## 8. 화면 흐름 및 API 매핑

| 화면 | 사용자 행동 | 호출 API |
|---|---|---|
| ① 로그인 | 회원가입 | POST /api/auth/signup |
| ① 로그인 | 로그인 → 토큰 저장 | POST /api/auth/login |
| ② 기록 목록 | 이번 달 목록 로드 | GET /api/foodlogs?yearMonth=... |
| ② 기록 목록 | 필터용 카테고리 드롭다운 | GET /api/categories |
| ② 기록 목록 | 항목 삭제 | DELETE /api/foodlogs/{id} |
| ③ 기록 등록 | 카테고리 선택지 로드 | GET /api/categories |
| ③ 기록 등록 | 저장(신규) | POST /api/foodlogs |
| ③ 기록 등록 | 저장(수정 시) | PUT /api/foodlogs/{id} |
| ④ 월별 통계 | 이번 달 집계 로드 | GET /api/statistics/monthly?yearMonth=... |

## 9. 개발 우선순위
1. 인증 (signup, login)
2. 카테고리 CRUD
3. 식단 기록 CRUD
4. 목록 필터/페이징
5. 통계
6. 프론트 연동
7. (도전) goals/search/export