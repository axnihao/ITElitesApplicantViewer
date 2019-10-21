package cn.sicnu.itelites.vo;


import cn.sicnu.itelites.util.bean.Date2StringConverter;
import cn.sicnu.itelites.util.bean.Team2StringConverter;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.converters.date.DateStringConverter;
import com.alibaba.excel.converters.longconverter.LongStringConverter;

import java.io.Serializable;
import java.util.Date;

public class Applicant implements Serializable
{
    @ExcelIgnore
    private Integer applicantId;  //数据库自动生成的学生ID主键
    @ExcelProperty(value = "学号", index = 0, converter = LongStringConverter.class)
    private Long applicantNum;    //学号
    @ExcelProperty(value = "姓名", index = 1)
    private String applicantName; //学生姓名
    @ExcelProperty(value = "电话", index = 2)
    private String phone;       //电话号码
    @ExcelProperty(value = "QQ", index = 3)
    private String qq;          //学生QQ
    @ExcelProperty(value = "报名时间", index = 4, converter = Date2StringConverter.class)
    private Date createTime;    //创建时间
    @ExcelIgnore
    private Date lastEditTime;  //最后修改时间
    @ExcelIgnore
    private Team teamPass;      //通过的大组
    @ExcelProperty(value = "第一志愿", index = 5, converter = Team2StringConverter.class)
    private Team teamOne;       //大组第一志愿
    @ExcelProperty(value = "第二志愿", index = 6, converter = Team2StringConverter.class)
    private Team teamTwo;       //大组第二志愿
    @ExcelIgnore
    private String teamReason;  //填写加入IT培优的感受
    @ExcelIgnore
    private Integer validate;   //当前状态（-1、未通过，0、等待中，1、通过）
    @ExcelIgnore
    private Group groupPass;    //通过的小组
    @ExcelIgnore
    private Group groupOne;     //小组第一志愿
    @ExcelIgnore
    private Group groupTwo;     //小组第二志愿
    @ExcelIgnore
    private String groupReason;  //填写你所选择小组的感受？？？

    public Applicant() {
    }

    public Integer getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(Integer applicantId) {
        this.applicantId = applicantId;
    }

    public Long getApplicantNum() {
        return applicantNum;
    }

    public void setApplicantNum(Long applicantNum) {
        this.applicantNum = applicantNum;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public Team getTeamPass() {
        return teamPass;
    }

    public void setTeamPass(Team teamPass) {
        this.teamPass = teamPass;
    }

    public Team getTeamOne() {
        return teamOne;
    }

    public void setTeamOne(Team teamOne) {
        this.teamOne = teamOne;
    }

    public Team getTeamTwo() {
        return teamTwo;
    }

    public void setTeamTwo(Team teamTwo) {
        this.teamTwo = teamTwo;
    }

    public Integer getValidate() {
        return validate;
    }

    public void setValidate(Integer validate) {
        this.validate = validate;
    }

    public Group getGroupPass() {
        return groupPass;
    }

    public void setGroupPass(Group groupPass) {
        this.groupPass = groupPass;
    }

    public Group getGroupOne() {
        return groupOne;
    }

    public void setGroupOne(Group groupOne) {
        this.groupOne = groupOne;
    }

    public Group getGroupTwo() {
        return groupTwo;
    }

    public void setGroupTwo(Group groupTwo) {
        this.groupTwo = groupTwo;
    }

    public String getTeamReason() {
        return teamReason;
    }

    public void setTeamReason(String teamReason) {
        this.teamReason = teamReason;
    }

    public String getGroupReason() {
        return groupReason;
    }

    public void setGroupReason(String groupReason) {
        this.groupReason = groupReason;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(Date lastEditTime) {
        this.lastEditTime = lastEditTime;
    }
}
