GRANT DBA TO siaag;
grant create session to siaag  WITH ADMIN OPTION;
grant drop tablespace, create tablespace to siaag;
grant drop user, create user to siaag;
grant drop any synonym, create any synonym to siaag;
grant drop any sequence, create any sequence to siaag;
grant drop any table, alter any table, create any table to siaag;
grant drop any index, create any index to siaag;
grant drop any view, create any view to siaag;
grant drop any view, create any TYPE to siaag;

grant drop any materialized view, create any materialized view to siaag;
grant drop any trigger, create any trigger to siaag;
grant drop any procedure, create any procedure to siaag;
grant create any job to siaag;

grant select any dictionary to siaag;

grant insert any table to siaag;
grant update any table to siaag;
grant delete any table to siaag;
grant select any table to siaag;

gRANT comment any table to siaag;