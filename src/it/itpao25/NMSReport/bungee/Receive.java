package it.itpao25.NMSReport.bungee;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Iterator;

import it.itpao25.NMSReport.Main;
import it.itpao25.NMSReport.permission.PermissionUtil;
import it.itpao25.NMSReport.permission._Permission;
import it.itpao25.NMSReport.reflection.ReflectionUtil;
import it.itpao25.NMSReport.util.ConsoleMessage;
import it.itpao25.NMSReport.util.Utili;
import it.itpao25.NMSReport.util.VersionServer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

public class Receive implements PluginMessageListener {
	
	public Receive() {
		Main.getInstance().getServer().getMessenger().registerIncomingPluginChannel(Main.getInstance(), "BungeeCord", this);
		Bukkit.getConsoleSender().sendMessage(Utili.color("&b[&3ReporterGUI&b] &2Enabled listening to use bungeecord"));
	}
	
	@Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) 
	{
		if ( !channel.equals( "BungeeCord" ) ) {
			return;
		}
		
		ByteArrayDataInput in = ByteStreams.newDataInput( message );
		String subchannel = in.readUTF();
		
		if (subchannel.equals("ReporterGUI")) {
			
			short len = in.readShort();
			byte[] msgbytes = new byte[ len ];
			in.readFully( msgbytes );
			DataInputStream msgin = new DataInputStream( new ByteArrayInputStream( msgbytes ) );
			
			try {
				String somedata = msgin.readUTF();
				
				// Decodifico il permesso presente in _Permission
				JSONObject o = (JSONObject) JSONValue.parse(somedata);
				if(o == null) {
					throw new NullPointerException("Bungeecord: json can't be null (error parsing)");
				}
				Iterator<?> keys = o.keySet().iterator();
				String server = null; String messaggio = null;
				while(keys.hasNext()){
					messaggio = (String) keys.next();
					if(o.get(messaggio) != null && o.get(messaggio) != "") {
						server = (String) o.get(messaggio);
					}
				}
				
				for( Player online : VersionServer.getOnlinePlayers()) {
					if( PermissionUtil._has( online, _Permission.PERM_RECEIVE ) || online.isOp() ) {
						ReflectionUtil.sendBungeeMessageJSON(messaggio, online, server);
					}
				}
				
				new ConsoleMessage("&aBungeecord message received!");
				
			} catch ( IOException e ) {
				e.printStackTrace();
			}	
		} else if (subchannel.equals("GetServer")) {
			
			// Imposto il nome del server attuale utilizzando bungeecord
			Main.server_name_bungeecord = in.readUTF();
		}
    }
}
