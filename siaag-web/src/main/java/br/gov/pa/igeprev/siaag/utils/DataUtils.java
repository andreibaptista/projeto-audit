package br.gov.pa.igeprev.siaag.utils;

import java.text.SimpleDateFormat;
import java.util.*;

public class DataUtils {

    private static String NomeDoMes(int i, int tipo) {
        String mes[] = {"janeiro", "fevereiro", "março", "abril", "maio", "junho", "julho", "agosto", "setembro",
                "outubro", "novembro", "dezembro"};

        if (tipo == 0)
            return (mes[i - 1]);
        else
            return (mes[i - 1].substring(0, 3)); // abreviado
    }

    private static String DiaDaSemana(int i, int tipo) {
        String diasem[] = {"domingo", "segunda-feira", "terça-feira", "quarta-feira", "quinta-feira", "sexta-feira",
                "sábado"};
        if (tipo == 0)
            return (diasem[i - 1]);
        else
            return (diasem[i - 1].substring(0, 3));
    }

    public static String DataPorExtenso(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int d = cal.get(Calendar.DATE);
        int m = cal.get(Calendar.MONTH)+1;
        int a = cal.get(Calendar.YEAR);

        return (d + " de " + NomeDoMes(m, 0) + " de " + a);
    }

    public static List<Date> DatasSemana(Date data) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        List<Date> datas = new ArrayList<>();
        for (int i = Calendar.MONDAY; i <= Calendar.FRIDAY; i++) {
            calendar.set(Calendar.DAY_OF_WEEK, i);
            if(calendar.get(Calendar.DAY_OF_WEEK) > Calendar.SUNDAY && calendar.get(Calendar.DAY_OF_WEEK) < Calendar.SATURDAY) { //TODO: Homologacao
            /*if(calendar.get(Calendar.DAY_OF_WEEK) > Calendar.SUNDAY && calendar.get(Calendar.DAY_OF_WEEK) < Calendar.WEDNESDAY) {*/
                datas.add(calendar.getTime());
            }
        }
        return datas;
    }

    public static List<Date> DatasMes(Date data) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int mes = calendar.get(Calendar.MONTH);
        List<Date> datas = new ArrayList<>();
        while (mes == calendar.get(Calendar.MONTH)) {
            if(calendar.get(Calendar.DAY_OF_WEEK) > Calendar.SUNDAY && calendar.get(Calendar.DAY_OF_WEEK) < Calendar.SATURDAY) { //TODO: Homologacao
            /*if(calendar.get(Calendar.DAY_OF_WEEK) > Calendar.SUNDAY && calendar.get(Calendar.DAY_OF_WEEK) < Calendar.WEDNESDAY) {*/
                datas.add(calendar.getTime());
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return datas;
    }

    public static List<Date> DatasAno(Date data) {
        //TODO: Melhorar performance na hora de salvar pro ano todo.
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        int ano = calendar.get(Calendar.YEAR);
        List<Date> datas = new ArrayList<>();
        while (ano == calendar.get(Calendar.YEAR)) {
            if(calendar.get(Calendar.DAY_OF_WEEK) > Calendar.SUNDAY && calendar.get(Calendar.DAY_OF_WEEK) < Calendar.SATURDAY) { //TODO: Homologacao
            /*if(calendar.get(Calendar.DAY_OF_WEEK) > Calendar.SUNDAY && calendar.get(Calendar.DAY_OF_WEEK) < Calendar.WEDNESDAY) {*/
                datas.add(calendar.getTime());
            }
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        return datas;
    }

    public static String FormatoHorarioHoraMinuto(Date date) {
        try {
            if (date != null) {
                return date.toString().substring(0, 5);
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    public static String FormatoDataHoraMinuto(Date date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            if (date != null) {
                return sdf.format(date);
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    public static List<Date> DatasEntre(Date dataInicial, Date dataFinal) {
        List<Date> datas = new ArrayList<>();
        Calendar calInicial = Calendar.getInstance();
        Calendar calFinal = Calendar.getInstance();
        calInicial.setTime(dataInicial);
        calFinal.setTime(dataFinal);

        while (calInicial.compareTo(calFinal) <= 0) {
            if (calInicial.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY
                    && calInicial.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
                datas.add(calInicial.getTime());
            }
            calInicial.add(Calendar.DAY_OF_MONTH, 1);
        }
        return datas;

    }

    public static Date DataCompleta(Date data, Date hora) {
        Calendar calData = Calendar.getInstance();
        calData.setTime(data);
        Calendar calHora = Calendar.getInstance();
        calHora.setTime(hora);

        Calendar calendar = Calendar.getInstance();
        calendar.set(calData.get(Calendar.YEAR), calData.get(Calendar.MONTH), calData.get(Calendar.DATE),
                calData.get(Calendar.HOUR), calData.get(Calendar.MINUTE));
        return calendar.getTime();
    }

    public static boolean DataExistenteEntreIntervalo(Date inicio, Date fim, Date dataInicio, Date dataFim) {
        Calendar calInicial = Calendar.getInstance();
        Calendar calFinal = Calendar.getInstance();
        calInicial.setTime(dataInicio);
        calFinal.setTime(dataFim);

        while (calInicial.compareTo(calFinal) <= 0) {
            if (calInicial.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY
                    && calInicial.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
                if (inicio.compareTo(calInicial.getTime()) == 0 ||
                        fim.compareTo(calInicial.getTime()) == 0) {
                    return true;
                }
            }
            calInicial.add(Calendar.DAY_OF_MONTH, 1);
        }
        return false;
    }

    public static String Formatada(Date data) {
        try{
            SimpleDateFormat sdff = new SimpleDateFormat("dd/MM/yyyy");
            return (sdff.format(data));
        }catch (Exception e){
            return data.toString();
        }
    }

    public static Date PrimeiraDataAno(Date data) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        return cal.getTime();
    }
    public static Date UltimaDataAno(Date data) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        cal.set(Calendar.DAY_OF_YEAR, cal.getMaximum(Calendar.DAY_OF_YEAR));
        return cal.getTime();
    }

    public static Date PrimeiraDataMes(Date data) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }
    public static Date UltimaDataMes(Date data) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        cal.set(Calendar.DAY_OF_MONTH, cal.getMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    public static Date PrimeiraDataSemana(Date data) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        cal.set(Calendar.DAY_OF_WEEK, 1);
        return cal.getTime();
    }
    public static Date UltimaDataSemana(Date data) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        cal.set(Calendar.DAY_OF_WEEK, cal.getMaximum(Calendar.DAY_OF_WEEK));
        return cal.getTime();
    }

    public static Date SomenteData(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH), cal.get(Calendar.DATE),0,0,0);
        return cal.getTime();
    }
}
