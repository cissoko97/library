drop procedure if exists save_or_update_profils;

DELIMITER |

create procedure
    save_or_update_profils(IN p_id_profils INT, IN p_label VARCHAR(30),
                           IN p_description varchar(255),
                           IN p_operation int)

begin
    if p_operation = 1 then

        insert into profils(label, description)
        values (p_label, p_description);

        set p_id_profils = last_insert_id();

        select * from profils where id = p_id_profils;

    end if;

    if p_operation = 0 then
        update profils
        set label       = p_label,
            description = p_description
        where id = p_id_profils;
        select * from profils where id = p_id_profils;
    end if;
end|

drop procedure if exists delete_profils;

create procedure delete_profils(IN p_id_procedure INT)

begin
    delete
    from profils
    where id = p_id_procedure;

    select true;
end |

drop procedure if exists fetch_profils;

create procedure fetch_profils(IN p_id_profils INT)

begin
    IF p_id_profils IS NULL THEN
        select * from profils;
    ELSE
        select *
        from profils
        where id = p_id_profils;
    END if;
end |
DELIMITER ;
