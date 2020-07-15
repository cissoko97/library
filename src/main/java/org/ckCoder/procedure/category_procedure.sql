drop procedure if exists save_or_update_category;

DELIMITER |

create procedure
    save_or_update_category(IN p_id_category INT, IN p_flag VARCHAR(30),
                            IN p_description varchar(255),
                            IN p_operation int)

begin
    if p_operation = 1 then

        insert into category(flag, description)
        values (p_flag, p_description);

        set p_id_category = last_insert_id();

        select * from category where id = p_id_category;

    end if;

    if p_operation = 0 then
        update category
        set flag        = p_flag,
            description = p_description
        where id = p_id_category;
        select * from category where id = p_id_category;
    end if;
end|

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
    IF p_id_category IS NULL THEN
        select * from category;
    ELSE
        select *
        from category
        where id = p_id_category;
    END if;
end |
DELIMITER ;
