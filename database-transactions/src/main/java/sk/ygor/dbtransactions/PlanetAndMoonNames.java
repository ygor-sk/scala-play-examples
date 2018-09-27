package sk.ygor.dbtransactions;

import java.util.List;

public class PlanetAndMoonNames {

    private final String planetName;
    private final List<String> moonNames;

    public PlanetAndMoonNames(String planetName, List<String> moonNames) {
        this.planetName = planetName;
        this.moonNames = moonNames;
    }

    public String getPlanetName() {
        return planetName;
    }

    public List<String> getMoonNames() {
        return moonNames;
    }
}
