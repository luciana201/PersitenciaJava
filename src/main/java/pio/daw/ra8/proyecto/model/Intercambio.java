package pio.daw.ra8.proyecto.model;

import jakarta.persistence.*;

@Entity
public class Intercambio {

    @Id
    @GeneratedValue
    private long id;

    private int numRonda;
    private double importe;

    @ManyToOne(fetch = FetchType.LAZY)
    private Individuo emisor;

    @ManyToOne(fetch = FetchType.LAZY)
    private Individuo receptor;

    @ManyToOne(fetch = FetchType.LAZY)
    private Simulacion simulacion;

    public Intercambio() {}

    public Intercambio(int numRonda, double importe,
                       Individuo emisor, Individuo receptor,
                       Simulacion simulacion) {
        this.numRonda = numRonda;
        this.importe = importe;
        this.emisor = emisor;
        this.receptor = receptor;
        this.simulacion = simulacion;
    }

    public long getId() { return id; }
    public int getNumRonda() { return numRonda; }
    public double getImporte() { return importe; }
    public Individuo getEmisor() { return emisor; }
    public Individuo getReceptor() { return receptor; }
    public Simulacion getSimulacion() { return simulacion; }

    @Override
    public String toString() {
        return "Intercambio{ronda=" + numRonda +
               ", importe=" + String.format("%.2f", importe) +
               ", de=" + emisor.getNombre() +
               " -> " + receptor.getNombre() + "}";
    }
}