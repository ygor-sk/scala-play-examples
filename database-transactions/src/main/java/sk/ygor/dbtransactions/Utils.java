package sk.ygor.dbtransactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

@Component
public class Utils {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void setupDatabase() {
        setupPlanets();
        setupMoons();
        setupAudit();
    }

    private void setupPlanets() {
        jdbcTemplate.execute("drop table if exists planet");
        jdbcTemplate.execute("create table planet (id INT AUTO_INCREMENT, name VARCHAR(255))");
        jdbcTemplate.batchUpdate(
                "insert into planet (id, name) values (?, ?)",
                Arrays.asList(
                        new Object[]{3, "Earth"},
                        new Object[]{4, "Mars"},
                        new Object[]{5, "Jupiter"},
                        new Object[]{6, "Saturn"},
                        new Object[]{7, "Uranus"},
                        new Object[]{8, "Neptune"}
                )
        );
    }

    private void setupMoons() {
        jdbcTemplate.execute("drop table if exists moon");
        jdbcTemplate.execute("create table moon (id INT AUTO_INCREMENT, name VARCHAR(255), planet_id INT)");
        jdbcTemplate.batchUpdate(
                "insert into moon (id, name, planet_id) values (?, ?, ?)",
                Arrays.asList(
                        new Object[]{1, "Moon", 3},
                        new Object[]{2, "Phobos", 4},
                        new Object[]{3, "Deimos", 4},
                        new Object[]{4, "Io", 5},
                        new Object[]{5, "Europa", 5},
                        new Object[]{6, "Ganymede", 5},
                        new Object[]{7, "Callisto", 5},
                        new Object[]{8, "Titan", 6},
                        new Object[]{9, "Enceladus", 6},
                        new Object[]{10, "Miranda", 7},
                        new Object[]{11, "Ariel", 7},
                        new Object[]{12, "Triton", 8}
                )
        );
    }

    private void setupAudit() {
        jdbcTemplate.execute("drop table if exists audit");
        jdbcTemplate.execute("create table audit (id INT AUTO_INCREMENT, event VARCHAR(255))");
        jdbcTemplate.batchUpdate(
                "insert into audit (id, event) values (?, ?)",
                Collections.singletonList(
                        new Object[]{1, "Default solar system was created"}
                )
        );
    }

    public void maybeThrowException() {
        if (new Random().nextBoolean()) {
            throw new RuntimeException("Simulated exception");
        }
    }

}
