package me.son.excelkit.sample.member.domain.repository;

import lombok.RequiredArgsConstructor;
import me.son.excelkit.sample.member.domain.model.Member;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final int BATCH_SIZE = 1000;

    public void saveAll(List<Member> members) {

        for (int i = 0; i < members.size(); i += BATCH_SIZE) {
            List<Member> batch = members.subList(
                    i,
                    Math.min(i + BATCH_SIZE, members.size())
            );
            batchInsert(batch);
        }
    }

    private void batchInsert(List<Member> batch) {
        String sql = """
                    insert into member (name, email, age)
                    values (:name, :email, :age)
                """;

        List<MapSqlParameterSource> params = batch.stream()
                .map(m -> new MapSqlParameterSource()
                        .addValue("name", m.getName())
                        .addValue("email", m.getEmail())
                        .addValue("age", m.getAge())
                )
                .toList();

        jdbcTemplate.batchUpdate(sql, params.toArray(MapSqlParameterSource[]::new));
    }

    public int count() {

        String sql = "select count(*) from member";

        Integer count = jdbcTemplate.queryForObject(
                sql,
                new MapSqlParameterSource(),
                Integer.class
        );

        return count != null ? count : 0;
    }

    public List<Member> findAll() {

        String sql = """
                    select id, name, email, age
                    from member
                    order by id
                """;

        return jdbcTemplate.query(
                sql,
                new MapSqlParameterSource(),
                (rs, rowNum) -> new Member(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getInt("age")
                )
        );
    }
}
