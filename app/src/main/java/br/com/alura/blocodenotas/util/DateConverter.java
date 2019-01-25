package br.com.alura.blocodenotas.util;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.time.LocalDateTime;

public class DateConverter {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static StringBuilder toString(LocalDateTime data) {
        StringBuilder dataFinal = null;

        dataFinal.append(data.getDayOfMonth()).append("/")
                 .append(data.getMonth()).append("/")
                 .append(data.getYear()).append(" - ")
                 .append(data.getHour()).append(":")
                 .append(data.getMinute()).append(":")
                 .append(data.getSecond());
        return dataFinal;
    }
}
