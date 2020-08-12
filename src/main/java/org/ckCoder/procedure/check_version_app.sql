DELIMITER |
drop procedure if exists check_version|
create procedure check_version()
begin
     select version from version where version.id=last_insert_id();
end |
