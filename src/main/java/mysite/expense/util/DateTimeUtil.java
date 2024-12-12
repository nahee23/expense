package mysite.expense.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    //자바 날짜 date 를 문자열 포맷으로 변환하는 스태틱 메서드
    public static String convertDateString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    //문자열 날자 => sql Date 날짜
    public static Date convertStringToDate(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date utilDate = sdf.parse(dateString);
        return new Date(utilDate.getTime());
    }

    //이번달 첫 날짜
    public static String getCurrentMonthStartDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.now();
        return today.withDayOfMonth(1).format(formatter);
    }

    //오늘 날짜 문자열 "2024-12-11"
    public static String getCurrentDate(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.now();
        return today.format(formatter);
    }
}
