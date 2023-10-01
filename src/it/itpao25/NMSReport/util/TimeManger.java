package it.itpao25.NMSReport.util;

import it.itpao25.NMSReport.config.ReporterGUIC;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class TimeManger {

	/**
	 * Timestamp impostato dal config
	 * @return
	 */
    public static String GetCurrentTimeStamp(){
    	try {					
    		String getDataConf = ReporterGUIC.getString("timestamp");
    		DateFormat dateFormat = new SimpleDateFormat(getDataConf);
        	Date date = new Date();
        	String data = dateFormat.format(date);
        	return data;
        	
    	} catch (NullPointerException e) {
    		
    		e.printStackTrace();
    	}
		return null;
    }
    
    /**
     * Converto il tempo in "time ago"
     * Esempio "10d" come "10 giorni fa"
     * @param data
     * @return
     */
	public static String getFrom(String data) {
		try {
			String getDataConf = ReporterGUIC.getString("timestamp");
	        SimpleDateFormat format = new SimpleDateFormat(getDataConf);
	        Date past = format.parse(data);
	        Date now = new Date();
	        if(TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) >= 1) {
	        	return TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) + "d";
	        }
	        if(TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) >= 1) {
	        	return TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime())+ "h";
	        }
	        if(TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) >= 1) {
	        	return TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) + "m";
	        }
	        return "A moment ago";
	    }
	    catch (Exception j){
	        j.printStackTrace();
	    }
		return null;
	}
	
	/**
     * Converto il tempo in "tempo mancante"
     * Esempio "10d" come "fra 10 giorni"
     * @param data
     * @return
     */
	public static String getTo(String data) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        Date past = sdf.parse(data);
	        Date now = new Date();
	        
        	if(sdf.parse(data).before(new Date())) {
        		return "Passed";
        	}
	        if(TimeUnit.MILLISECONDS.toDays(past.getTime() - now.getTime()) >= 1) {
	        	return TimeUnit.MILLISECONDS.toDays(past.getTime() - now.getTime()) + "d";
	        }
	        if(TimeUnit.MILLISECONDS.toHours(past.getTime() - now.getTime()) >= 1) {
	        	return TimeUnit.MILLISECONDS.toHours(past.getTime() - now.getTime())+ "h";
	        }
	        if(TimeUnit.MILLISECONDS.toMinutes(past.getTime() - now.getTime()) >= 1) {
	        	return TimeUnit.MILLISECONDS.toMinutes(past.getTime() - now.getTime()) + "m";
	        }
	        return "Now";
	    }
	    catch (Exception j){
	        j.printStackTrace();
	    }
		return null;
	}
	
	/**
	 * Ritorno con la data attuale più X minuti
	 * @return
	 */
	public static Date addMinutesToCurrent(int minutes) {
		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.MINUTE, minutes);
		return cal.getTime();
	}
}
