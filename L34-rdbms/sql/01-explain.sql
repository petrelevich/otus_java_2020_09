-- Простые запросы
EXPLAIN
SELECT * FROM actor;

EXPLAIN ANALYZE
SELECT * FROM actor;

EXPLAIN (ANALYZE, BUFFERS)
SELECT * FROM actor;

EXPLAIN (ANALYZE, BUFFERS, FORMAT TEXT)
SELECT * FROM actor;

EXPLAIN (ANALYZE, BUFFERS, FORMAT XML)
SELECT * FROM actor;

EXPLAIN (ANALYZE, BUFFERS, FORMAT JSON)
SELECT * FROM actor;

EXPLAIN (ANALYZE, BUFFERS, FORMAT YAML)
SELECT * FROM actor;

-- https://blog.bullgare.com/wp-content/uploads/2015/04/understanding_explain.html

-- Создаем тестовую таблицу
CREATE TABLE foo (c1 integer, c2 text);

INSERT INTO foo
SELECT i, md5(random()::text)
FROM generate_series(1, 1000000) AS i;

-- Читаем данные
EXPLAIN
SELECT * FROM foo;
-- Seq Scan

-- Попробуем добавить 10 строк.
INSERT INTO foo
SELECT i, md5(random()::text)
FROM generate_series(1, 10) AS i;

-- Опять выполним запрос
EXPLAIN SELECT * FROM foo;
-- Видим старое значение estimated rows

-- Обновим статистику через ANALYZE
ANALYZE foo;
EXPLAIN SELECT * FROM foo;
-- Теперь в rows актуальное значение.

-- EXPLAIN выводит гипотетический план, запрос не выполняется
-- EXPLAIN ANALYZE - реальный, запрос выполняется
EXPLAIN ANALYZE
SELECT * FROM foo;

-- Использование памяти
EXPLAIN (ANALYZE,BUFFERS)
SELECT * FROM foo;
-- Buffers: shared read — количество блоков, считанных с диска.
-- Buffers: shared hit — количество блоков, считанных из кэша.

-- ------------
-- WHERE
-- ------------
EXPLAIN ANALYZE
SELECT * FROM foo WHERE c1 > 500;
-- Seq Scan - индексов нет.
-- Filter: (c1 > 500)
-- Rows Removed by Filter: 510
-- (cost=0.00..20834.38 rows=999523 width=37) (actual time=0.168..454.000 rows=999500 loops=1)
-- Execution time: 621.078 ms

-- ------------
-- Индексы
-- ------------
-- Создадим индекс
CREATE INDEX ON foo(c1);
-- Выполним тот же запрос
EXPLAIN ANALYZE
SELECT * FROM foo WHERE c1 > 500;
-- Seq Scan - хотя индекс есть

-- Отфильтровано 510 строк из более чем миллиона (Rows Removed by Filter: 510)

-- Seq Scan on foo  (cost=0.00..20834.12 rows=999533 width=37) (actual time=4.691..7513.423 rows=999500 loops=1)
-- Execution time: 10595.238 ms

-- Запретим Seq Scan и попробуем использовать индекс принудительно:
SET enable_seqscan TO off;
EXPLAIN ANALYZE
SELECT * FROM foo WHERE c1 > 500;
SET enable_seqscan TO on;
-- Index Scan, Index Cond
-- А что со стоимостью?

-- Изменим запрос
EXPLAIN ANALYZE
SELECT * FROM foo WHERE c1 < 500;
-- Index Scan

-- Усложним условие. Используем текстовое поле.
EXPLAIN ANALYZE
SELECT * FROM foo
WHERE c1 < 500 AND c2 LIKE 'abcd%';

-- Если в условии только текстовое поле:
EXPLAIN ANALYZE
SELECT * FROM foo WHERE c2 LIKE 'abcd%';
-- Seq Scan, индексов на c2 нет

-- Создадим индекс по c2
CREATE INDEX ON foo(c2 text_pattern_ops);
-- Выполняем запрос
EXPLAIN ANALYZE
SELECT * FROM foo
WHERE c2 LIKE 'abcd%';
-- Bitmap Index Scan, индекс foo_c2_idx1 для определения нужных нам записей,

-- Покрывающий индекс - Index Only Scan
EXPLAIN ANALYZE
SELECT c1 FROM foo WHERE c1 < 500;

-- ------------
-- Сортировка
-- ------------
-- удалим индекс
DROP INDEX foo_c1_idx;

EXPLAIN (ANALYZE, BUFFERS)
SELECT * FROM foo ORDER BY c1;
-- есть:
-- Sort Method: external sort
-- temp read=5751 written=57451 (это страницы)

-- Попробуем увеличить work_mem:
SET work_mem TO '200MB';
EXPLAIN (ANALYZE)
SELECT * FROM foo ORDER BY c1;
-- Sort Method: quicksort - все в памяти

-- ----------
-- LIKE
-- ----------

-- Будет ли использоваться индекс?
EXPLAIN (ANALYZE,BUFFERS)
SELECT * FROM foo WHERE c2 LIKE 'ab%';

-- А здесь?
EXPLAIN (ANALYZE,BUFFERS)
SELECT * FROM foo WHERE c2 LIKE '%ab';

-- Что со стоимостью и временем?

-- -----------
-- JOIN
-- -----------

-- Создадим новую таблицу, соберём для неё статистику.

CREATE TABLE bar (c1 integer, c2 boolean);
INSERT INTO bar
SELECT i, i%2=1
FROM generate_series(1, 500000) AS i;
ANALYZE bar;

select * from bar;

-- Запрос по двум таблицам
EXPLAIN ANALYZE
SELECT *
FROM foo
JOIN bar ON foo.c1=bar.c1;
-- Hash Join
-- Запомним стоимость и время:
-- Hash Join  (cost=30834.22..46029.24 rows=546611 width=42) (actual time=4225.109..8614.594 rows=500010 loops=1)
-- Execution time: 9121.507 ms

-- Добавим индекс
CREATE INDEX ON bar(c1);
CREATE INDEX ON foo(c1);

-- Тот же запрос - какой будет тип JOIN?
EXPLAIN ANALYZE
SELECT *
FROM foo
JOIN bar ON foo.c1=bar.c1;
