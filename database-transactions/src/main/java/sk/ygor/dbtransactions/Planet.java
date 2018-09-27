package sk.ygor.dbtransactions;

import org.springframework.jdbc.core.RowMapper;

public class Planet {

    private final Integer id;
    private final String name;

    private Planet(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static RowMapper<Planet> mapper = (rs, rowNum) -> new Planet(
            rs.getInt("id"),
            rs.getString("name")
    );
}
