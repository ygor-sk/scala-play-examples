package sk.ygor.dbtransactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
class WebController {

    @Autowired
    private SolarSystemService solarSystemService;

    @Autowired
    private DatabaseUtils databaseUtils;

    @RequestMapping("/listPlanetsAndEvents")
    String listPlanetsAndEvents() {
        StringBuilder result = new StringBuilder();
        result.append("<h1>Planets and moons</h1>");
        for (PlanetAndMoonNames pm : solarSystemService.fetchAllPlanetsAndMoons()) {
            result.append(pm.getPlanetName()).append("<br>");
            if (pm.getMoonNames().isEmpty()) {
                result.append("- [no moons]").append("<br>");
            } else {
                for (String moonName : pm.getMoonNames()) {
                    result.append("- ").append(moonName).append("<br>");
                }
            }
        }

        result.append("<h1>Events</h1>");
        List<String> events = solarSystemService.fetchAllEvents();
        result.append(String.join("<br>", events));

        return result.toString();
    }

    @RequestMapping("/addPlanetNoTransaction")
    String addPlanetNoTransaction() {
        PlanetAndMoonNames pm = solarSystemService.addPlanetNoTransaction(false);
        return formatCreatedPlanetMessage(pm);
    }

    @RequestMapping("/addPlanetProgrammaticTransaction")
    String addPlanetProgrammaticTransaction() {
        PlanetAndMoonNames pm = solarSystemService.addPlanetProgrammaticTransaction(false);
        return formatCreatedPlanetMessage(pm);
    }

    @RequestMapping("/addPlanetAnnotatedTransaction")
    String addPlanetAnnotatedTransaction() {
        PlanetAndMoonNames pm = solarSystemService.addPlanetAnnotatedTransaction(false);
        return formatCreatedPlanetMessage(pm);
    }

    @RequestMapping("/addPlanetNoTransactionUseInnerTx")
    String addPlanetNoTransactionUseInnerTx() {
        PlanetAndMoonNames pm = solarSystemService.addPlanetNoTransaction(true);
        return formatCreatedPlanetMessage(pm);
    }

    @RequestMapping("/addPlanetProgrammaticTransactionUseInnerTx")
    String addPlanetProgrammaticTransactionUseInnerTx() {
        PlanetAndMoonNames pm = solarSystemService.addPlanetProgrammaticTransaction(true);
        return formatCreatedPlanetMessage(pm);
    }

    @RequestMapping("/addPlanetAnnotatedTransactionUseInnerTx")
    String addPlanetAnnotatedTransactionUseInnerTx() {
        PlanetAndMoonNames pm = solarSystemService.addPlanetAnnotatedTransaction(true);
        return formatCreatedPlanetMessage(pm);
    }

    @RequestMapping("/restorePlanets")
    String restorePlanets() {
        databaseUtils.setupDatabase();
        return "List of planets was restored";
    }

    private String formatCreatedPlanetMessage(PlanetAndMoonNames pm) {
        return "Created planet " + pm.getPlanetName() + " and moons " + String.join(", ", pm.getMoonNames());
    }

}