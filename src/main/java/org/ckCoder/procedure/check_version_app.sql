DELIMITER |
drop procedure if exists check_version|
create procedure check_version()
begin
    select max(id) into @v_id from version;
     select version from version where version.id=@v_id;
end |
