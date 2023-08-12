-- 貸出回数が少ない図書のリスト（過去5年間における、貸出回数が0～5回の図書のリスト）
--
-- 目的：図書室全体の蔵書整理を実施する際の参考のため。
-- 出力項目：蔵書番号, 題名, 著者, 出版社, 登録年, 貸出回数
-- 備考：例年、8/20頃までに必要。

CONNECT 'jdbc:derby:pjsLibraryDB';

-- 貸出回数0回の本


SELECT
  Book.id, title, returned_date, name, katakana
FROM
  CheckoutHistory, Person, Book
WHERE
  book_id = :target_book_id AND
  person_id = Person.id AND
  CheckoutHistory.book_id = Book.id
;

DISCONNECT;
