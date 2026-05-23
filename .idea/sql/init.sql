/*1. region*/
INSERT INTO region (region_name, city) VALUES
('강남권', '서울'),
('강북권', '서울'),
('해운대권', '부산'),
('수성권', '대구'),
('서구권', '대전'),
('남구권', '울산'),
('광산권', '광주'),
('영통권', '수원'),
('일산권', '고양'),
('분당권', '성남'),
('동안권', '안양'),
('덕양권', '고양'),
('상록권', '안산'),
('단원권', '안산'),
('팔달권', '수원'),
('처인권', '용인'),
('기흥권', '용인'),
('만안권', '안양'),
('연수권', '인천'),
('부평권', '인천'),
('청라권', '인천'),
('흥덕권', '청주'),
('완산권', '전주'),
('덕진권', '전주'),
('천안권', '천안'),
('동래권', '부산'),
('창원권', '창원'),
('진해권', '창원'),
('포항권', '포항'),
('여수권', '여수');
/*2. customer*/
INSERT INTO customer
(name, age, gender, region_id, current_grade)
VALUES
('김민수', 25, 'M', 1, 'Silver'),
('이서연', 31, 'F', 2, 'Gold'),
('박지훈', 28, 'M', 3, 'Bronze'),
('최유진', 22, 'F', 4, 'Silver'),
('정현우', 35, 'M', 5, 'VIP'),
('한지민', 27, 'F', 6, 'Gold'),
('오세훈', 29, 'M', 7, 'Bronze'),
('윤아린', 24, 'F', 8, 'Silver'),
('강동현', 33, 'M', 9, 'Gold'),
('서다은', 26, 'F', 10, 'Bronze'),
('장민혁', 30, 'M', 11, 'Silver'),
('백수진', 21, 'F', 12, 'Bronze'),
('조성민', 40, 'M', 13, 'VIP'),
('송예린', 23, 'F', 14, 'Gold'),
('임태호', 37, 'M', 15, 'Silver'),
('신가영', 28, 'F', 16, 'Bronze'),
('유재석', 41, 'M', 17, 'VIP'),
('노지은', 25, 'F', 18, 'Silver'),
('권혁준', 32, 'M', 19, 'Gold'),
('문소희', 29, 'F', 20, 'Bronze'),
('하준영', 26, 'M', 21, 'Silver'),
('양서진', 24, 'F', 22, 'Gold'),
('고민재', 36, 'M', 23, 'VIP'),
('배채원', 27, 'F', 24, 'Silver'),
('남도윤', 31, 'M', 25, 'Bronze'),
('차유나', 22, 'F', 26, 'Gold'),
('진현수', 34, 'M', 27, 'Silver'),
('류다인', 23, 'F', 28, 'Bronze'),
('손지호', 38, 'M', 29, 'VIP'),
('마은별', 20, 'F', 30, 'Silver');

/*3. customer_history     ← customer, region 참조*/
INSERT INTO customer_history
(customer_id, old_region_id, new_region_id, old_grade, new_grade)
VALUES
(1, 1, 2, 'Bronze', 'Silver'),
(2, 2, 3, 'Silver', 'Gold'),
(3, 3, 4, 'Bronze', 'Bronze'),
(4, 4, 5, 'Bronze', 'Silver'),
(5, 5, 6, 'Gold', 'VIP'),
(6, 6, 7, 'Silver', 'Gold'),
(7, 7, 8, 'Bronze', 'Bronze'),
(8, 8, 9, 'Bronze', 'Silver'),
(9, 9, 10, 'Silver', 'Gold'),
(10, 10, 11, 'Bronze', 'Bronze'),
(11, 11, 12, 'Bronze', 'Silver'),
(12, 12, 13, 'Bronze', 'Bronze'),
(13, 13, 14, 'Gold', 'VIP'),
(14, 14, 15, 'Silver', 'Gold'),
(15, 15, 16, 'Bronze', 'Silver'),
(16, 16, 17, 'Bronze', 'Bronze'),
(17, 17, 18, 'Gold', 'VIP'),
(18, 18, 19, 'Bronze', 'Silver'),
(19, 19, 20, 'Silver', 'Gold'),
(20, 20, 21, 'Bronze', 'Bronze'),
(21, 21, 22, 'Bronze', 'Silver'),
(22, 22, 23, 'Silver', 'Gold'),
(23, 23, 24, 'Gold', 'VIP'),
(24, 24, 25, 'Bronze', 'Silver'),
(25, 25, 26, 'Bronze', 'Bronze'),
(26, 26, 27, 'Silver', 'Gold'),
(27, 27, 28, 'Bronze', 'Silver'),
(28, 28, 29, 'Bronze', 'Bronze'),
(29, 29, 30, 'Gold', 'VIP'),
(30, 30, 1, 'Bronze', 'Silver');
/*
4. restaurant           ← region 참조
5. menu                 ← restaurant 참조
6. menu_price_history   ← menu 참조
7. delivery_fee
*/
/*8. delivery_address     ← customer 참조*/
INSERT INTO delivery_address
(customer_id, address_detail, is_default)
VALUES
(1, '서울 강남구 테헤란로 101', TRUE),
(2, '서울 강북구 미아로 22', TRUE),
(3, '부산 해운대구 달맞이길 33', TRUE),
(4, '대구 수성구 동대구로 44', TRUE),
(5, '대전 서구 둔산로 55', TRUE),
(6, '울산 남구 삼산로 66', TRUE),
(7, '광주 광산구 첨단로 77', TRUE),
(8, '수원 영통구 광교로 88', TRUE),
(9, '고양 일산동구 중앙로 99', TRUE),
(10, '성남 분당구 판교역로 100', TRUE),
(11, '안양 동안구 시민대로 111', TRUE),
(12, '고양 덕양구 화정로 122', TRUE),
(13, '안산 상록구 예술광장로 133', TRUE),
(14, '안산 단원구 중앙대로 144', TRUE),
(15, '수원 팔달구 효원로 155', TRUE),
(16, '용인 처인구 금령로 166', TRUE),
(17, '용인 기흥구 구성로 177', TRUE),
(18, '안양 만안구 안양로 188', TRUE),
(19, '인천 연수구 송도과학로 199', TRUE),
(20, '인천 부평구 부평대로 200', TRUE),
(21, '인천 서구 청라커낼로 211', TRUE),
(22, '청주 흥덕구 가로수로 222', TRUE),
(23, '전주 완산구 효자로 233', TRUE),
(24, '전주 덕진구 백제대로 244', TRUE),
(25, '천안 서북구 불당대로 255', TRUE),
(26, '부산 동래구 충렬대로 266', TRUE),
(27, '창원 성산구 중앙대로 277', TRUE),
(28, '창원 진해구 충장로 288', TRUE),
(29, '포항 북구 중흥로 299', TRUE),
(30, '여수 학동로 300', TRUE);
/*
9. coupon
10. orders              ← customer, restaurant, delivery_fee, delivery_address, coupon 참조
11. order_item          ← orders, menu 참조
12. review              ← customer, restaurant 참조
*/

