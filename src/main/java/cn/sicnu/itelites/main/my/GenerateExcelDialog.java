package cn.sicnu.itelites.main.my;

import cn.sicnu.itelites.db.TeamDB;
import cn.sicnu.itelites.handler.IConvertHandler;
import cn.sicnu.itelites.util.bean.ExcelGenerator;
import cn.sicnu.itelites.util.bean.swing.AfPanel;
import cn.sicnu.itelites.util.bean.swing.layout.AfColumnLayout;
import cn.sicnu.itelites.util.bean.swing.layout.AfRowLayout;
import cn.sicnu.itelites.vo.Applicant;
import cn.sicnu.itelites.vo.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.Rectangle;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

@Component
public class GenerateExcelDialog extends JDialog {

    private static IConvertHandler convertHandler;

    @Autowired
    public void setConvertHandler(IConvertHandler convertHandler) {
        GenerateExcelDialog.convertHandler = convertHandler;
    }

	JTextField fileSaveUrl = new JTextField(20);
	JComboBox<Team> teamList;

	JCheckBox qqBox = new JCheckBox("QQ");
    JCheckBox phoneBox = new JCheckBox("phone");
    JCheckBox timeBox = new JCheckBox("Time");

	JButton okButton = new JButton("确定");
	JButton cancelButton = new JButton("取消");
	JButton openFileUrl = new JButton("选择文件夹");

	// 默认是“取消"
	private Boolean retValue = null;
	
	public GenerateExcelDialog(JFrame owner)
	{
		super(owner, "生成Excel表格", true);
		this.setSize(300, 300);
		
		// 设置一个容器
		AfPanel root = new AfPanel();
		this.setContentPane(root);
		root.setLayout(new AfColumnLayout(10));
		root.padding(10);
		
		// 中间面板
		AfPanel main = new AfPanel();
		root.add(main, "1w"); // 占居中间区域
		main.setLayout(new AfColumnLayout(12));
		main.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		main.padding(10);

		teamList = createToolJcb();

		if (true) {
			AfPanel row = new AfPanel();
			main.add(row, "24px");
			row.setLayout(new AfRowLayout(10));
			row.add(new JLabel("大组 :"), "50px");
			row.add(teamList, "1w");
		}

		if (true) {
			AfPanel row = new AfPanel();
			main.add(row, "24px");
			row.setLayout(new AfRowLayout(10));
			row.add(new JLabel("选择需要额外添加的字段："), "1w");
		}

		if (true) {
			AfPanel row = new AfPanel();
			main.add(row, "24px");
			row.setLayout(new AfRowLayout(10));
			row.add(phoneBox, "1w");
			row.add(qqBox, "1w");
			row.add(timeBox, "1w");
		}

		if (true) {
			AfPanel row = new AfPanel();
			main.add(row, "24px");
			row.setLayout(new AfRowLayout(2));
			row.add(new JLabel("选择你要保存的文件夹，并命名"), "1w");
		}

		if (true) {
			AfPanel row = new AfPanel();
			main.add(row, "24");
			row.setLayout(new AfRowLayout(10));
			row.add(fileSaveUrl, "1w");
			row.add(openFileUrl, "auto");
		}


		// 底下
		AfPanel bottom = new AfPanel();
		root.add(bottom, "30px"); // 底部区域 30px
		bottom.setLayout(new AfRowLayout(10));
        bottom.add(cancelButton, "auto"); // 按钮靠右显示
        bottom.add(new JLabel(), "1w"); // 占位
		bottom.add(okButton, "auto"); // 按钮靠右显示

		openFileUrl.addActionListener(e -> {
			this.getSaveFileUrl();
		});

        cancelButton.addActionListener(e -> {
            this.setVisible(false);
        });

		// 此处 使用 Lambda 表达式 的写法，参考3.5 节
		okButton.addActionListener( (e)->{	//这里利用工作线程处理更合理！！！
			try {
				if (generateExcel()) {
					this.retValue = true;
				}
			} catch (Exception ex) {
				this.retValue = false;
			}
			this.setVisible(false);
		});
	}
	
	// 返回值为 true : 表示用户点了"确定"
	// 返回值为 false : 表示用户叉掉了窗口，或者点了”取消"
	public Boolean exec()
	{
		// 相对owner居中显示
		Rectangle frmRect = this.getOwner().getBounds();
		int width = this.getWidth();
		int height = this.getHeight();
		int x = frmRect.x + (frmRect.width - width)/2;
		int y = frmRect.y + (frmRect.height - height)/2;
		this.setBounds(x,y, width, height);
		
		// 显示窗口 ( 阻塞 ，直接对话框窗口被关闭)
		this.setVisible(true);
		
		return retValue;
	}

	protected JComboBox<Team> createToolJcb() {
		Vector<Team> vector = new Vector<>();
		TeamDB.getInstance().getTeamList().forEach(e -> vector.add(e));
		return new JComboBox<>(vector);
	}

    private boolean generateExcel() throws NoSuchFieldException {
        Team selectTeam = (Team) this.teamList.getSelectedItem();

        Field[] fields = Applicant.class.getDeclaredFields();
        Field[] validFields = new Field[fields.length];
        validFields[0] = Applicant.class.getDeclaredField("applicantNum");
        validFields[1] = Applicant.class.getDeclaredField("applicantName");
        for (int i = 2; i < fields.length; i++) {
			if (this.phoneBox.isSelected()) {
				validFields[i] = Applicant.class.getDeclaredField(this.phoneBox.getText());
			} else if (this.qqBox.isSelected()) {
				validFields[i] = Applicant.class.getDeclaredField(this.qqBox.getText().toLowerCase());
			} else if (this.timeBox.isSelected()) {
				validFields[i] = Applicant.class.getDeclaredField("create" + this.timeBox.getText());
			}
			break;
        }

        ExcelGenerator generator = new ExcelGenerator(this.fileSaveUrl.getText());
        generator.addSheet(convertHandler.getAllApplicants(), "all", null)
                .addSheet(convertHandler.getApplicantsByTeamOneId(selectTeam), "theOneTeam", validFields)
                .addSheet(convertHandler.getApplicantsByTeamTwoId(selectTeam), "theTwoTeam", validFields)
                .addSheet(convertHandler.getApplicantsByTeams(selectTeam), "theTeams", validFields);
        TeamDB.getInstance().getTeamList().forEach(e -> {
            if (e.getTeamId() != selectTeam.getTeamId()) {
                generator.addSheet(convertHandler.getApplicantsByTeamOneAndTeamTwo(selectTeam, e), selectTeam.getTeamName()+"&"+e.getTeamName(), validFields);
            }
        });
        generator.generateExcel();
        return true;
    }

	private void getSaveFileUrl()
	{
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".xlsx", "xlsx");
		fileChooser.setFileFilter(filter);
		int ret = fileChooser.showSaveDialog(this);
		if (ret == JFileChooser.APPROVE_OPTION)
		{
			File file = fileChooser.getSelectedFile();
			fileSaveUrl.setText(file.getAbsolutePath());
		}
	}

}
