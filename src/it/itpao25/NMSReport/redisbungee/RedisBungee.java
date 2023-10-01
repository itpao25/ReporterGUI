package it.itpao25.NMSReport.redisbungee;

import it.itpao25.NMSReport.config.ReporterGUIC;

public class RedisBungee {
	/**
	 * Controllo se RediBungee è abilitato
	 * {@link https://minecraft.minimum.io/wiki/RedisBungee/API}
	 * @return
	 */
	public static boolean isEnable() 
	{
		Boolean var = ReporterGUIC.getBoolean("redisbungee.enable");
		if(var) {
			return true;
		}
		return false;
	}
	
	/**
	 * Nome del player dove è connesso
	 * @param p
	 */
	 public static String getMessage() 
	 {
		 String stringa = ReporterGUIC.getString("redisbungee.message");
		 return stringa;   	
	 }
	 
	 /**
	 * Nome del player dove è connesso
	 * @param p
	 */
	 public static String getMessageGeneral() 
	 {
		 String stringa = ReporterGUIC.getString("redisbungee.message-general");
		 return stringa;   	
	 }
	
}
