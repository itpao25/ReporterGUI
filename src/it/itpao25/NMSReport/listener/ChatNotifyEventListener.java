package it.itpao25.NMSReport.listener;

import it.itpao25.NMSReport.config.ReporterGUIC;
import it.itpao25.NMSReport.config.ReporterGUIM;
import it.itpao25.NMSReport.event.ChangedStatusReport;
import it.itpao25.NMSReport.event.NoteCreateEvent;
import it.itpao25.NMSReport.notify.PlayerExitFromNotify;
import it.itpao25.NMSReport.permission.PermissionUtil;
import it.itpao25.NMSReport.permission._Permission;
import it.itpao25.NMSReport.reflection.ReflectionUtil;
import it.itpao25.NMSReport.storage.MysqlReport;
import it.itpao25.NMSReport.storage.MysqlStatus;
import it.itpao25.NMSReport.util.Utili;
import it.itpao25.NMSReport.util.VersionServer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatNotifyEventListener implements Listener {
	
	@EventHandler
	public void onChangeStatus(ChangedStatusReport event) {
		int id_report = event.getReportID();
		
		// Controllo se l'opzione è abilitata dal config
		if( ReporterGUIC.getString("status-update-messages.enable") != null) {
			if( ReporterGUIC.getBoolean("status-update-messages.enable") == false) {
				return;
			}
		}
		Connection c = MysqlReport.connessioneMysql();
		try {
			PreparedStatement s = (PreparedStatement) c.prepareStatement("SELECT * from reporter WHERE ID=(?)");
			s.setLong(1, id_report);
			ResultSet rs = s.executeQuery();

			String from = null;
			while (rs.next()) {
				from = rs.getString("PlayerFrom");
			}
			// Faccio il get del giocatore
			@SuppressWarnings("deprecation")
			OfflinePlayer from_instance = Bukkit.getOfflinePlayer(from);
			if( from_instance == null) {
				return;
			}
			// invio il messaggio al giocatore che ha inviato la segnalazione
			if( ReporterGUIC.getBoolean("status-update-messages.send-notify-reporter") == true) {
				if( !from_instance.hasPlayedBefore() ) {
					return;
				}
				// Se il giocatore che ha inviato il report è lo stesso che ha cambiato stato, prevengo il messaggio notifica
				if( from_instance.getName().toLowerCase().equals(event.getExcutor().getName().toLowerCase()) == false) {
					String message = ReporterGUIM.getString("message.status-update-messages.send-notify-reporter");
					message = message.replace("[id]", id_report + "");
					message = message.replace("[new_status]", MysqlStatus.translatecolor(event.getStatus().toString()));
					if( from_instance.isOnline()) {
						Player p = (Player) from_instance;
						ReflectionUtil.sendMessageJSON( id_report, message, p );
					} else {
						Utili.addNotifyOffline(from_instance.getName(), message);
					}
				}
			}
			// Invio il messaggio ai membri dello staff online
			if( ReporterGUIC.getBoolean("status-update-messages.send-notify-staff") == true) {
				for( Player online : VersionServer.getOnlinePlayers() ) {
					if( PermissionUtil._has( online, _Permission.PERM_RECEIVE ) || online.isOp() ) {
						if( online.getName().toLowerCase().equals(event.getExcutor().getName().toLowerCase())) {
							continue;
						}
						// Controllo se il giocatore ha disabilitato le notifiche
						if(PlayerExitFromNotify.hasPlayer(online.getUniqueId())) {
							continue;
						}
						String message = ReporterGUIM.getString("message.status-update-messages.send-notify-staff");
						message = message.replace("[id]", id_report + "");
						message = message.replace("[new_status]", MysqlStatus.translatecolor(event.getStatus().toString()));
						ReflectionUtil.sendMessageJSON( id_report, message, online );
					}
				}
			}
			return;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return;
	}
	
	@EventHandler
	public void onNoteCreate(NoteCreateEvent event) {
		// Controllo se l'opzione è abilitata dal config
		if( ReporterGUIC.getString("notes-update-messages.enable") != null) {
			if( ReporterGUIC.getBoolean("notes-update-messages.enable") == false) {
				return;
			}
		}
		Connection c = MysqlReport.connessioneMysql();
		try {
			PreparedStatement s = (PreparedStatement) c.prepareStatement("SELECT * from reporter WHERE ID=(?)");
			s.setLong(1, event.getIdReport());
			ResultSet rs = s.executeQuery();

			String from = null;
			while (rs.next()) {
				from = rs.getString("PlayerFrom");
			}
			@SuppressWarnings("deprecation")
			OfflinePlayer from_instance = Bukkit.getOfflinePlayer(from);
			if( from_instance == null ) {
				return;
			}
			if( ReporterGUIC.getBoolean("notes-update-messages.send-newnote-reporter") == true) {
				if( !from_instance.hasPlayedBefore() ) {
					return;
				}
				// Se il giocatore che ha inviato il report è lo stesso che ha cambiato stato, prevengo il messaggio notifica
				if( from_instance.getName().toLowerCase().equals(event.getSender().getName().toLowerCase()) == false) {
					String message = ReporterGUIM.getString("message.notes-update-messages.send-newnote-reporter");
					message = message.replace("[id]", event.getIdReport() + "");
					message = message.replace("[id_note]", event.getIdNota() + "");
					message = message.replace("[text_note]", event.getTextNote());
					if( from_instance.isOnline()) {
						Player p = (Player) from_instance;
						ReflectionUtil.sendMessageJSON( event.getIdReport(), message, p );
					} else {
						Utili.addNotifyOffline(from_instance.getName(), message);
					}
				}
			}
			if( ReporterGUIC.getBoolean("notes-update-messages.send-newnote-staff") == true) {
				for( Player online : VersionServer.getOnlinePlayers() ) {
					if( PermissionUtil._has( online, _Permission.PERM_RECEIVE ) || online.isOp() ) {
						if( online.getName().toLowerCase().equals(event.getSender().getName().toLowerCase())) {
							continue;
						}
						// Controllo se il giocatore ha disabilitato le notifiche
						if(PlayerExitFromNotify.hasPlayer(online.getUniqueId())) {
							continue;
						}
						String message = ReporterGUIM.getString("message.notes-update-messages.send-newnote-staff");
						message = message.replace("[id]", event.getIdReport() + "");
						message = message.replace("[id_note]", event.getIdNota() + "");
						message = message.replace("[text_note]", event.getTextNote());
						message = message.replace("[name]", event.getSender().getName());
						ReflectionUtil.sendMessageJSON( event.getIdReport(), message, online );
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
