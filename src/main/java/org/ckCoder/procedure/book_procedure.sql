/*
*   procedure ajouter et mise a jour d'un livre
 */
DELIMITER |
drop procedure if exists save_book|

create
    /*definer = virtual_libadmin@`%`*/ procedure save_book(IN p_title varchar(255), IN p_descrition text,
                                                       IN p_file_name varchar(255), IN p_fk_id_cathalogue bigint,
                                                       IN p_edition_year int, IN p_valeur_nominal int,
                                                       IN p_price double, IN p_type_livre varchar(30),
                                                       IN p_img_name varchar(250), IN p_id_book bigint,
                                                       IN p_file_book mediumblob, IN p_img_book mediumblob)
begin
    declare last_id bigint;
    set last_id = p_id_book;

    if (p_id_book=0) then
        insert into book(title, description, file_name, id_category, nb_vue, edition_year, valeur_nominal, price, type,
                         img_name, created_at, updated_at, fileByte, imgByte)
        values (p_title, p_descrition, p_file_name, p_fk_id_cathalogue, 0, p_edition_year, p_valeur_nominal, p_price,
                p_type_livre, p_img_name, current_timestamp, current_time, p_file_book, p_img_book);

        set last_id = LAST_INSERT_ID();
    else
        update book
        set title          = p_title,
            description    = p_descrition,
            file_name      = p_file_name,
            id_category    = p_fk_id_cathalogue,
            edition_year   = p_edition_year,
            valeur_nominal = p_valeur_nominal,
            price          = p_price,
            type           = p_type_livre,
            img_name       = p_img_name,
            updated_at     = current_timestamp
        where id = last_id;
    end if;
    /*set p_id_book = SCOPE_IDENTITY();*/
    /*call select_book_and_critique();*/
    select * from book where id=last_id;
end|


drop procedure if exists add_authorAtBook |

create procedure add_authorAtBook(IN p_id_book int(10), IN p_id_author int(10))
begin
   insert into author_book(book_id, author_id) VALUES (p_id_book, p_id_author);
end |

/*
*   procedure bloquer livre
 */
drop procedure if exists lock_book |

create procedure lock_book(p_id_book bigint, p_availability tinyint)
begin
    declare var_id_book bigint;

    select id from book where id = p_id_book into var_id_book;

    update book
    set availability = p_availability
    where id = var_id_book;

end |

/*
*   procedure afficher un livre
 */

drop procedure if exists get_books_by_page |

create procedure get_books_by_page(p_page int, p_size int, p_id_catalogue int)
begin
    if (p_id_catalogue is null) then
        select * from book limit p_page,p_size;
    else
        select * from book where id_category = p_id_catalogue limit p_page, p_size;
    end if;
end |

drop procedure if exists get_one_book |

create procedure get_one_book(p_id bigint)
begin
    select * from book where id = p_id;
end |

/*
 find book by book name
 or
 find by name author
 */

drop procedure if exists find_book |

create procedure find_book(p_name varchar(250))
begin
    select *
    from book
             inner join author_book on book.id = author_book.book_id
             inner join author on author_book.author_id = author.id
             inner join person on author.person_id = person.id
    where book.title = p_name
       or person.name = p_name;
end |

/*
 ajouter critique sur un livre and
 attribuer note à un livre
 */

drop procedure if exists save_critique |

create procedure save_critique(p_note int, p_comment text, p_fk_book_id BIGINT,
                               p_fk_user_id bigint(20))
begin
    declare current_value_nominal_for_book int(10);
    start transaction;
    insert into critiques(book_id, user_id, note, comment, updated_at, created_at)
    values (p_fk_book_id, p_fk_user_id, p_note, p_comment, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

    select valeur_critique from book where id = p_fk_book_id into current_value_nominal_for_book;

    update book
    set valeur_critique = current_value_nominal_for_book + p_note
    where id = p_fk_book_id;
    commit;
end |

/*
 * afficher critique sur un livre
 */

drop procedure if exists get_critique |

create procedure get_critique(p_critique_id bigint)
begin
    if p_critique_id is null then
        select *
        from critiques
                 inner join users on critiques.user_id = users.id
                 left join person on users.person_id = person.id;

    else
        select *
        from critiques
                 inner join users on critiques.user_id = users.id
                 left join person on users.person_id = person.id
        where critiques.id = person_id;
    end if;
end |


drop procedure if exists find_all_book |

create procedure find_all_book()
begin
    select * from book;
end |

/*
 *this procedure recall all books and associated comment
 *with those books
 */
drop procedure if exists select_book_and_critique |

create procedure select_book_and_critique()
begin
    select book.id, book.title, book.description, book.file_name,
           book.nb_vue, book.edition_year, book.valeur_critique,
           book.valeur_nominal, book.price, book.type, book.img_name,
           book.created_at, book.updated_at, book.availability,
           book.fileByte, book.imgByte,
           c.id, c.comment, c.note, c.created_at
    from book left join critiques as c on book.id = c.book_id;
end |

/*
 * this procedure recall one book and author, categories, critique
 */

drop procedure if exists select_book_author_categorie_critique |

create procedure select_book_author_categorie_critique(IN p_id_book BIGINT)
begin
    select book.id, book.title, book.description, book.file_name, book.nb_vue, book.edition_year, book.valeur_critique, book.valeur_nominal, book.price, book.type, book.img_name,
           book.created_at, book.updated_at, book.availability, book.fileByte, book.imgByte, a.id, a.bibliography, c.id, c.flag, c.description, cr.id, cr.note, cr.comment, cr.created_at
    from book left join author_book ab on book.id = ab.book_id left join category c on book.id_category = c.id
              left join critiques cr on book.id = cr.book_id left join author a on ab.author_id = a.id where book.id= p_id_book;
end |

DELIMITER ;
