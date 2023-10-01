package it.itpao25.NMSReport.redisbungee;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import it.itpao25.NMSReport.Main;
import it.itpao25.NMSReport.permission.PermissionUtil;
import it.itpao25.NMSReport.permission._Permission;
import it.itpao25.NMSReport.util.Utili;
import it.itpao25.NMSReport.util.VersionServer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

public class RedisReceive implements PluginMessageListener {
	public RedisReceive() {
		Main.getInstance().getServer().getMessenger().registerIncomingPluginChannel(Main.getInstance(), "BungeeCord", this);
		Bukkit.getConsoleSender().sendMessage(Utili.color("&b[&3ReporterGUI&b] &2Enabled listening to use bungeecord"));
	}
	@Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) 
	{
		if ( !channel.equals( "RedisBungee" ) ) {
			return;
		}
		
		ByteArrayDataInput in = ByteStreams.newDataInput( message );
		String subchannel = in.readUTF();
		
		if ( subchannel.equals("ReporterGUI") ) {
			short len = in.readShort();
			byte[] msgbytes = new byte[ len ];
			in.readFully( msgbytes );
			DataInputStream msgin = new DataInputStream( new ByteArrayInputStream( msgbytes ) );
			try 
			{	
				String somedata = msgin.readUTF();
				for( Player online : VersionServer.getOnlinePlayers()) {
					if( PermissionUtil._has( online, _Permission.PERM_RECEIVE ) || online.isOp() ) {
						online.sendMessage( somedata );
					}
				}
				System.out.print( "[ ReporterGUI ] Message received" );

			} catch ( IOException e ) {
				e.printStackTrace();
			}	
		}
	}
}
