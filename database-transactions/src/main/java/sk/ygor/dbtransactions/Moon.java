package sk.ygor.dbtransactions;

import org.springframework.jdbc.core.RowMapper;

public class Moon {

    private final Integer id;
    private final String name;
    private final Integer planetId;

    private Moon(Integer id, String name, Integer planetId) {
        this.id = id;
        this.name = name;
        this.planetId = planetId;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPlanetId() {
        return planetId;
    }

    public static RowMapper<Moon> mapper = (rs, rowNum) -> new Moon(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getInt("planet_id")
    );
}
