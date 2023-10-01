package it.itpao25.NMSReport.util;

import java.util.HashMap;

import org.bukkit.Location;

public class WorldManager {
	
	/**
	 * Converto una variable Location in string
	 * @param loc
	 * @return
	 */
	public static String loc2str(Location loc){
		return loc.getWorld().getName()+" : "+loc.getBlockX()+"x : "+loc.getBlockY()+"y : "+loc.getBlockZ() +"z";
	}
	
	/**
	 * Converto una variable Location in string
	 * @param loc
	 * @return
	 */
	public static String loc2strTrim(Location loc) {
		return loc.getWorld().getName()+":"+loc.getBlockX()+":"+loc.getBlockY()+":"+loc.getBlockZ() +":" + loc.getYaw();
	}
	
	/**
	 * Converto la stringa in location
	 * @param loc
	 * @return
	 */
	public static HashMap<String, String> locfromstr(String loc){
		HashMap<String, String> result = new HashMap<>();
		
		String[] loca = loc.trim().split(":");
		result.put("world", loca[0]);
		result.put("x", loca[1]);
		result.put("y", loca[2]);
		result.put("z", loca[3]);
		result.put("yaw", loca[4]);
		
		return result;
	}
}
