package servicios;

import com.google.gson.annotations.Expose;

import java.util.List;

public class RespuestaPaginacion<T> {

    @Expose
    private List<T> elementos;

    @Expose
    private long totalElementos;

    @Expose
    private int numeroPagina;

    @Expose
    private int tamanioPagina;

    public RespuestaPaginacion(List<T> elementos, long totalElementos, int numeroPagina, int tamanioPagina) {
        this.elementos = elementos;
        this.totalElementos = totalElementos;
        this.numeroPagina = numeroPagina;
        this.tamanioPagina = tamanioPagina;
    }

    // Constructor vacío para deserialización JSON
    public RespuestaPaginacion() {
    }

    public List<T> getElementos() {
        return elementos;
    }

    public void setElementos(List<T> elementos) {
        this.elementos = elementos;
    }

    public long getTotalElementos() {
        return totalElementos;
    }

    public void setTotalElementos(long totalElementos) {
        this.totalElementos = totalElementos;
    }

    public int getNumeroPagina() {
        return numeroPagina;
    }

    public void setNumeroPagina(int numeroPagina) {
        this.numeroPagina = numeroPagina;
    }

    public int getTamanioPagina() {
        return tamanioPagina;
    }

    public void setTamanioPagina(int tamanioPagina) {
        this.tamanioPagina = tamanioPagina;
    }
}
