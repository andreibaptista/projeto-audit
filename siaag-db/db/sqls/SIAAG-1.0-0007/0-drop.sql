begin BEGIN EXECUTE IMMEDIATE 'drop table SIAAG.TAB_SETOR  cascade constraints';EXCEPTION WHEN OTHERS THEN DBMS_OUTPUT.PUT_LINE(SQLCODE||' -ERROR- '||SQLERRM);  END;end;
/
begin BEGIN EXECUTE IMMEDIATE 'drop table SIAAG.UNQ_SETOR_DESCRICAO  cascade constraints';EXCEPTION WHEN OTHERS THEN DBMS_OUTPUT.PUT_LINE(SQLCODE||' -ERROR- '||SQLERRM);  END;end;
/