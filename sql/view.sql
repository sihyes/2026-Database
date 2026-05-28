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
    c.customer_name,

    res.restaurant_id,
    res.restaurant_name

FROM review r
         JOIN orders o
              ON r.order_id = o.order_id
         JOIN customer c
              ON o.customer_id = c.customer_id
         JOIN restaurant res
              ON o.restaurant_id = res.restaurant_id;