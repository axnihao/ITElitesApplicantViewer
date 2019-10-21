package cn.sicnu.itelites.handler.impl;

import cn.sicnu.itelites.dao.ApplicantDao;
import cn.sicnu.itelites.dao.TeamDao;
import cn.sicnu.itelites.db.ApplicantDB;
import cn.sicnu.itelites.db.TeamDB;
import cn.sicnu.itelites.handler.IConvertHandler;
import cn.sicnu.itelites.util.ConvertUtil;
import cn.sicnu.itelites.vo.Applicant;
import cn.sicnu.itelites.vo.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class ConvertHandlerImpl implements IConvertHandler {

    @Autowired
    private ApplicantDao applicantDao;

    @Autowired
    private TeamDao teamDao;

    /**
     * 在bean装配期间进行本地数据库的初始化
     * 这里并不进行重复的DAO层操作，避免过多且繁杂的数据流过滤操作带来的网络延迟问题
     * 还有因素是数据库的数据并不经常更新，且此单机程序主要是进行Excel的生成，不知道理解的对不对 :)
     * 此函数可以有其他事件触发进行重新初始化
     */
    @PostConstruct
    public void init() {
        TeamDB.getInstance().clear();
        ApplicantDB.getInstance().clear();
        List<Team> teamList = teamDao.getAllTeams();
        TeamDB.getInstance().addTeam(teamList);
        List<Applicant> applicantList = applicantDao.getAllApplicants();
        ApplicantDB.getInstance().addApplicant(applicantList);
    }

    @Override
    public List<Applicant> getAllApplicants() {
        return ApplicantDB.getInstance().getApplicantList();
    }

    @Override
    public List<Applicant> getApplicantsByTeams(Team team) {
        return ConvertUtil.getApplicantListByTeams(ApplicantDB.getInstance().getApplicantList(), team);
    }

    @Override
    public List<Applicant> getApplicantsByTeamOne() {
        return ConvertUtil.getTeamOne(ApplicantDB.getInstance().getApplicantList());
    }

    @Override
    public List<Applicant> getApplicantsByTeamTwo() {
        return ConvertUtil.getTeamTwo(ApplicantDB.getInstance().getApplicantList());
    }

    @Override
    public List<Applicant> getApplicantsByTeamOneId(Team team) {
        return ConvertUtil.getApplicantListByTeamOne(ApplicantDB.getInstance().getApplicantList(), team);
    }

    @Override
    public List<Applicant> getApplicantsByTeamTwoId(Team team) {
        return ConvertUtil.getApplicantListByTeamTwo(ApplicantDB.getInstance().getApplicantList(), team);
    }

    @Override
    public List<Applicant> getApplicantsByTeamOneAndTeamTwo(Team teamOne, Team teamTwo) {
        return ConvertUtil.getApplicantListByTeamOneAndTeamTwo(ApplicantDB.getInstance().getApplicantList(), teamOne, teamTwo);
    }

}
