package cn.sicnu.itelites.vo;

import java.io.Serializable;
import java.util.Date;

public class Team implements Serializable
{
    private Integer teamId;     //数据库自动生成的ID主键
    private String teamName;    //大组名称


    public Team() {
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public String toString() {
        return "[" + teamName + "]";
    }
}