/* 9. coupon */
INSERT INTO coupon (coupon_name, discount_type, discount_value, min_order_amount, expired_at) VALUES
('WELCOME2025',   'fixed',   3000, 15000, '2025-12-31 23:59:59'),
('SPRING10',      'percent', 10,   20000, '2025-06-30 23:59:59'),
('CHICKEN2000',   'fixed',   2000, 10000, '2025-07-31 23:59:59'),
('BIRTHDAY20',    'percent', 20,   25000, '2025-12-31 23:59:59'),
('WEEKEND1000',   'fixed',   1000, 8000,  '2025-08-31 23:59:59'),
('FIRST5000',     'fixed',   5000, 30000, '2025-09-30 23:59:59'),
('SUMMER15',      'percent', 15,   20000, '2025-08-31 23:59:59'),
('VIP25',         'percent', 25,   50000, '2025-12-31 23:59:59'),
('LUNCH2500',     'fixed',   2500, 12000, '2025-07-31 23:59:59'),
('DINNER1500',    'fixed',   1500, 10000, '2025-10-31 23:59:59'),
('KOREAN12',      'percent', 12,   18000, '2025-11-30 23:59:59'),
('WEEKDAY2000',   'fixed',   2000, 15000, '2025-09-30 23:59:59');

/* 10. orders */
INSERT INTO orders (customer_id, restaurant_id, total_price, order_time, delivery_status, delivery_fee_id, address_id, coupon_id, discount_amount) VALUES
(1,  1,  25000, '2025-01-05 12:30:00', 'delivered', 1, 1,  1,  3000),
(3,  3,  32000, '2025-01-15 19:20:00', 'delivered', 1, 3,  NULL, 0),
(4,  4,  15000, '2025-01-20 13:10:00', 'delivered', 3, 4,  3,  2000),
(5,  5,  45000, '2025-02-01 20:00:00', 'delivered', 2, 5,  4,  9000),
(6,  1,  22000, '2025-02-10 12:00:00', 'delivered', 1, 6,  5,  1000),
(7,  2,  37000, '2025-02-15 19:30:00', 'delivered', 2, 7,  NULL, 0),
(8,  3,  28000, '2025-02-20 18:00:00', 'delivered', 3, 8,  6,  5000),
(9,  4,  19000, '2025-03-01 13:30:00', 'delivered', 1, 9,  7,  2850),
(10, 5,  53000, '2025-03-05 20:15:00', 'delivered', 2, 10, 8,  13250),
(1,  3,  31000, '2025-03-10 19:00:00', 'delivered', 1, 1,  NULL, 0),
(2,  4,  24000, '2025-03-15 12:45:00', 'delivered', 3, 2,  9,  2500),
(3,  5,  41000, '2025-03-20 18:30:00', 'delivered', 2, 3,  10, 1500),
(4,  1,  16000, '2025-04-01 13:00:00', 'delivered', 1, 4,  NULL, 0),
(5,  2,  29000, '2025-04-05 20:00:00', 'delivered', 2, 5,  11, 3480);


/* 11. order_items */
INSERT INTO order_item (order_id, menu_id, quantity, ordered_unit_price) VALUES
(1,  1,  2, 9000),
(1,  2,  1, 7000),
(2,  3,  1, 12000),
(2,  4,  2, 3000),
(3,  5,  2, 13000),
(3,  6,  1, 6000),
(4,  7,  1, 15000),
(5,  8,  2, 18000),
(5,  9,  1, 9000),
(6,  1,  1, 9000),
(6,  2,  2, 6500),
(7,  3,  3, 11000),
(7,  4,  1, 4000),
(8,  5,  1, 13000),
(8,  6,  2, 7500),
(9,  7,  2, 8000),
(9,  8,  1, 3000),
(10, 9,  3, 15000),
(10, 1,  1, 8000),
(11, 2,  2, 12000),
(12, 3,  1, 10000),
(12, 4,  2, 7000),
(13, 5,  2, 17000),
(14, 6,  1, 16000),
(15, 7,  1, 14000),
(15, 8,  2, 7500);