begin BEGIN EXECUTE IMMEDIATE 'drop table SIAAG.TAB_PERFIL  cascade constraints';EXCEPTION WHEN OTHERS THEN DBMS_OUTPUT.PUT_LINE(SQLCODE||' -ERROR- '||SQLERRM);  END;end;
/
begin BEGIN EXECUTE IMMEDIATE 'drop table SIAAG.TAB_TIPO_BENEFICIARIO  cascade constraints';EXCEPTION WHEN OTHERS THEN DBMS_OUTPUT.PUT_LINE(SQLCODE||' -ERROR- '||SQLERRM);  END;end;
/
begin BEGIN EXECUTE IMMEDIATE 'drop table SIAAG.TAB_TIPO_REQUERENTE  cascade constraints';EXCEPTION WHEN OTHERS THEN DBMS_OUTPUT.PUT_LINE(SQLCODE||' -ERROR- '||SQLERRM);  END;end;
/
begin BEGIN EXECUTE IMMEDIATE 'drop table SIAAG.TAB_ENDERECO  cascade constraints';EXCEPTION WHEN OTHERS THEN DBMS_OUTPUT.PUT_LINE(SQLCODE||' -ERROR- '||SQLERRM);  END;end;
/
begin BEGIN EXECUTE IMMEDIATE 'drop table SIAAG.TAB_PESSOA  cascade constraints';EXCEPTION WHEN OTHERS THEN DBMS_OUTPUT.PUT_LINE(SQLCODE||' -ERROR- '||SQLERRM);  END;end;
/
begin BEGIN EXECUTE IMMEDIATE 'drop table SIAAG.TAB_USUARIO  cascade constraints';EXCEPTION WHEN OTHERS THEN DBMS_OUTPUT.PUT_LINE(SQLCODE||' -ERROR- '||SQLERRM);  END;end;
/
