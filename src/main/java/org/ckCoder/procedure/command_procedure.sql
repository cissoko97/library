Delimiter |

/*
    procedure pour la gestion de commandes
*/

drop procedure if exists save_order_simple_order;

create procedure save_order_simple_order(IN p_id_array text, IN p_user_id bigint)
begin
    declare _next varchar(30);
    declare _nextlen int;
    declare current_book_id bigint;
    declare _idCommand bigint;
    declare _totalprice numeric;

    set _totalprice = 0;
    start transaction;
    insert into command(user_id,total_price, accepted) value (p_user_id, 0, false);
    set _idCommand = last_insert_id();
    iterator:
    loop
        if length(p_id_array) = 0 or p_id_array is null then
            leave iterator;
        end if ;
        set _next = substring_index(p_id_array,',',1);
        set _nextlen = length(_next);
        select convert(trim(_next), SIGNED integer) into current_book_id;

        insert into line(book_id, command_id, quantity)
            value (current_book_id, _idCommand, 1);

        select price into @price_book from book where id = current_book_id;

        set _totalprice = _totalprice + @price_book;

        set p_id_array = INSERT(p_id_array,1,_nextlen+1,'');
    end loop;

    /*
     *  update total order
     */

    update command set total_price = _totalprice
                       where id = _idCommand;

    select command.id as command_id, command.total_price as command_total_price,
           command.updated_at as command_update_at, command.created_at as command_created_at,
           command.accepted as command_accept,
           p.name as person_name, p.surname as person_surname
    from command
             inner join users as us on command.user_id = us.id
             left join person p on us.person_id = p.id;

    commit;
end |

drop procedure if exists findAll_command;

create procedure findAll_command(IN p_page INTEGER, IN p_size INTEGER)
begin
    declare current_page int;
    declare current_size int;

    set current_page = p_size * p_page;
    set current_size = current_page + p_size;

    select command.id as command_id, command.total_price as command_total_price,
           command.updated_at as command_update_at, command.created_at as command_created_at,
           command.accepted as command_accept,
           p.name as person_name, p.surname as person_surname
    from command
    inner join users as us on command.user_id = us.id
    left join person p on us.person_id = p.id order by command_id desc
    limit current_page,current_size;

end |

drop procedure if exists find_command_between_two_date;

create procedure find_command_between_two_date(in p_begin timestamp, in p_end timestamp)
begin
    select command.id as command_id, command.total_price as command_total_price,
           command.updated_at as command_update_at, command.created_at as command_created_at,
           command.accepted as command_accept,
           p.name as person_name, p.surname as person_surname
    from command
    inner join users as us on command.user_id = us.id
    left join person p on us.person_id = p.id
    where p_begin <= command.created_at and  command.created_at  <= p_end order by command_id desc;
end |

drop procedure if exists find_command_and_lineItem;
create procedure find_command_and_lineItem(IN p_id_order bigint)
begin
    select book.created_at as book_created_at, book.updated_at as book_update_at,
           book.price as book_price, book.title book_title
    from book
    inner join line l on book.id = l.book_id
    where l.command_id = p_id_order;

end |

drop procedure if exists change_status_command;
create procedure change_status_command(IN p_id_order bigint)
begin
    update command set accepted = true where id = p_id_order;
end |
delimiter ;