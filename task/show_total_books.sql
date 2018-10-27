-- 蔵書数調査。
--
-- 目的：図書室全体の蔵書数の把握。
-- 出力項目：蔵書件数

CONNECT 'jdbc:derby:../PJS-DB/pjsLibraryDB';

SELECT
  COUNT(Book.id)
FROM
  Book
WHERE
  discard_date IS NULL
;

DISCONNECT;
