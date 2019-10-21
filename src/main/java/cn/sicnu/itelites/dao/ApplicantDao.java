package cn.sicnu.itelites.dao;

import cn.sicnu.itelites.db.TeamDB;
import cn.sicnu.itelites.vo.Applicant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ApplicantDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Applicant> getAllApplicants() {
        return jdbcTemplate.query("SELECT applicant_num,applicant_name,phone,qq,team_one,team_two,create_time FROM tb_applicant ORDER BY applicant_num",
                new RowMapper<Applicant>() {
                    @Override
                    public Applicant mapRow(ResultSet resultSet, int i) throws SQLException {
                        Applicant applicant = new Applicant();
                        applicant.setApplicantNum(resultSet.getLong(1));
                        applicant.setApplicantName(resultSet.getString(2));
                        applicant.setPhone(resultSet.getString(3));
                        applicant.setQq(resultSet.getString(4));
                        applicant.setTeamOne(TeamDB.getInstance().getMap().get(resultSet.getInt(5)));
                        applicant.setTeamTwo(TeamDB.getInstance().getMap().get(resultSet.getInt(6)));
                        applicant.setCreateTime(resultSet.getDate(7));
                        return applicant;
                    }
                });
    }
}
