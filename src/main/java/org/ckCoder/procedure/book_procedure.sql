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

        insert into book(title, description, file_name, id_category, nb_vue, edition_year, valeur_nominal, price, type,
                         img_name, created_at, updated_at, fileByte, imgByte)
        values (p_title, p_descrition, p_file_name, p_fk_id_cathalogue, 0, p_edition_year, p_valeur_nominal, p_price,
                p_type_livre, p_img_name, current_timestamp, current_time, p_file_book, p_img_book);

        set last_id = LAST_INSERT_ID();

    /*set p_id_book = SCOPE_IDENTITY();*/
    /*call select_book_and_critique();*/
    select book.id as book_id, book.title, book.description, book.file_name, book.nb_vue,
           book.edition_year,book.valeur_critique, book.valeur_nominal,
           book.price, book.type, book.img_name,
           book.created_at as book_created_at, book.updated_at as book_updated_at,
           book.availability, book.fileByte, book.imgByte from book where id=last_id;
end|

drop procedure if exists update_book |

create procedure update_book(IN p_title varchar(255), IN p_descrition text,
                             IN p_fk_id_cathalogue bigint,
                             IN p_edition_year int, IN p_valeur_nominal int,
                             IN p_price double, IN p_type_livre varchar(30),
                              IN p_id_book bigint)
                             begin
                                 update book
                                 set title          = p_title,
                                     description    = p_descrition,
                                     id_category    = p_fk_id_cathalogue,
                                     edition_year   = p_edition_year,
                                     valeur_nominal = p_valeur_nominal,
                                     price          = p_price,
                                     type           = p_type_livre,
                                     updated_at     = current_timestamp
                                 where id = p_id_book;
                             end |

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
    declare current_value_critique_for_book int;

    start transaction;

    insert into critiques(book_id, user_id, note, comment, updated_at, created_at)
    values (p_fk_book_id, p_fk_user_id, p_note, p_comment, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

    select valeur_critique into current_value_critique_for_book from book where id = p_fk_book_id;
    if current_value_critique_for_book is null then
        set current_value_critique_for_book = 0;
    end if;
    set current_value_critique_for_book = current_value_critique_for_book + p_note - 2.5;

    update book set valeur_critique = current_value_critique_for_book where id = p_fk_book_id;
    select valeur_nominal, valeur_critique from book where id=p_fk_book_id;
    commit;
end |

/*
 * afficher critique sur un livre
 */

drop procedure if exists get_critique |

create procedure get_critique(IN p_book_id bigint)
begin
    select critiques.note as critique_note, critiques.comment as critique_comment,critiques.id as critique_id,
           critiques.created_at as critique_created_at, critiques.updated_at as critique_updaated_at,
           users.email as user_email
    from critiques
             left join users on critiques.user_id = users.id
             inner join person on users.person_id = person.id
    where critiques.book_id = p_book_id;
end |


drop procedure if exists find_all_book |

create procedure find_all_book()
begin
    select book.id as book_id, book.title, book.description, book.file_name, book.nb_vue, book.edition_year,
           book.valeur_critique, book.valeur_nominal, book.price, book.type, book.img_name,
           book.created_at as book_created_at, book.updated_at as book_updated_at,
           book.availability, book.fileByte, book.imgByte from book;
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
    select book.id as book_id, book.title, book.description, book.file_name, book.nb_vue, book.edition_year,
           book.valeur_critique, book.valeur_nominal, book.price, book.type, book.img_name,
           book.created_at as book_created_at, book.updated_at as book_updated_at,
           book.availability, book.fileByte, book.imgByte, a.id as author_id, a.bibliography as authoer_bibliography,
           c.id as category_id, c.flag as category_flag, c.description as category_description,
           cr.id as critique_id, cr.note as critique_note, cr.comment as critique_comment,
           cr.created_at as critique_created_at,p.id as person_id, p.name as person_name, p.surname as person_surname
    from book
        left join author_book ab on book.id = ab.book_id
        left join category c on book.id_category = c.id
        left join critiques cr on book.id = cr.book_id
        left join author a on ab.author_id = a.id
        left join person p on a.person_id = p.id where book.id= p_id_book;
end |



drop procedure if exists find_product_by_category;

create procedure find_product_by_category(IN p_id_category bigint)
begin
    select book.id as book_id, book.title, book.description, book.file_name, book.nb_vue, book.edition_year,
           book.valeur_critique, book.valeur_nominal, book.price, book.type, book.img_name,
           book.created_at as book_created_at, book.updated_at as book_updated_at,
           book.availability, book.fileByte, book.imgByte
    from book where book.id_category = p_id_category order by book.title;
end |

/*
 * delete book
 */
drop procedure if exists increment_number_of_views;
create procedure increment_number_of_views(IN p_id_book bigint, IN p_id_user BIGINT)
begin
    declare _id_verification bigint;
    set _id_verification = null;
    select book_id into _id_verification from view_user_book where user_id=p_id_user and book_id=p_id_book;
    if _id_verification is null then
        insert into view_user_book(user_id, book_id) VALUES (p_id_user, p_id_book);
        select nb_vue into @v_nb_vu from book where book.id = p_id_book;
        update book set nb_vue=(@v_nb_vu+1) where id = p_id_book;
    end if ;
end |
DELIMITER ;
