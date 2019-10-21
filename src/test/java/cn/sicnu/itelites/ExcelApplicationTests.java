package cn.sicnu.itelites;

import cn.sicnu.itelites.dao.ApplicantDao;
import cn.sicnu.itelites.dao.TeamDao;
import cn.sicnu.itelites.db.ApplicantDB;
import cn.sicnu.itelites.db.TeamDB;
import cn.sicnu.itelites.handler.IConvertHandler;
import cn.sicnu.itelites.util.bean.ExcelGenerator;
import cn.sicnu.itelites.vo.Applicant;
import cn.sicnu.itelites.vo.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.util.List;

@SpringBootTest
class ExcelApplicationTests {

	@Autowired
	private ApplicantDao applicantDao;

	@Autowired
	private TeamDao teamDao;

	@Autowired
	private IConvertHandler convertHandler;

	@Test
	public void dao() {
		List<Team> teamList = teamDao.getAllTeams();
		TeamDB.getInstance().addTeam(teamList);
		List<Applicant> applicantList = applicantDao.getAllApplicants();
		ApplicantDB.getInstance().addApplicant(applicantList);
		System.out.println(teamList.size());
		System.out.println(applicantList.size());
		System.out.println(applicantList.get(3).getTeamOne().getTeamName());
	}

	@Test
	public void handler() {
		List<Applicant> list = convertHandler.getAllApplicants();
		list.parallelStream().forEach(e-> System.out.println(e.getApplicantNum()+" "+e.getApplicantName()));
	}

	@Test
	public void generate() {
		List<Applicant> list = convertHandler.getAllApplicants();
		ExcelGenerator generator = new ExcelGenerator();
		generator.addSheet(list, "all", null).generateExcel();
		System.out.println(generator.getFilePath());
		convertHandler.init();
	}

	@Test
	public void generateByFieldName() {
		List<Applicant> dataOne = convertHandler.getAllApplicants();
		List<Applicant> dataTwo = convertHandler.getApplicantsByTeamOneId(TeamDB.getInstance().getMap().get(1));
		ExcelGenerator generator = new ExcelGenerator();
		Field[] fields = new Field[5];
		try {
			fields[0] = Applicant.class.getDeclaredField("applicantNum");
			fields[1] = Applicant.class.getDeclaredField("applicantName");
			fields[2] = Applicant.class.getDeclaredField("qq");
			fields[3] = Applicant.class.getDeclaredField("teamOne");
			fields[4] = Applicant.class.getDeclaredField("createTime");
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		generator.addSheet(dataOne,"all",null)
				.addSheet(dataTwo,"编程组",fields)
				.generateExcel();
		System.out.println(generator.getFilePath());
		System.out.println(dataOne.size());
		System.out.println(dataTwo.size());
	}

	@Test
	public void testFields() {
		Class clazz = Applicant.class;
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			System.out.println(field.getName());
		}
	}


}
