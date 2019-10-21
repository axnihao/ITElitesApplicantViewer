package cn.sicnu.itelites.handler;

import cn.sicnu.itelites.vo.Applicant;
import cn.sicnu.itelites.vo.Team;

import java.util.List;

public interface IConvertHandler {

    public void init();

    /**
     * 查询所有报名的学生
     * @return
     */
    public List<Applicant> getAllApplicants();

    /**
     * 查询所有填了{@team}志愿的学生，无论第一还是第二志愿
     * @param team 所需要查询的大组
     * @return
     */
    public List<Applicant> getApplicantsByTeams(Team team);

    /**
     * 查询所有的第一志愿的学生
     * @return
     */
    public List<Applicant> getApplicantsByTeamOne();

    /**
     * 查询所有第二志愿的学生，则第一和第二志愿重叠的学生忽略
     * @return
     */
    public List<Applicant> getApplicantsByTeamTwo();

    /**
     * 查询第一志愿选择了{@team}志愿的学生
     * @param team
     * @return
     */
    public List<Applicant> getApplicantsByTeamOneId(Team team);

    /**
     * 查询第二志愿选择了{@team}志愿的学生
     * @param team
     * @return
     */
    public List<Applicant> getApplicantsByTeamTwoId(Team team);

    /**
     * 查询第一志愿选择了{@teamOne},第二志愿选择了{@teamTwo}的学生
     * @param teamOne
     * @param teamTwo
     * @return
     */
    public List<Applicant> getApplicantsByTeamOneAndTeamTwo(Team teamOne, Team teamTwo);

}
