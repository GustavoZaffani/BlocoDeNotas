package br.com.alura.blocodenotas.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public String formataDataHora(Date date) {
        String dataHoraFormatada = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.getDefault()).format(date);
        return dataHoraFormatada;
    }

}
