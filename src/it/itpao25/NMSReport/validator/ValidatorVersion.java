package it.itpao25.NMSReport.validator;

import it.itpao25.NMSReport.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


public class ValidatorVersion {
	
	private static String uri = "http://api.itpao25.ovh/validator.php";
	// %%__USER__%%
	private static String uid = "%%__USER__%%";
	public static String VALID = "00x11121_";
	public static String DISABLED = "00x11120_";
	public static String WAIT = "00x11122_";
	
	public static String ver() {
		return "1.8.5";
	}
	
	private static String uid() {
		return uid;
	}
	public static String check() {
		try {
			// Apro il link
			String url = uri + "?act=ver&pl=ReporterGUI&ver="+ ver() + "&usr="+ uid();
			URL url_s = new URL(url);
			URLConnection conn = url_s.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while( ( line = br.readLine() ) != null )  {
				return line;
			}
			br.close();
			return "";
		} catch (IOException e) {
			Main.getInstance().getLogger().info("Problem to check updates:" + e.getMessage());
		}
		
		return null;
	}
}
