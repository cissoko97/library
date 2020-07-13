/*
*   procedure ajouter et mise a jour d'un livre
 */
drop procedure if exists save_book;

DELIMITER |
create procedure save_book(IN p_title varchar(255), p_descrition text, p_file_name varchar(255),
p_fk_id_cathalogue bigint, p_edition_year int, p_valeur_nominal int, p_price double, p_type_livre tinyint(1),
p_img_name varchar(250), inout p_id_book bigint, p_num_method_to_exc int)

begin
    if (p_num_method_to_exc = 1) then
        insert into book(title, description, file_name, id_category, nb_vue, edition_year, valeur_nominal, price, type, img_name, created_at, updated_at)
        values (p_title, p_descrition, p_file_name, p_fk_id_cathalogue, 0, p_edition_year, p_valeur_nominal, p_price, p_type_livre, p_img_name,current_timestamp, current_time);

        set p_id_book = LAST_INSERT_ID();
    end if;


    if (p_num_method_to_exc = 2) then
        update book set
                        title = p_title,
                        description = p_descrition,
                        file_name = p_file_name,
                        id_category = p_fk_id_cathalogue,
                        edition_year = p_edition_year,
                        valeur_nominal = p_valeur_nominal,
                        price = p_price,
                        type = p_type_livre,
                        img_name = p_img_name,
                        updated_at = current_timestamp
        where id = p_id_book;
    end if ;
    /*set p_id_book = SCOPE_IDENTITY();*/
    select * from book;
end |

/*
*   procedure bloquer livre
 */
drop procedure if exists lock_book |

create procedure lock_book (p_id_book bigint, p_availability tinyint)
begin
    declare var_id_book bigint;

    select id from book where id = p_id_book into var_id_book;

    update book set
        availability = p_availability where id = var_id_book;

end |

/*
*   procedure afficher un livre
 */

drop procedure if exists get_books_by_page |

create procedure get_books_by_page (p_page int, p_size int, p_id_catalogue int)
begin
    if(p_id_catalogue is null ) then
        select * from book limit p_page,p_size;
    else
        select * from book where id_category = p_id_catalogue  limit p_page, p_size;
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
    select * from book
    inner join author_book on book.id = author_book.book_id
    inner join author on author_book.author_id = author.id
    inner join person on author.person_id = person.id
    where book.title = p_name or person.name = p_name;
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

    select valeur_critique from book where id=p_fk_book_id into current_value_nominal_for_book;

    update book set
                    valeur_critique = current_value_nominal_for_book + p_note
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
          select * from critiques
              inner join users on critiques.user_id = users.id
              left join person on users.person_id = person.id;

      else
          select * from critiques
                inner join users on critiques.user_id = users.id
                left join person on users.person_id = person.id
                where critiques.id = person_id;
      end if ;
  end |













