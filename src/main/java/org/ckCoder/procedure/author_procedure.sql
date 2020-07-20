delimiter |

drop procedure if exists find_all_author|
create procedure find_all_author()
begin
    select * from author a left join person as p on a.person_id = p.id;
end |