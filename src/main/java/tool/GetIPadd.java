package tool;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import tool.*;
 

public class GetIPadd {
    
    public static void main(String[] args) throws ParseException {
 
        InetAddress ip;
        String hostname;
        //InetAddress client_ip;
        try {
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostName();
            InetAddress client_ip = InetAddress.getLoopbackAddress();
            System.out.println("Your current IP address : " + ip);
            System.out.println("Your current Hostname : " + hostname);
            System.out.println("Your current Client_ip : " + client_ip);
 
        } catch (UnknownHostException e) {
 
            e.printStackTrace();
        }
        
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date).toString());
        
        FormatDate days = new FormatDate();
        System.out.println(days.SumDate(30));
        
        
        String line = "1,21,33";
        for (String s : line.split(",")) {
            char[] chars = s.toCharArray();
            int sum = 0;
            for (int i = 0; i < chars.length; i++) {
                sum += (chars[chars.length - i - 1] - '0') * Math.pow(10, i);
            }
            //System.out.println(sum);
        }
        
        NumberFormat format = NumberFormat.getInstance(Locale.US);    
        Number number = format.parse("10,000"); 

        // Now you can get number values from the object (like int, long, double)
        //System.out.println(number.intValue()); 
        
        String a = "";
        ArrayList ch = new ArrayList();
        ch.add(1);
        ch.add(1);
        ch.add(3);
        ch.add(4);
        ch.add(2);
        ch.add(1);
        ch.add(9);
        ch.add(1);
        Collections.sort(ch);
        for(int i = 0; i < ch.size() ; i++){
            if(i==0) a += ch.get(i);
            if(i>0 && !ch.get(i).equals(ch.get(i-1)) ) a+= "," + ch.get(i); 
        }
        System.out.println("String = " + a);
        
        
        FormatDate testday = new FormatDate();
        System.out.println(testday.formatDate("13/06/2017 00:00:00"));
        
        System.out.println(testday.SumDate(-4));
        System.out.println(testday.DateO(testday.SumDate(4)));
        
        ArrayList week = new ArrayList();
        String start = "2016-03-1";
        String end = "2016-03-8";
        String t = "";
        for (int i = 0; i <= 7; i++) {
            System.out.println(testday.SumDate(start, i));
            t = testday.SumDate(start, i);
            week.add(t);
            System.out.println(t);
        }
        System.out.println(week);
        
    }
}
