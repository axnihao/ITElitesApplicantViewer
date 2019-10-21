package cn.sicnu.itelites.main;

import cn.sicnu.itelites.main.my.MyFrame;
import com.alee.laf.WebLookAndFeel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

@Component
public class SwingRunner {

	private static MyFrame frame;

	@Autowired
	public void setFrame(MyFrame frame) {
		SwingRunner.frame = frame;
	}

	private static void createGUI()
	{
		// JFrame指一个窗口，构造方法的参数为窗口标题
		// 语法：因为MyFrame是JFrame的子类，所以可以这么写
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setTitle("ITElites报名表");
		// 设置窗口的其他参数，如窗口大小
		frame.setSize(620, 400);
		frame.setLocation(200,300);
		// 显示窗口
		frame.setVisible(true);
		
		
	}

	public static void run()
	{
		// 此段代码间接地调用了 createGUI()，具体原理在 Swing高级篇 里讲解
		// 初学者先照抄此代码框架即可
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel ( NimbusLookAndFeel.class.getCanonicalName () );
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (UnsupportedLookAndFeelException e) {
					e.printStackTrace();
				}
				WebLookAndFeel.initializeManagers ();
				createGUI();
			}
		});

	}
}
