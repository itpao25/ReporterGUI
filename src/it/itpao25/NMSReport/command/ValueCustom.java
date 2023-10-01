package it.itpao25.NMSReport.command;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;

import it.itpao25.NMSReport.config.ReporterGUIC;
import it.itpao25.NMSReport.util.ConsoleMessage;
import it.itpao25.NMSReport.util.Utili;

public class ValueCustom {
	
	/**
	 * Registra i comandi
	 * @return
	 */
	public static boolean register() {
		
		// Comando report
		if(hasListReport()) {
			StringBuilder string = new StringBuilder();			
			for ( String value : listCommandReport() ) {
				AbstractCommand reportcommand = new CommandReport(value);
				reportcommand.register();
				string.append(value + ", ");
			}
			new ConsoleMessage("&2commands multiple registred in report: "+ string.toString());	
		} else {
			AbstractCommand reportcommand = new CommandReport(getCommandReport());
			reportcommand.register();
		}
		
		// Comando reportercli
		if( hasListReportcli()) {
			StringBuilder string = new StringBuilder();		
			for ( String value : listCommandReportcli() ) {
				AbstractCommand reportclicommand = new CommandReportCLI(value);
				reportclicommand.register();
				string.append(value + ", ");
			}
			new ConsoleMessage("&2commands multiple registred in reportcli: "+ string.toString());
		} else {
			AbstractCommand reportclicommand = new CommandReportCLI(getCommandReportCli());
			reportclicommand.register();
		}
		
		return true;
	}
	
	// Comandi predifiniti per il funzionamento del plugin
	// Questi valori vengono impostati in base al config
	// E sono per l'utente configurabili al 100%
	public static String getCommandReport() {
		if (  ReporterGUIC.getString("command-custom.report") != null ) {
			String value = ReporterGUIC.getString("command-custom.report");
			if(isCommandValid(value, "report" )) {
				return value;
			}
			return "report";
		}
		return "report";
	}
	
	public static String getCommandMain() {
		if ( ReporterGUIC.getString("command-custom.reportergui") != null ) {
			String value = ReporterGUIC.getString("command-custom.reportergui");
			if(isCommandValid(value, "main" )) {
				return value;
			}
			return "reportergui";
		}
		return "reportergui";
	}
	
	public static String getCommandReportCli() {
		if ( ReporterGUIC.getString("command-custom.reportcli") != null ) {
			String value =  ReporterGUIC.getString("command-custom.reportcli");
			if(isCommandValid(value, "reportcli" )) {
				return value;
			}
			return "reportcli";
		}
		return "reportcli";
	}
	
	private static boolean hasListReport() {
		if ( ReporterGUIC.getString("command-custom.report") != null) {
			return ReporterGUIC.get().isList("command-custom.report");
		}
		return false;
	}
	private static boolean hasListReportcli() {
		if ( ReporterGUIC.getString("command-custom.reportcli") != null) {
			return ReporterGUIC.get().isList("command-custom.reportcli");
		}
		return false;
	}
	
	private static List<String> listCommandReport() {
		List<String> list = ReporterGUIC.get().getStringList("command-custom.report");
		return list;
	}
	
	private static List<String> listCommandReportcli() {
		List<String> list = ReporterGUIC.get().getStringList("command-custom.reportcli");
		return list;
	}
	
	/**
	 * Controllo se il comando custom è valido usando regeix 
	 * Dove controllo se la stringa è vuota ("") o con caratteri speciali non ammessi
	 * @param string
	 * @return
	 */
	private static boolean isCommandValid(String string, String pos) {
		if(pos.equals("report") && hasListReport()) {
			return true;
		}
		if(pos.equals("reportcli") && hasListReportcli()) {
			return true;
		}
		if (string == null || string.trim().isEmpty()) {
			Bukkit.getConsoleSender().sendMessage("");
			Bukkit.getConsoleSender().sendMessage(Utili.color("&b[&3ReporterGUI&b] &4Incorrect format of string (command custom)"));
			Bukkit.getConsoleSender().sendMessage("");
     		return false;
     	}
		Pattern p = Pattern.compile("[^A-Za-z0-9]");
		Matcher m = p.matcher(string);
		boolean b = m.find();
		if (b == true) {
			Bukkit.getConsoleSender().sendMessage("");
			Bukkit.getConsoleSender().sendMessage(Utili.color("&b[&3ReporterGUI&b] &4Incorrect format of string (command custom, contains invaild character)"));
			Bukkit.getConsoleSender().sendMessage("");
			return false;
		}
		return true;
	}
}
