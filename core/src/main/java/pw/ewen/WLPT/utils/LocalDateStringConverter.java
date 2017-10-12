package pw.ewen.WLPT.utils;

import java.time.DateTimeException;
import java.time.LocalDate;

/**
 * Created by wenliang on 17-10-9.
 * LocalDate String 转换
 */
public class LocalDateStringConverter {
    public static LocalDate toLocalDate(String localDate) throws DateTimeException {
        LocalDate dDate = null;

        if(localDate.isEmpty()) {
            throw new DateTimeException("字符串转换为DateTime类型出错，字符串格式错误");
        } else {
            String[] dateStrings = localDate.split("/");
            if(dateStrings.length != 3) {
                throw new DateTimeException("字符串转换为DateTime类型出错，字符串格式错误");
            } else {
                try{
                    int year = Integer.valueOf(dateStrings[0]);
                    int month = Integer.valueOf(dateStrings[1]);
                    int day = Integer.valueOf(dateStrings[2]);

                    dDate = LocalDate.of(year, month, day);

                } catch(Exception e){
                    throw new DateTimeException("字符串转换为DateTime类型出错，字符串格式错误");
                }
            }
        }
        return dDate;
    }
}
