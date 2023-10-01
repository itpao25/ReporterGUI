package it.itpao25.NMSReport.util;

import it.itpao25.NMSReport.Main;
import it.itpao25.NMSReport.config.ReporterGUIC;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

public class GestiscoSimboli 
{
	/**
	 * Converto eventuali simboli ascii
	 * @param str
	 * @return
	 */
	public String GestiscoSimboliS(String str) {
		if(str != null) {
			List<String> listaSimboli = ReporterGUIC.get().getStringList("symbol");
			StringBuilder sb = new StringBuilder(str);
			for (String keySimbolo : listaSimboli) {
				String[] arraySimboloStringa = keySimbolo.split(",");
				if(str.contains(arraySimboloStringa[0])) {
					// Debug message
					if(Main.debug) {
						Main.getInstance().getLogger().info("Replace" + keySimbolo + " with " + arraySimboloStringa[1]+ " in string" + str);	
					}
					SimboliReplace(sb, arraySimboloStringa[0], StringEscapeUtils.unescapeJava(arraySimboloStringa[1].trim()));
				}
			}
			return sb.toString();
		}
		return str;
	}
	
	public static void SimboliReplace(StringBuilder sb, String daReplace, String Replace) {
		int c = -1;
		while ((c = sb.lastIndexOf(daReplace)) != -1) {
			sb.replace(c, c + daReplace.length(), Replace);
		}
	}
}
