package br.com.alura.blocodenotas.enumeration;

public enum FormatoData {
    BR("dd/MM/yyyy"),
    INT("yyyy-MM-dd"),
    DATETIME("yyyy-MM-dd HH:mm:ss"),
    DATETIMEBR("dd/MM/yyyy HH:mm:ss"),
    DATETIMEBRF("dd-MM-yyyy HH:mm:ss"),
    DIA("dd"),
    HORASS("HH:mm:ss"),
    HORA("HH:mm"),
    DATEPARAM("MM/dd/yyyy HH:mm:ss"),
    DATE("dd/MM/yyyy HH:mm");

    private String formato;

    private FormatoData(String formato) {
        this.formato = formato;
    }

    public String getFormato() {
        return formato;
    }

}
