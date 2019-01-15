package br.com.alura.blocodenotas.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import br.com.alura.blocodenotas.enumeration.FormatoData;

public class DateUtil {


    public static Date dtParce(String data, FormatoData formato) {
        try {
            return new SimpleDateFormat(formato.getFormato(), Locale.getDefault()).parse(data);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date getDateTime(String data) {
        return dtParce(data, FormatoData.DATETIME);
    }
}
