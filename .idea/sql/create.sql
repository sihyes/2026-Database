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
CREATE TABLE coupon (
                        coupon_id         INT           PRIMARY KEY AUTO_INCREMENT,
                        coupon_name       VARCHAR(100)  NOT NULL,
                        discount_type     VARCHAR(20)   NOT NULL,
                        discount_value    DECIMAL(10,2) NOT NULL,
                        min_order_amount  DECIMAL(10,2) DEFAULT 0,
                        expired_at        TIMESTAMP     NOT NULL
);

/* 10. orders */
CREATE TABLE orders (
                        order_id        INT           PRIMARY KEY AUTO_INCREMENT,
                        customer_id     INT           NOT NULL,
                        restaurant_id   INT           NOT NULL,
                        total_price     DECIMAL(10,2) NOT NULL,
                        order_time      TIMESTAMP     NOT NULL,
                        delivery_status VARCHAR(20)   DEFAULT 'pending',
                        delivery_fee_id INT,
                        address_id      INT,
                        coupon_id       INT,
                        discount_amount DECIMAL(10,2) DEFAULT 0,

                        FOREIGN KEY (customer_id)     REFERENCES customer(customer_id),
                        FOREIGN KEY (restaurant_id)   REFERENCES restaurant(restaurant_id),
                        FOREIGN KEY (delivery_fee_id) REFERENCES delivery_fee(delivery_fee_id),
                        FOREIGN KEY (address_id)      REFERENCES delivery_address(address_id),
                        FOREIGN KEY (coupon_id)       REFERENCES coupon(coupon_id)
);

/* 11. order_items */
CREATE TABLE order_item (
                            order_item_id      INT           PRIMARY KEY AUTO_INCREMENT,
                            order_id           INT           NOT NULL,
                            menu_id            INT           NOT NULL,
                            quantity           INT           NOT NULL CHECK (quantity > 0),
                            ordered_unit_price DECIMAL(10,2) NOT NULL,

                            FOREIGN KEY (order_id) REFERENCES orders(order_id),
                            FOREIGN KEY (menu_id)  REFERENCES menu(menu_id)
);