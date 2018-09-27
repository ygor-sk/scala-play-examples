package sk.ygor.dbtransactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class SolarSystemDAO {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public List<Planet> fetchAllPlanets() {
        return jdbcTemplate.query(
                "select id, name from planet",
                Planet.mapper
        );
    }

    public List<Moon> fetchPlanetMoons(int planetId) {
        return jdbcTemplate.query(
                "select id, name, planet_id from moon where planet_id = :planet_id",
                Collections.singletonMap("planet_id", planetId),
                Moon.mapper
        );
    }

    public List<String> fetchAllEvents() {
        return jdbcTemplate.query(
                "select event from audit",
                (rs, rowNum) -> rs.getString("event")
        );
    }

    public int insertPlanet(String planetName) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                "insert into planet (name) values (:name)",
                new MapSqlParameterSource("name", planetName),
                keyHolder
        );
        return keyHolder.getKey().intValue();
    }

    public void insertMoon(String moonName, int planetId) {
        jdbcTemplate.update(
                "insert into moon (name, planet_id) values (:name, :planet_id)",
                new MapSqlParameterSource()
                        .addValue("name", moonName)
                        .addValue("planet_id", planetId)
        );
    }

    public void insertEvent(String event) {
        jdbcTemplate.update(
                "insert into audit (event) values (:event)",
                new MapSqlParameterSource()
                        .addValue("event", event)
        );
    }
}
