package cn.sicnu.itelites;

import cn.sicnu.itelites.main.SwingRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ExcelApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(ExcelApplication.class).headless(false).run(args);
		SwingRunner.run();
	}

}
