begin BEGIN EXECUTE IMMEDIATE 'drop table SIAAG.TAB_BLOQUEIO_AGENDA  cascade constraints';EXCEPTION WHEN OTHERS THEN DBMS_OUTPUT.PUT_LINE(SQLCODE||' -ERROR- '||SQLERRM);  END;end;
/
begin BEGIN EXECUTE IMMEDIATE 'drop table SIAAG.TAB_AGENDA_ITEM  cascade constraints';EXCEPTION WHEN OTHERS THEN DBMS_OUTPUT.PUT_LINE(SQLCODE||' -ERROR- '||SQLERRM);  END;end;
/
begin BEGIN EXECUTE IMMEDIATE 'drop table SIAAG.TAB_AGENDA  cascade constraints';EXCEPTION WHEN OTHERS THEN DBMS_OUTPUT.PUT_LINE(SQLCODE||' -ERROR- '||SQLERRM);  END;end;
/
begin BEGIN EXECUTE IMMEDIATE 'drop table SIAAG.TAB_HORARIO  cascade constraints';EXCEPTION WHEN OTHERS THEN DBMS_OUTPUT.PUT_LINE(SQLCODE||' -ERROR- '||SQLERRM);  END;end;
/