package it.itpao25.NMSReport.bungee;

import it.itpao25.NMSReport.Main;
import it.itpao25.NMSReport.util.ConsoleMessage;
import it.itpao25.NMSReport.util.Utili;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class Broadcast {
	
	public Broadcast(CommandSender sender, final int id_report, final String reported, final String reason) throws IOException {
		
		if(!(sender instanceof Player)) {
			return;
		}
		
		final Player p = (Player) sender;
	
		if(Bungeecord.isEnableBungee()) {
			
			getCurrentServerName(p);
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
				@Override public void run() {
					String listaServer = Main.getInstance().getConfig().getString("bungeecord.send-message-server");
					String[] listaServerArray = listaServer.split(",");
					
					// Data per il messaggio
					
					String nomeServer = Utili.getServerName();
					String playerreport = p.getName();
					String MessaggioFinale = null;
					
					if(reported != null) {
						MessaggioFinale = Utili.color(p, Bungeecord.stringBroadcast()).replace("%id%", id_report+ "").replace("%servername%", nomeServer).replace("%playertarget%", reported).replace("%motivation%", reason).replace("%fromplayer%", playerreport);
					} else {
						MessaggioFinale = Utili.color(p, Bungeecord.stringBroadcastGenerale()).replace("%id%", id_report+ "").replace("%servername%", nomeServer).replace("%motivation%", reason).replace("%fromplayer%", playerreport);
					}
					
					MessaggioFinale = "{\""+ MessaggioFinale + "\" : \""+ Main.server_name_bungeecord +"\"}";
					
					for(String nome : listaServerArray) {
					
						ByteArrayDataOutput out = ByteStreams.newDataOutput();
						
						out.writeUTF("Forward"); // So BungeeCord knows to forward it
						out.writeUTF(nome);
						out.writeUTF("ReporterGUI");
						
						ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
						DataOutputStream msgout = new DataOutputStream(msgbytes);
						
						
						try {
							msgout.writeUTF(MessaggioFinale);
							msgout.writeShort(123);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
						out.writeShort(msgbytes.toByteArray().length);
						out.write(msgbytes.toByteArray());
						
						if(Main.debug) {
							new ConsoleMessage("&2Message sent: "+ MessaggioFinale+" to server "+ nome +" ");
						}

						p.sendPluginMessage(Main.getInstance(), "BungeeCord", out.toByteArray());
					}
				}
			}, 40L);
		}
	}
	
	/**
	 * Ritorno con
	 * @param player
	 */
	public void getCurrentServerName(final Player player){
		try {
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(b);
			out.writeUTF("GetServer");
			
			player.sendPluginMessage(Main.getInstance(), "BungeeCord", b.toByteArray());
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
}
