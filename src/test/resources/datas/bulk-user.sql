INSERT INTO users (name, age)
SELECT
    CONCAT('User', x) as name,
    (x % 100) + 1 as age
FROM system_range(1, 100000) as t(x);