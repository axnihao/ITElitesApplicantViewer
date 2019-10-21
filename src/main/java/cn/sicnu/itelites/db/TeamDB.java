package cn.sicnu.itelites.db;

import cn.sicnu.itelites.vo.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TeamDB { //自定义的数据库采用单例设计，而不是应该交给Spring管理

    private static TeamDB i = new TeamDB();

    private List<Team> teamList = new ArrayList<>(5);
    private Map<Integer, Team> map = new ConcurrentHashMap<>();

    private TeamDB() {
    }

    public boolean addTeam(Team team) {
        if (team == null) return false;
        i.teamList.add(team);
        i.list2MapConvert(team);
        return true;
    }

    public boolean addTeam(List<Team> list) {
        if (list == null || list.size() == 0) return false;
        i.teamList.addAll(list);
        i.list2MapConvert(list);
        return true;
    }


    public static TeamDB getInstance() {
        return i;
    }

    public List<Team> getTeamList() {
        return teamList;
    }

    public Map<Integer, Team> getMap() {
        return map;
    }

    private void list2MapConvert(List<Team> list) {
        list.parallelStream().forEach(e->list2MapConvert(e));
    }

    private void list2MapConvert(Team team) {
        i.map.put(team.getTeamId(), team);
    }

    public void clear() {
        this.teamList.clear();
        this.map.clear();
    }
}
