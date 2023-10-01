package it.itpao25.NMSReport.stats;

import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.entity.Player;

import it.itpao25.NMSReport.config.ReporterGUIM;
import it.itpao25.NMSReport.notify.PlayerExitFromNotify;
import it.itpao25.NMSReport.permission.PermissionUtil;
import it.itpao25.NMSReport.permission._Permission;
import it.itpao25.NMSReport.reflection.ReflectionUtil;
import it.itpao25.NMSReport.storage.MysqlReport;
import it.itpao25.NMSReport.util.Utili;
import it.itpao25.NMSReport.util.VersionServer;

public class StatsJoin {
	
	public StatsJoin(Player p) {
		
		int number = 0;
		try {
			number = MysqlReport.getTotalReportOwnOpen(p);
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
		
		// Messaggi allo staff
		if (ReporterGUIM.getBoolean("message.message-on-join.to-staff.enable")) {
			String message = ReporterGUIM.getString("message.message-on-join.to-staff.message");
			message = message.replace("[num]", number + "");
			message = message.replace("[player]", p.getName());
			
			// Invio il messaggio allo staff con le informazioni del giocatore
			// appena entrato nel server
			for( Player online : VersionServer.getOnlinePlayers()) {
				if( PermissionUtil._has( online, _Permission.PERM_VIEW_JOIN_OTHER ) || online.isOp() ) {
					// Controllo se il giocatore ha disabilitato le notifiche
					if(PlayerExitFromNotify.hasPlayer(online.getUniqueId())) {
						continue;
					}
					
					
					ReflectionUtil.sendMessageJSON_join( online, message, p );
				}
			}
		}
		if (ReporterGUIM.getBoolean("message.message-on-join.to-player.enable")) {
			// Statistiche per il giocatore
			// Invio il messaggio con le statistiche dei propri reports
			if( PermissionUtil._has( p, _Permission.PERM_VIEW_JOIN_OWN ) || p.isOp() ) {
				if( PermissionUtil._has( p, _Permission.PERM_VIEW_JOIN_OTHER )) {
					return;
				}
				// Controllo se il giocatore ha disabilitato le notifiche
				if(PlayerExitFromNotify.hasPlayer(p.getUniqueId())) {
					return;
				}
				// Notifiche che non ha ricevuto dato che era offline
				if(Utili.getNotifyOffline(p.getName()) != null) {
					ArrayList<String> notify = Utili.getNotifyOffline(p.getName());
					for( String messaggio : notify ) {
						p.sendMessage( Utili.colorWithPrefix(p, messaggio) );
					}
					Utili.removeNotify(p.getName());
				}
				String message_toplayer = ReporterGUIM.getString("message.message-on-join.to-player.message");
				message_toplayer = message_toplayer.replace("[num]", number +"");
				p.sendMessage( Utili.colorWithPrefix(p, message_toplayer) );
			}
		}
	}
}
