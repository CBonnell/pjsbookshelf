-- 貸出記録調査
--
-- 目的：ある本の直近の貸出記録を調べる。破損などが生じた時などに利用。
-- 出力項目：返却日, 氏名, 氏名カナ

CONNECT 'jdbc:derby:../PJS-DB/pjsLibraryDB';

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
