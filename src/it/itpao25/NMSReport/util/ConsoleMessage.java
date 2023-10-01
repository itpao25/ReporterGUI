package it.itpao25.NMSReport.util;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class ConsoleMessage {
	public ConsoleMessage() {
		
	}
	public ConsoleMessage(String string) {
		Bukkit.getConsoleSender().sendMessage(Utili.color("&b[&3ReporterGUI&b] "+ string));
	}

	public ConsoleMessage(String string, boolean b) {
		if(b == false) {
			Bukkit.getConsoleSender().sendMessage("[ReporterGUI] "+ string);
		}
	}
	public static String ConsoleMessage_return(CommandSender p, String string) {
		return Utili.color(p, "&b[&3ReporterGUI&b] "+ string);
	}
}
