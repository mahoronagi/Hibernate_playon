package tool;

import java.text.*;
import java.time.*;
import java.util.*;

public class FormatDate {

    private SimpleDateFormat inSDF = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private SimpleDateFormat outSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String formatDate(String inDate) {
        String outDate = "";
        //Format formatter = new SimpleDateFormat("hh:mm:ss");
        if (inDate != null) {
            try {
                Date date = inSDF.parse(inDate);
                outDate = outSDF.format(date);
            } catch (ParseException ex) {
                System.out.println("Unable to format date: " + inDate + ex.getMessage());
                ex.printStackTrace();
            }
        }
        //return outDate + " " + formatter.format(new Date());
        return outDate; 
    }
    
    public String DateO(String inDate){
        String outDate = "";
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        if (inDate != null) {
            try {
                Date date = outSDF.parse(inDate);
                outDate = formatter.format(date);
            } catch (ParseException ex) {
                System.out.println("Unable to format date: " + inDate + ex.getMessage());
                ex.printStackTrace();
            }
        }
        //return outDate + " " + formatter.format(new Date());
        return outDate; 
    }

    public String CurrentDate() {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date).toString();
    }
    
    public String SumDate(int day) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); // Now use today date.
        c.add(Calendar.DATE, day);
        return dateFormat.format(c.getTime());
    }
    
    public String SumDate(String date,int day) throws ParseException {
        Date a = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        a = dateFormat.parse(date);
        Calendar c = Calendar.getInstance();
        c.setTime(a); // Now use today date.
        c.add(Calendar.DATE, day);
        return dateFormat.format(c.getTime());
    }
    
    public String SumWeek(String date,int week) throws ParseException {
        Date a = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        a = dateFormat.parse(date);
        Calendar c = Calendar.getInstance();
        c.setTime(a); // Now use today date.
        c.add(Calendar.WEEK_OF_MONTH, week);
        return dateFormat.format(c.getTime());
    }
}
