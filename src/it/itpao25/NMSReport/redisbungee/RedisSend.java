package it.itpao25.NMSReport.redisbungee;

import it.itpao25.NMSReport.Main;
import it.itpao25.NMSReport.config.ReporterGUIC;
import it.itpao25.NMSReport.util.Utili;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class RedisSend {
	public RedisSend(CommandSender sender, int id_report, String reported, String reason) throws IOException {
		if(!(sender instanceof Player)) {
			return;
		}
		Player p = (Player) sender;
		
		if(RedisBungee.isEnable()) {
			String listaServer = ReporterGUIC.getString("redisbungee.send-message-server");
			String[] listaServerArray = listaServer.split(",");
			
			// Data per il messaggio
			
			String nomeServer = Utili.getServerName();
			String playerreport = p.getName();
			String MessaggioFinale = null;
			
			if(reported != null) {
				MessaggioFinale = Utili.color(p, RedisBungee.getMessage()).replace("%id%", id_report + "").replace("%servername%", nomeServer).replace("%playertarget%", reported).replace("%motivation%", reason).replace("%fromplayer%", playerreport);
			} else {
				MessaggioFinale = Utili.color(p, RedisBungee.getMessageGeneral()).replace("%id%", id_report + "").replace("%servername%", nomeServer).replace("%motivation%", reason).replace("%fromplayer%", playerreport);
			}
			
			for(String nome : listaServerArray) {
			
				ByteArrayDataOutput out = ByteStreams.newDataOutput();
				
				out.writeUTF("Forward");
				out.writeUTF(nome);
				out.writeUTF("ReporterGUI");
			
				ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
				DataOutputStream msgout = new DataOutputStream(msgbytes);
					
				
				msgout.writeUTF(MessaggioFinale);
				msgout.writeShort(123);

				out.writeShort(msgbytes.toByteArray().length);
				out.write(msgbytes.toByteArray());
				
				if(Main.debug) {
					System.out.print( "[ ReporterGUI ] RedisBungee message sent: "+ MessaggioFinale+" to server "+ nome +" " );
				}
				
				p.sendPluginMessage(Main.getInstance(), "redisbungee:redisbungee", out.toByteArray());
				
			}
		}
	}
}
