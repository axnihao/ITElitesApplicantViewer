package cn.sicnu.itelites.db;

import cn.sicnu.itelites.vo.Applicant;

import java.util.ArrayList;
import java.util.List;

public class ApplicantDB {  //自定义的数据库采用单例设计，而不是应该交给Spring管理
    private static ApplicantDB i = new ApplicantDB();

    private List<Applicant> applicantList = new ArrayList<>(250);

    private ApplicantDB() {
    }

    public boolean addApplicant(Applicant applicant) {
        if (applicant == null) return false;
        i.applicantList.add(applicant);
        return true;
    }

    public boolean addApplicant(List<Applicant> list) {
        if (list == null || list.size() == 0) return false;
        i.applicantList.addAll(list);
        return true;
    }

    public static ApplicantDB getInstance() {
        return i;
    }

    public List<Applicant> getApplicantList() {
        return applicantList;
    }

    public void clear() {
        this.applicantList.clear();
    }
}
