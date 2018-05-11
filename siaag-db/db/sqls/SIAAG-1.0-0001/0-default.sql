DECLARE
    v_path   VARCHAR2(4000);
BEGIN
    SELECT
        value
    INTO
        v_path
    FROM
        v$parameter
    WHERE
        name = 'db_create_file_dest';

    IF
        (
            TRIM(v_path) IS NULL
        OR trim(v_path) = '' )
    THEN
        RAISE no_data_found;
    END IF;

EXCEPTION
    WHEN no_data_found THEN
        raise_application_error(-20000,'Necessário definir o caminho padrão de criação dos datafiles (alter system set db_create_file_dest = "C:\ORACLEXE\APP\ORACLE\ORADATA\" scope=both;). Usar consulta 
select * from v$datafile; para ver datafiles ja criados');
END;
/
