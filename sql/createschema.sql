/*1. region*/
CREATE TABLE region (
                        region_id INT PRIMARY KEY AUTO_INCREMENT,
                        region_name VARCHAR(50) NOT NULL,
                        city VARCHAR(50) NOT NULL
);
/*2. customer*/
CREATE TABLE customer (
                          customer_id INT PRIMARY KEY AUTO_INCREMENT,
                          name VARCHAR(50) NOT NULL,
                          age INT NOT NULL,
                          gender VARCHAR(10),
                          region_id INT,
                          current_grade VARCHAR(20) DEFAULT 'Bronze',
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                          FOREIGN KEY (region_id)
                              REFERENCES region(region_id)
);
/*3. customer_history     ← customer, region 참조*/
CREATE TABLE customer_history (
                                  history_id INT PRIMARY KEY AUTO_INCREMENT,
                                  customer_id INT NOT NULL,
                                  old_region_id INT,
                                  new_region_id INT,
                                  old_grade VARCHAR(20),
                                  new_grade VARCHAR(20),
                                  changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                  FOREIGN KEY (customer_id)
                                      REFERENCES customer(customer_id),

                                  FOREIGN KEY (old_region_id)
                                      REFERENCES region(region_id),

                                  FOREIGN KEY (new_region_id)
                                      REFERENCES region(region_id)
);

/*4. restaurant           ← region 참조*/
CREATE TABLE restaurant (
                            restaurant_id INT PRIMARY KEY AUTO_INCREMENT,
                            restaurant_name VARCHAR(100) NOT NULL,
                            category VARCHAR(50),
                            region_id INT,
                            rating NUMERIC(3,2),

                            FOREIGN KEY (region_id)
                                REFERENCES region(region_id)
);

/*5. menu                 ← restaurant 참조*/
CREATE TABLE menu (
                      menu_id INT PRIMARY KEY AUTO_INCREMENT,
                      restaurant_id INT NOT NULL,
                      menu_name VARCHAR(100) NOT NULL,
                      current_price INT NOT NULL,
                      is_available BOOLEAN DEFAULT TRUE,

                      FOREIGN KEY (restaurant_id)
                          REFERENCES restaurant(restaurant_id)
);

/*6. menu_price_history   ← menu 참조*/
CREATE TABLE menu_price_history (
                                    history_id INT PRIMARY KEY AUTO_INCREMENT,
                                    menu_id INT NOT NULL,
                                    old_price INT NOT NULL,
                                    new_price INT NOT NULL,
                                    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                    FOREIGN KEY (menu_id)
                                        REFERENCES menu(menu_id)
);

/*7. delivery_fee*/
CREATE TABLE delivery_fee (
                              delivery_fee_id INT PRIMARY KEY AUTO_INCREMENT,
                              delivery_type VARCHAR(20) NOT NULL,
                              distance_category VARCHAR(20) NOT NULL,
                              fee INT NOT NULL
);

/*8. delivery_address     ← customer 참조*/
CREATE TABLE delivery_address (
                                  address_id INT PRIMARY KEY AUTO_INCREMENT,
                                  customer_id INT NOT NULL,
                                  address_detail VARCHAR(200) NOT NULL,
                                  is_default BOOLEAN DEFAULT FALSE,

                                  FOREIGN KEY (customer_id)
                                      REFERENCES customer(customer_id)
);

/* 9. coupon */
CREATE TABLE coupon (
                        coupon_id         INT           PRIMARY KEY AUTO_INCREMENT,
                        coupon_name       VARCHAR(100)  NOT NULL,
                        discount_type     VARCHAR(20)   NOT NULL,
                        discount_value    NUMERIC(10,2) NOT NULL,
                        min_order_amount  NUMERIC(10,2) DEFAULT 0,
                        expired_at        TIMESTAMP     NOT NULL
);

/* 10. orders */
CREATE TABLE orders (
                        order_id        INT           PRIMARY KEY AUTO_INCREMENT,
                        customer_id     INT           NOT NULL,
                        restaurant_id   INT           NOT NULL,
                        total_price     NUMERIC(10,2) NOT NULL,
                        order_time      TIMESTAMP     NOT NULL,
                        delivery_status VARCHAR(20)   DEFAULT 'pending',
                        delivery_fee_id INT,
                        address_id      INT,
                        coupon_id       INT,
                        discount_amount NUMERIC(10,2) DEFAULT 0,

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
                            ordered_unit_price NUMERIC(10,2) NOT NULL,

                            FOREIGN KEY (order_id) REFERENCES orders(order_id),
                            FOREIGN KEY (menu_id)  REFERENCES menu(menu_id)
);

/* 12. review */

CREATE TABLE review (
                        review_id INT PRIMARY KEY AUTO_INCREMENT,

                        order_id INT NOT NULL UNIQUE,

                        rating NUMERIC(3,1) NOT NULL
                            CHECK (rating BETWEEN 0 AND 5),

                        comment TEXT,

                        created_at TIMESTAMP
                            DEFAULT CURRENT_TIMESTAMP,

                        updated_at TIMESTAMP NULL
        ON UPDATE CURRENT_TIMESTAMP,

                        FOREIGN KEY (order_id)
                            REFERENCES orders(order_id)
                            ON DELETE CASCADE
);


/* order view */

CREATE VIEW order_detail_view AS
SELECT
    o.order_id,
    o.customer_id,
    o.order_time,
    o.delivery_status,
    o.total_price,
    o.discount_amount,
    oi.order_item_id,
    oi.menu_id,
    m.menu_name,
    oi.quantity,
    oi.ordered_unit_price,
    oi.quantity * oi.ordered_unit_price AS item_total
FROM orders o
         JOIN order_item oi ON o.order_id = oi.order_id
         JOIN menu m ON oi.menu_id = m.menu_id;

/* review view */
CREATE VIEW review_detail AS
SELECT
    r.review_id,
    r.rating,
    r.comment,
    r.created_at,
    r.updated_at,

    o.order_id,
    o.order_time,
    o.total_price,

    c.customer_id,
    c.name,

    res.restaurant_id,
    res.restaurant_name

FROM review r
         JOIN orders o
              ON r.order_id = o.order_id
         JOIN customer c
              ON o.customer_id = c.customer_id
         JOIN restaurant res
              ON o.restaurant_id = res.restaurant_id;


CREATE INDEX idx_orders_customer ON orders(customer_id);
CREATE INDEX idx_orders_time     ON orders(order_time);
CREATE INDEX idx_orderitem_menu  ON order_item(menu_id);
CREATE INDEX idx_menu_restaurant ON menu(restaurant_id);