/* procedure pour la gstion d elma categorie*/
DELIMITER |

drop procedure if exists save_category;

create procedure
    save_category(IN p_flag VARCHAR(30),
                  IN p_description varchar(255))

begin

    insert into category(flag, description)
    values (p_flag, p_description);

    select * from category where id = last_insert_id();

end|

drop procedure if EXISTS update_category;

create procedure update_category(IN p_id INT, IN p_flag VARCHAR(30), IN p_description VARCHAR(255))

begin
    update category
    set flag        = p_flag,
        description = p_description
    where id = p_id;
    select * from category where id = p_id;
end |

drop procedure if exists delete_category;

create procedure delete_category(IN p_id_procedure INT)

begin
    delete
    from category
    where id = p_id_procedure;

    select true;
end |

drop procedure if exists fetch_category;

create procedure fetch_category(IN p_id_category INT)

begin
    IF p_id_category=0 THEN
        select * from category;
    ELSE
        select *
        from category
        where id = p_id_category;
    END if;
end |

DELIMITER ;
