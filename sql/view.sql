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