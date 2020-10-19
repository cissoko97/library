/*
  Procedure pour la creation d'utilsateur
  un utilisateur est en relation avec person
  */
Delimiter |
drop procedure if exists save_user;

create procedure save_user(IN p_name varchar(255),
                           IN p_surname varchar(255),
                           IN p_email varchar(255),
                           IN p_password varchar(255))
begin

    declare errno INT;
    declare msg TEXT;
    Declare new_id INT;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
        BEGIN
            ROLLBACK;
            GET STACKED DIAGNOSTICS CONDITION 1
                errno = MYSQL_ERRNO, msg = MESSAGE_TEXT;
            SELECT 'stacked DA before mapped insert' AS op, errno, msg;
        END;


    insert into person(name, surname)
    VALUES (p_name, p_surname);

    set new_id = last_insert_id();
    insert into users(email, password, person_id)
    values (p_email, p_password, new_id);

    set new_id = last_insert_id();

    select id into @id_profil from profils where label = 'user' limit 1;

    insert into user_profil(user_id, profil_id)
    values (new_id, @id_profil);

    select P.*
    from users U
             inner join person P
                        on U.person_id = P.id
    where U.id = new_id;
end|

/*
    Procedure pour bloquer au debloquer un utilisateur
*/

drop procedure if exists lock_or_unlock_user;

create procedure lock_or_unlock_user(IN p_user_id INT, IN p_is_lock TINYINT(1))

BEGIN
    update users
    set is_locked = p_is_lock
    where id = p_user_id;

    select true;
end |

/*
    Procedure pour la mise à jour d'un utilisateur
*/

drop procedure if exists update_profil_user;

create procedure update_profil_user(IN p_user_id INT,
                                    IN p_password varchar(255),
                                    IN p_email varchar(255))

BEGIN
    update users
    set email    = p_email,
        password = p_password
    where id = p_user_id;

    select * from users where id = p_user_id;
end |

/*
    Procedure pour recuperer un utilisateur par son email et son password
*/
drop procedure if exists get_user_credential;

create procedure get_user_credential(IN p_email varchar(255),
                                     IN p_password varchar(255))

begin

    select P.*,
           U.id         u_id,
           U.email      U_email,
           U.is_locked,
           U.updated_at U_updated_at,
           U.created_at U_created_at,
           p2.*,
           f.*
    from users U
             left join person P on U.person_id = P.id
             left join user_profil up on U.id = up.user_id
             left join profils p2 on up.profil_id = p2.id
             left join favoris f on U.id = f.user_id
    where U.email = p_email
      AND U.password = p_password;
end |

/*
    Procedure pour recuperer tout les utilisateurs
*/
drop procedure if exists get_all_user;

create procedure get_all_user()

begin
    select P.*, U.email U_email, U.id U_id, U.updated_at U_updated_at, U.created_at U_created_at, U.is_locked
    from users U
             inner join person P
                        on U.person_id = P.id;
end |

drop procedure if exists create_user_from_person;

create procedure create_user_from_person(IN p_id_person INT,
                                         IN p_email varchar(255),
                                         IN p_password varchar(255))

begin

    declare errno INt;
    declare msg TEXT;
    Declare new_id INT;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
        BEGIN
            ROLLBACK;
            GET STACKED DIAGNOSTICS CONDITION 1
                errno = MYSQL_ERRNO, msg = MESSAGE_TEXT;
            SELECT 'stacked DA before mapped insert' AS op, errno, msg;
        END;

    insert into users(email, password, person_id)
    values (p_email, p_password, p_id_person);

    set new_id = last_insert_id();

    select id into @id_profil from profils where label = 'user' limit 1;

    insert into user_profil(user_id, profil_id)
    values (new_id, @id_profil);


    select U.*, P.*, p2.*
    from users U
             inner join person P on U.person_id = P.id
             inner join user_profil up on U.id = up.user_id
             inner join profils p2 on up.profil_id = p2.id
    where U.id = new_id;

end |
