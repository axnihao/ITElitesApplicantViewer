package cn.sicnu.itelites.util;

import cn.sicnu.itelites.vo.Applicant;
import cn.sicnu.itelites.vo.Team;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class ConvertUtil {

    public static List<Applicant> getTeamOne(List<Applicant> list) {
        return list;
    }

    public static List<Applicant> getTeamTwo(List<Applicant> list) {
        List<Applicant> result = new ArrayList<>(200);
        list.stream().forEach(e -> {    //怀疑这里采用parallelStream()会有线程安全的风险
            if (!(e.getTeamOne().getTeamId() == e.getTeamTwo().getTeamId()))
                result.add(e);
        });
        return result;
    }

    public static List<Applicant> getApplicantListByTeams(List<Applicant> list, Team team) {
        return list.parallelStream().filter(e -> (e.getTeamOne().getTeamId() == team.getTeamId()) || (e.getTeamTwo().getTeamId() == team.getTeamId())).collect(Collectors.toList());
    }

    public static List<Applicant> getApplicantListByTeamOne(List<Applicant> list, Team team) {
        return list.parallelStream().filter(e -> e.getTeamOne().getTeamId() == team.getTeamId()).collect(Collectors.toList());
    }

    public static List<Applicant> getApplicantListByTeamTwo(List<Applicant> list, Team team) {
        return list.parallelStream().filter(e -> e.getTeamTwo().getTeamId() == team.getTeamId()).collect(Collectors.toList());
    }

    public static List<Applicant> getApplicantListByTeamOneAndTeamTwo(List<Applicant> list, Team teamOne, Team teamTwo) {
        return list.parallelStream().filter(e -> e.getTeamOne().getTeamId() == teamOne.getTeamId() && e.getTeamTwo().getTeamId() == teamTwo.getTeamId()).collect(Collectors.toList());
    }

    public static <T> List<List<String>> getApplicantListByFields(List<T> list, Field[] fields) {
        List<List<String>> result = new ArrayList<>();
        Method[] methods = list.get(0).getClass().getMethods();
        Map<String, Method> methodMap = new HashMap<>(methods.length, 1);
        for (Method method : methods) { //提前进行一次循环转换为键值对Map，避免下面的双重循环进行查找，空间换时间
            methodMap.put(method.getName(), method);
        }
        //这里为保存Field与Method对应的关系，采用Map键值对的方式进行储存，但未了保证Map装入的顺序性，应该采用LinkedHashMap！
        Map<Field, Method> fmMap = new LinkedHashMap<>(fields.length,1);
        for (int i = 0; i < fields.length; i++) {
            if (fields[i] == null) continue;
            String methodName = "get" + StringUtil.initCap(fields[i].getName());
            if (methodMap.containsKey(methodName)) {
                fmMap.put(fields[i], methodMap.get(methodName));
            }
        }
        list.forEach(e -> {
            List<String> row = new ArrayList<>();
            for (Map.Entry<Field, Method> entry : fmMap.entrySet()) {
                String strValue = "";
                try {
                    Object obj = entry.getValue().invoke(e);
                    strValue = value2String(obj, entry.getKey());
                } catch (Exception ex) { ex.printStackTrace(); }
                row.add(strValue);
            }
            result.add(row);
        });
        return result;
    }

    private static String value2String(Object object, Field field) throws Exception {
        if (object instanceof String)
            return (String) object;
        Converter converter = field.getAnnotation(ExcelProperty.class).converter().getConstructor().newInstance();
        String strValue = converter.convertToExcelData(object, null, null).getStringValue();
        return strValue;
    }
}
