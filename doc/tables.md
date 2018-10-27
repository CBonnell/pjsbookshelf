## BOOK
```
COLUMN_NAME         |TYPE_NAME|DEC&|NUM&|COLUM&|COLUMN_DEF|CHAR_OCTE&|IS_NULL&
------------------------------------------------------------------------------
ID                  |VARCHAR  |NULL|NULL|20    |NULL      |40        |NO      
TITLE               |VARCHAR  |NULL|NULL|256   |NULL      |512       |NO      
KANA_TITLE          |VARCHAR  |NULL|NULL|256   |NULL      |512       |NO      
AUTHORS             |VARCHAR  |NULL|NULL|256   |NULL      |512       |NO      
PUBLISHER           |VARCHAR  |NULL|NULL|256   |NULL      |512       |NO      
ISBN                |VARCHAR  |NULL|NULL|64    |NULL      |128       |NO      
IMAGE_URL           |VARCHAR  |NULL|NULL|256   |NULL      |512       |NO      
REGISTER_DATE       |DATE     |0   |10  |10    |NULL      |NULL      |YES     
DISCARD_DATE        |DATE     |0   |10  |10    |NULL      |NULL      |YES     
COMMENTS            |VARCHAR  |NULL|NULL|256   |NULL      |512       |NO      
FLAGS               |VARCHAR  |NULL|NULL|256   |NULL      |512       |NO    
```

## CHECKOUTHISTORY
```
COLUMN_NAME         |TYPE_NAME|DEC&|NUM&|COLUM&|COLUMN_DEF|CHAR_OCTE&|IS_NULL&
------------------------------------------------------------------------------
ID                  |INTEGER  |0   |10  |10    |AUTOINCRE&|NULL      |NO      
STATUS              |SMALLINT |0   |10  |5     |1         |NULL      |YES     
CHECKOUT_DATE       |TIMESTAMP|6   |10  |26    |CURRENT T&|NULL      |NO      
RETURNED_DATE       |TIMESTAMP|6   |10  |26    |NULL      |NULL      |YES     
PERSON_ID           |INTEGER  |0   |10  |10    |NULL      |NULL      |NO      
BOOK_ID             |VARCHAR  |NULL|NULL|20    |NULL      |40        |NO     
```

## FAMILY
```
COLUMN_NAME         |TYPE_NAME|DEC&|NUM&|COLUM&|COLUMN_DEF|CHAR_OCTE&|IS_NULL&
------------------------------------------------------------------------------
ID                  |INTEGER  |0   |10  |10    |AUTOINCRE&|NULL      |NO      
NAME                |VARCHAR  |NULL|NULL|24    |NULL      |48        |NO    
```

## LASTID
```
COLUMN_NAME         |TYPE_NAME|DEC&|NUM&|COLUM&|COLUMN_DEF|CHAR_OCTE&|IS_NULL&
------------------------------------------------------------------------------
NAME                |VARCHAR  |NULL|NULL|20    |NULL      |40        |NO      
LAST_ID             |INTEGER  |0   |10  |10    |NULL      |NULL      |NO      
```

## PERSON
```
COLUMN_NAME         |TYPE_NAME|DEC&|NUM&|COLUM&|COLUMN_DEF|CHAR_OCTE&|IS_NULL&
------------------------------------------------------------------------------
ID                  |INTEGER  |0   |10  |10    |AUTOINCRE&|NULL      |NO      
FAMILY_ID           |INTEGER  |0   |10  |10    |NULL      |NULL      |NO      
TYPE                |VARCHAR  |NULL|NULL|8     |NULL      |16        |NO      
NAME                |VARCHAR  |NULL|NULL|64    |NULL      |128       |NO      
KATAKANA            |VARCHAR  |NULL|NULL|64    |NULL      |128       |NO      
ROMAJI              |VARCHAR  |NULL|NULL|64    |NULL      |128       |NO      
DELETION_DATE       |DATE     |0   |10  |10    |NULL      |NULL      |YES     
```


