package pio.daw.ra8.proyecto.repository;

import jakarta.persistence.EntityManager;
import pio.daw.ra8.proyecto.model.Individuo;
import pio.daw.ra8.proyecto.model.Intercambio;
import pio.daw.ra8.proyecto.model.Simulacion;

import java.util.List;

public class SimulacionRepository {

    private final EntityManager em;

    public SimulacionRepository(EntityManager em) {
        this.em = em;
    }

    public List<Individuo> rankingIndividuos(Simulacion sim) {
        return em.createQuery(
            "SELECT i FROM Individuo i WHERE i.simulacion = :sim ORDER BY i.saldoActual DESC",
            Individuo.class).setParameter("sim", sim).getResultList();
    }

    public Individuo masRico(Simulacion sim) {
        return em.createQuery(
            "SELECT i FROM Individuo i WHERE i.simulacion = :sim ORDER BY i.saldoActual DESC",
            Individuo.class).setParameter("sim", sim).setMaxResults(1).getSingleResult();
    }

    public Individuo masPobre(Simulacion sim) {
        return em.createQuery(
            "SELECT i FROM Individuo i WHERE i.simulacion = :sim ORDER BY i.saldoActual ASC",
            Individuo.class).setParameter("sim", sim).setMaxResults(1).getSingleResult();
    }

    public void estadisticas(Simulacion sim) {
        Double media = em.createQuery(
            "SELECT AVG(i.saldoActual) FROM Individuo i WHERE i.simulacion = :sim",
            Double.class).setParameter("sim", sim).getSingleResult();
        Double maximo = em.createQuery(
            "SELECT MAX(i.saldoActual) FROM Individuo i WHERE i.simulacion = :sim",
            Double.class).setParameter("sim", sim).getSingleResult();
        Double minimo = em.createQuery(
            "SELECT MIN(i.saldoActual) FROM Individuo i WHERE i.simulacion = :sim",
            Double.class).setParameter("sim", sim).getSingleResult();
        System.out.println("  Media:  " + String.format("%.2f", media));
        System.out.println("  Maximo: " + String.format("%.2f", maximo));
        System.out.println("  Minimo: " + String.format("%.2f", minimo));
    }

    public long contarRicos(Simulacion sim) {
        List<Individuo> todos = em.createQuery(
            "SELECT i FROM Individuo i WHERE i.simulacion = :sim",
            Individuo.class).setParameter("sim", sim).getResultList();
        
        double mitad = sim.getSaldoInicial() * 0.5;
        return todos.stream().filter(i -> i.getSaldoActual() > mitad).count();
    }

    public List<Intercambio> top10Intercambios(Simulacion sim) {
        return em.createQuery(
            "SELECT i FROM Intercambio i WHERE i.simulacion = :sim ORDER BY i.importe DESC",
            Intercambio.class).setParameter("sim", sim).setMaxResults(10).getResultList();
    }
}