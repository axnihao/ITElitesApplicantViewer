package cn.sicnu.itelites.main.my;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.net.URL;
import java.util.List;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import cn.sicnu.itelites.db.ApplicantDB;
import cn.sicnu.itelites.db.TeamDB;
import cn.sicnu.itelites.handler.IConvertHandler;
import cn.sicnu.itelites.main.renderer.DateColumnRenderer;
import cn.sicnu.itelites.main.renderer.IdColumnRenderer;
import cn.sicnu.itelites.main.renderer.MyColumnRenderer;
import cn.sicnu.itelites.vo.Applicant;
import cn.sicnu.itelites.vo.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyFrame extends JFrame {

	@Autowired
	private IConvertHandler convertHandler;

	JPanel root = new JPanel();
	JTable table = null;
	JComboBox<Team> teamOneList;
	JComboBox<Team> teamTwoList;
	
	// dataList: 维护所有记录  , tableModel: 要显示出来的记录
	List<Applicant> dataList = ApplicantDB.getInstance().getApplicantList();
	DefaultTableModel tableModel = new DefaultTableModel();
	
	
	JButton refreshButton;
	JTextField searchField = new JTextField();
	JButton generateDialog = new JButton("生成Excel");
	JButton helpButton;
	
	public MyFrame() {

		// Content Pane		
		this.setContentPane(root);
		root.setLayout(new BorderLayout());
		
		// 表格初始化
		initTable();
		
		// 初始化工具栏
		initToolBar();
		
		// 加载文件
		loadData();
	}
	
	private void initTable() {
		// 创建 JTable，直接重写 isCellEditable()，设为不可编辑
		table = new JTable(tableModel){
			@Override
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}			
		};
		JScrollPane scrollPane = new JScrollPane(table);
		root.add(scrollPane, BorderLayout.CENTER);
		
		// 添加到主界面		
		table.setFillsViewportHeight(true);		
		table.setRowSelectionAllowed(true); // 整行选择
		table.setRowHeight(30);	
		
		// 列设置：添加7列
		tableModel.addColumn ("ID");
		tableModel.addColumn ("学号");
		tableModel.addColumn ("姓名");
		tableModel.addColumn ("电话");
		tableModel.addColumn ("QQ");
		tableModel.addColumn ("报名时间");
		tableModel.addColumn ("第一志愿");
		tableModel.addColumn ("第二志愿");

		// 列设置：自定义绘制
		table.getColumnModel().getColumn(5).setCellRenderer(new DateColumnRenderer());
		table.getColumnModel().getColumn(0).setCellRenderer(new IdColumnRenderer());
		table.getColumnModel().getColumn(1).setCellRenderer(new MyColumnRenderer());
		table.getColumnModel().getColumn(2).setCellRenderer(new MyColumnRenderer());
		table.getColumnModel().getColumn(3).setCellRenderer(new MyColumnRenderer());
		table.getColumnModel().getColumn(4).setCellRenderer(new MyColumnRenderer());
		table.getColumnModel().getColumn(6).setCellRenderer(new MyColumnRenderer());
		table.getColumnModel().getColumn(7).setCellRenderer(new MyColumnRenderer());

		table.getColumnModel().getColumn(0).setPreferredWidth(26); // 该列的宽度
		table.getColumnModel().getColumn(3).setPreferredWidth(100); // 该列的宽度
		table.getColumnModel().getColumn(4).setPreferredWidth(100); // 该列的宽度
	}
	
	private void initToolBar() {
		JToolBar toolBar = new JToolBar();
		root.add(toolBar, BorderLayout.PAGE_START);
		toolBar.setFloatable(false);

		toolBar.addSeparator(new Dimension(1,10));
		
		// 按钮
		refreshButton = createToolButton("刷新", "ic_refresh.png" );
		toolBar.add(refreshButton);
		refreshButton.addActionListener( (e)->{	//这里利用工作线程处理更合理！！！
			refreshData();
		});

		// 查询
		toolBar.addSeparator(new Dimension(20,10));
		toolBar.add( new JLabel("查询: ") );
		toolBar.add( searchField );
		searchField.addFocusListener(new JTextFieldHintListener(searchField,"名字或学号，回车确定"));
		searchField.setMaximumSize(new Dimension(130,30));
		searchField.addActionListener( (e)->{
			// 按回车时触发事件
			onSearch();
		});

		//组查询
		toolBar.addSeparator(new Dimension(20,10));
		teamOneList = this.createToolJcb();
		toolBar.add(teamOneList);
		teamOneList.addActionListener(e->{
			onTeamSearch();
		});
		teamOneList.setMaximumSize(new Dimension(80,30));

		toolBar.addSeparator(new Dimension(10,10));
		teamTwoList = this.createToolJcb();
		teamTwoList.setMaximumSize(new Dimension(80,30));
		toolBar.add(teamTwoList);
		teamTwoList.addActionListener(e->{
			onTeamSearch();
		});

		toolBar.addSeparator(new Dimension(10,10));
		generateDialog.setMaximumSize(new Dimension(70, 30));
		toolBar.add(generateDialog);
		generateDialog.addActionListener(e -> {
			onGenerate();
		});

		helpButton = createToolButton("帮助", "ic_help.png");
		toolBar.addSeparator(new Dimension(10,10));
		helpButton.setMaximumSize(new Dimension(60, 30));
		toolBar.add(helpButton);
		helpButton.addActionListener(e -> {
			JOptionPane.showMessageDialog(this,"欢迎使用报名表，如果您喜欢，请在Github上给我点个心哦，谢谢！\n"
			+ "GitHubId : 'axinihao'\n"
			+ "如果网络卡顿会导致程序卡顿甚至未响应，请使用时保持网络畅通！\n"
			+ "如发现有Bug，请联系本人QQ：974212451");
		});
	}
	
	protected JButton createToolButton(String text, String icon) {
		// 图标
		String imagePath = "/icons/" + icon;
		URL imageURL = getClass().getResource(imagePath);

		// 创建按钮
		JButton button = new JButton(text);
		//button.setActionCommand(action);
		button.setToolTipText(text);
		button.setIcon(new ImageIcon(imageURL));
		button.setFocusPainted(false);
		return button;
	}

	protected JComboBox<Team> createToolJcb() {
		Vector<Team> vector = new Vector<>();
		Team team = new Team();
		team.setTeamId(0);
		team.setTeamName("all");
		vector.add(team);
		TeamDB.getInstance().getTeamList().forEach(e -> vector.add(e));
		return new JComboBox<>(vector);
	}

	// 设置 表格控件中的一条记录的值
	private void setTableRow(Applicant v, int row) {
		tableModel.setValueAt(v.getApplicantNum(), row, 0);
		tableModel.setValueAt(v.getApplicantName(), row, 1);
		tableModel.setValueAt(v.getPhone(), row, 2);
		tableModel.setValueAt(v.getQq(), row, 3);
		tableModel.setValueAt(v.getCreateTime(), row, 4);  //TODO
		tableModel.setValueAt(v.getTeamOne().getTeamName(), row, 5);
		tableModel.setValueAt(v.getTeamTwo().getTeamName(), row, 6);
	}

	private void addTableRow(Applicant item) {
		// java.util.Vector 是个范型 ，表示数组
		Vector<Object> rowData = new Vector<>();
		rowData.add(item.getApplicantId());
		rowData.add(item.getApplicantNum());
		rowData.add(item.getApplicantName());
		rowData.add(item.getPhone());
		rowData.add(item.getQq());
		rowData.add(item.getCreateTime()); //TODO
		rowData.add(item.getTeamOne().getTeamName());
		rowData.add(item.getTeamTwo().getTeamName());
		tableModel.addRow( rowData ); // 添加一行
	}

	private void onSearch() {
		// 获取用户输入的过滤条件
		String filter = searchField.getText().trim();
		this.teamOneList.setSelectedIndex(0);
		this.teamTwoList.setSelectedIndex(0);
		if(filter.length() == 0) {// 过滤条件为空
			tableModel.setRowCount(0);//清空
			this.dataList.forEach(this::addTableRow);
			return;
		}
		// 把符合条件的记录显示在表格里  // TODO
		String regExpName = "[\\u4e00-\\u9fa5]{1,5}";
		if (filter.matches(regExpName)) {
			tableModel.setRowCount(0);//清空
			for (Applicant applicant : dataList) {
				if (applicant.getApplicantName().indexOf(filter) >= 0) {
					addTableRow(applicant);
				}
			}
			return;
		}

		String regExpNum = "[0-9]{1,10}";
		if (filter.matches(regExpNum)) {
			tableModel.setRowCount(0);//清空
			for (Applicant applicant : dataList) {
				if (String.valueOf(applicant.getApplicantNum()).indexOf(filter) >= 0) {
					addTableRow(applicant);
				}
			}
			return;
		}

		JOptionPane.showMessageDialog(this, "未找到匹配的,请重新输入");
		this.searchField.setText("");
		// 把其他操作按钮禁用
	}

	private void onTeamSearch() {	//TODO
		Team teamOne = (Team) this.teamOneList.getSelectedItem();
		Team teamTwo = (Team) this.teamTwoList.getSelectedItem();
		this.searchField.setText("");
		tableModel.setRowCount(0);//清空
		if (teamOne.getTeamId() == 0 && teamTwo.getTeamId() == 0) {//都是所有大组则重新加载一次数据
			loadData();
			return;
		} else if (teamOne.getTeamId() != 0 && teamTwo.getTeamId() == 0) {
			for (Applicant applicant : this.dataList) {
				if (applicant.getTeamOne().getTeamId() == teamOne.getTeamId()) {
					this.addTableRow(applicant);
				}
			}
		} else if (teamOne.getTeamId() == 0 && teamTwo.getTeamId() != 0) {
			for (Applicant applicant : this.dataList) {
				if (applicant.getTeamTwo().getTeamId() == teamTwo.getTeamId()) {
					this.addTableRow(applicant);
				}
			}
		} else {
			for (Applicant applicant : this.dataList) {
				if (applicant.getTeamTwo().getTeamId() == teamTwo.getTeamId()
				&& applicant.getTeamOne().getTeamId() == teamOne.getTeamId()) {
					this.addTableRow(applicant);
				}
			}
		}
	}

	private void loadData() {
		this.tableModel.setRowCount(0);
		this.dataList.forEach(this::addTableRow);
	}

	private void refreshData() {
		convertHandler.init();
		this.dataList = ApplicantDB.getInstance().getApplicantList();
		loadData();
	}

	private void onGenerate() {
		GenerateExcelDialog dialog = new GenerateExcelDialog(this);
		Boolean retBool = dialog.exec();
		if (retBool == null) {
			return;
		} else if (retBool) {
			JOptionPane.showMessageDialog(this, "成功！");
		} else {
			JOptionPane.showMessageDialog(this, "失败！");
		}
	}




	class JTextFieldHintListener implements FocusListener {
		private String hintText;
		private JTextField textField;

		public JTextFieldHintListener(JTextField jTextField, String hintText) {
			this.textField = jTextField;
			this.hintText = hintText;
			jTextField.setText(hintText);  //默认直接显示
			jTextField.setForeground(Color.GRAY);
		}

		@Override
		public void focusGained(FocusEvent e) {
			//获取焦点时，清空提示内容
			String temp = textField.getText();
			if (temp.equals(hintText)) {
				textField.setText("");
				textField.setForeground(Color.BLACK);
			}

		}

		@Override
		public void focusLost(FocusEvent e) {
			//失去焦点时，没有输入内容，显示提示内容
			String temp = textField.getText();
			if (temp.equals("")) {
				textField.setForeground(Color.GRAY);
				textField.setText(hintText);
			}
		}
	}
}
