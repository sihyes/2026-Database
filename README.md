## Database 팀 프로젝트

### 폴더 구조
```
src/
├── com.delivery.Main.java                  # 진입점, 메인 메뉴 루프
│
├── db/
│   └── DBConnection.java      # DB 연결 관리
│
├── menu/                      # 각 메뉴 기능 담당
│   ├── InsertMenu.java        # INSERT 2개
│   ├── SelectMenu.java        # SELECT 4개
│   ├── UpdateMenu.java        # UPDATE 2개 (트랜잭션)
│   └── DeleteMenu.java        # DELETE 2개
│
└── util/
    └── PrintUtil.java         # 표 형식 출력 등 공통 유틸
```
### ERD 다이어그램
https://dbdiagram.io/d/6a0bf7ae697f99c167aae33d
