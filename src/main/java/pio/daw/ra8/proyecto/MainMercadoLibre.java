package pio.daw.ra8.proyecto;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import pio.daw.ra8.proyecto.model.Individuo;
import pio.daw.ra8.proyecto.model.Intercambio;
import pio.daw.ra8.proyecto.model.Simulacion;
import pio.daw.ra8.proyecto.repository.SimulacionRepository;
import pio.daw.ra8.proyecto.service.SimulacionService;
import pio.daw.ra8.proyecto.ui.GraficaDistribucion;
import pio.daw.ra8.util.JPAUtil;

import java.util.List;

public class MainMercadoLibre {

    public static void main(String[] args) {

        System.out.println("=== SIMULACION MERCADO LIBRE ===\n");

        EntityManagerFactory emf = JPAUtil.crearEMF("target/mercado.odb");
        EntityManager em = emf.createEntityManager();

        try {
            SimulacionService service = new SimulacionService(em);
            Simulacion sim = service.ejecutar("Simulacion_1", 100, 100.0, 10000);
            System.out.println("Completada: " + sim + "\n");

            SimulacionRepository repo = new SimulacionRepository(em);

            System.out.println("--- RANKING ---");
            List<Individuo> ranking = repo.rankingIndividuos(sim);
            ranking.forEach(System.out::println);

            System.out.println("\n--- MAS RICO ---");
            System.out.println(repo.masRico(sim));

            System.out.println("\n--- MAS POBRE ---");
            System.out.println(repo.masPobre(sim));

            System.out.println("\n--- ESTADISTICAS ---");
            repo.estadisticas(sim);

            System.out.println("\n--- CON MAS DEL 50% DEL SALDO INICIAL ---");
            System.out.println(repo.contarRicos(sim) + " individuos");

            System.out.println("\n--- TOP 10 INTERCAMBIOS ---");
            List<Intercambio> top = repo.top10Intercambios(sim);
            top.forEach(System.out::println);

            System.out.println("\nAbriendo grafica...");
            GraficaDistribucion.mostrar(ranking);

        } finally {
            em.close();
            emf.close();
        }
    }
}