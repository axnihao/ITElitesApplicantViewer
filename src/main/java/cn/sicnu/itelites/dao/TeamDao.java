package cn.sicnu.itelites.dao;

import cn.sicnu.itelites.vo.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TeamDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Team> getAllTeams() {
        return jdbcTemplate.query("SELECT team_id,team_name FROM tb_team ORDER BY team_id", new RowMapper<Team>() {
            @Override
            public Team mapRow(ResultSet rs, int rowNum) throws SQLException {
                Team team = new Team();
                team.setTeamId(rs.getInt(1));
                team.setTeamName(rs.getString(2));
                return team;
            }
        });
    }

}
