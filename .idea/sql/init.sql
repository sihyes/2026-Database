/*
1. region
2. customer
3. customer_history     ← customer, region 참조
4. restaurant           ← region 참조
5. menu                 ← restaurant 참조
6. menu_price_history   ← menu 참조
7. delivery_fee
8. delivery_address     ← customer 참조
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