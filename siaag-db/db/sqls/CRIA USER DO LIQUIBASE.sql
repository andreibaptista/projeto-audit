create user liquibase IDENTIFIED by liquibase123  QUOTA UNLIMITED ON USERS;

GRANT DBA TO liquibase;
grant create session to liquibase  WITH ADMIN OPTION;
grant drop tablespace, create tablespace to liquibase;
grant drop user, create user to liquibase;
grant drop any synonym, create any synonym to liquibase;
grant drop any sequence, create any sequence to liquibase;
grant drop any table, alter any table, create any table to liquibase;
grant drop any index, create any index to liquibase;
grant drop any view, create any view to liquibase;
grant drop any view, create any TYPE to liquibase;

grant drop any materialized view, create any materialized view to liquibase;
grant drop any trigger, create any trigger to liquibase;
grant drop any procedure, create any procedure to liquibase;
grant create any job to liquibase;

grant select any dictionary to liquibase;

grant insert any table to liquibase;
grant update any table to liquibase;
grant delete any table to liquibase;
grant select any table to liquibase;

gRANT comment any table to liquibase;