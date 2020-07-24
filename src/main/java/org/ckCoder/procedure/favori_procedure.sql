DELIMITER |

drop procedure if exists add_to_favoris;

create procedure add_to_favoris(IN p_id_user INT, IN p_id_book INT)

begin

    insert into favoris(book_id, user_id) VALUES (p_id_book, p_id_user);

    select true;
end |

DELIMITER ;