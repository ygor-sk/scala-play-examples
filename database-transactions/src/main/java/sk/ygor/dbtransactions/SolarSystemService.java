package sk.ygor.dbtransactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings("Duplicates")
@Service
public class SolarSystemService {

    @Autowired
    private SolarSystemDAO solarSystemDAO;

    @Autowired
    TransactionTemplate transactionTemplate;

    public List<PlanetAndMoonNames> fetchAllPlanetsAndMoons() {
        return solarSystemDAO.fetchAllPlanets()
                .stream()
                .map(planet -> new PlanetAndMoonNames(
                                planet.getName(),
                                solarSystemDAO.fetchPlanetMoons(planet.getId())
                                        .stream()
                                        .map(Moon::getName)
                                        .collect(Collectors.toList())
                        )
                )
                .collect(Collectors.toList());
    }

    public List<String> fetchAllEvents() {
        return solarSystemDAO.fetchAllEvents();
    }

    public PlanetAndMoonNames addPlanetNoTransaction(boolean useInnerTx) {
        insertEvent("addPlanetNoTransaction");
        PlanetAndMoonNames pm = useInnerTx ? addPlanetTx() : addPlanetNoTx();
        return insertEvent("addPlanetNoTransaction", pm);
    }

    public PlanetAndMoonNames addPlanetProgrammaticTransaction(boolean useInnerTx) {
        return transactionTemplate.execute(txStatus -> {
            insertEvent("addPlanetProgrammaticTransaction");
            PlanetAndMoonNames pm = useInnerTx ? addPlanetTx() : addPlanetNoTx();
            return insertEvent("addPlanetProgrammaticTransaction", pm);
        });
    }

    @Transactional
    public PlanetAndMoonNames addPlanetAnnotatedTransaction(boolean useInnerTx) {
        insertEvent("addPlanetAnnotatedTransaction");
        PlanetAndMoonNames pm = useInnerTx ? addPlanetTx() : addPlanetNoTx();
        return insertEvent("addPlanetAnnotatedTransaction", pm);
    }

    private PlanetAndMoonNames addPlanetNoTx() {
        PlanetAndMoonNames pm = generatePlanetAndMoons();
        int planetId = solarSystemDAO.insertPlanet(pm.getPlanetName());
        maybeThrowException();
        pm.getMoonNames().forEach(moonName -> solarSystemDAO.insertMoon(moonName, planetId));
        return pm;
    }

    private PlanetAndMoonNames addPlanetTx() {
        PlanetAndMoonNames pm = generatePlanetAndMoons();
        return transactionTemplate.execute(status -> {
            int planetId = solarSystemDAO.insertPlanet(pm.getPlanetName());
            maybeThrowException();
            pm.getMoonNames().forEach(moonName -> solarSystemDAO.insertMoon(moonName, planetId));
            return pm;
        });
    }

    private PlanetAndMoonNames generatePlanetAndMoons() {
        Random random = new Random();
        String planetName = "Planet" + random.nextInt(100000);

        int moonCount = random.nextInt(3) + 1;
        List<String> moonNames = IntStream.range(0, moonCount)
                .mapToObj(i -> "Moon_" + planetName + "_" + (char) (i + 65))
                .collect(Collectors.toList());

        return new PlanetAndMoonNames(planetName, moonNames);
    }

    private void insertEvent(String event) {
        solarSystemDAO.insertEvent("[Start]: " + event);
    }

    private PlanetAndMoonNames insertEvent(String event, PlanetAndMoonNames pm) {
        solarSystemDAO.insertEvent("[End]: " + event + ": " + pm.getPlanetName());
        return pm;
    }

    private void maybeThrowException() {
        if (new Random().nextBoolean()) {
            throw new RuntimeException("Simulated exception");
        }
    }
}
