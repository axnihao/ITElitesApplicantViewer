package cn.sicnu.itelites.util.bean;


import cn.sicnu.itelites.util.ConvertUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.write.metadata.WriteSheet;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ExcelGenerator {

    private String filePath;
    private ExcelWriter writer;
    private static int sheetCount = 0;
    public ExcelGenerator() {
        this.filePath = System.getProperty("user.dir") + File.separator + "temp" + File.separator + "default.xlsx";
        this.writer = EasyExcel.write(this.filePath).build();
    }

    public ExcelGenerator(String filePath) {
        this.filePath = filePath;
        if (this.filePath == null || this.filePath.length() == 0 || "".equals(this.filePath))
            this.filePath = System.getProperty("user.dir") + File.separator + "temp" + File.separator + "default.xlsx";
        if (!this.filePath.endsWith(".xlsx")) {
            this.filePath += ".xlsx";
        }
        this.writer = EasyExcel.write(this.filePath).build();
    }

    public ExcelGenerator addSheet(List<?> data, String sheetName, Field[] fields) {
        if (data == null || data.size() == 0) return this;
        WriteSheet sheet = null;
        if (fields == null || fields.length == 0) {
            sheet = EasyExcel.writerSheet(sheetCount,sheetName).head(data.get(0).getClass()).build();
            this.writer.write(data, sheet);
        } else {
            List<List<String>> heads = new ArrayList<>(fields.length);
            for (Field field : fields) {
                if (field == null) continue;
                List<String> head = new ArrayList<>(1);
                head.add(field.getAnnotation(ExcelProperty.class).value()[0]);
                heads.add(head);
            }
            sheet = EasyExcel.writerSheet(sheetCount,sheetName).head(heads).build();
            List<List<String>> rows = ConvertUtil.getApplicantListByFields(data,fields);
            this.writer.write(rows, sheet);
        }
        sheetCount++;
        return this;
    }

    public void generateExcel() {
        this.writer.finish();
    }

    public String getFilePath() {
        return filePath;
    }
}
