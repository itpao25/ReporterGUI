package it.itpao25.NMSReport.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.command.CommandSender;

import it.itpao25.NMSReport.command.ValueCustom;
import it.itpao25.NMSReport.config.ReporterGUIM;
import it.itpao25.NMSReport.util.TimeManger;
import it.itpao25.NMSReport.util.Utili;

public class MysqlHistory {
	public static void getHistoryReport(CommandSender p, String time, int index, boolean hasCMDServer) {
		
		int perpagina = 10;
		int pagina = index == 0 || index < 0 ? 1 : index;
		int paginastart = perpagina * (pagina -1);
		
		String query = null;
		String title = null;
		boolean hasServer = false;
		
		if(Utili.hasServerName() && hasCMDServer == false) {
			hasServer = true;
		}
		
		if(time == null) {
			time = "";
		}
		switch(time) {
			
			// Oggi
			case "today":
			case "t":
				title = ReporterGUIM.getString("message.history-time.today");
				if(hasServer) {
					query = ("SELECT * from reporter WHERE `server`= (?) AND date_format(Time, '%Y-%m-%d') = date_format(now(), '%Y-%m-%d')");
				} else {
					query = ("SELECT * from reporter WHERE date_format(Time, '%Y-%m-%d') = date_format(now(), '%Y-%m-%d')");
				}
				break;
				
			// Ieri 
			case "yesterday":
			case "y":
				title = ReporterGUIM.getString("message.history-time.yesterday");
				if(hasServer) {
					query = ("SELECT * from reporter WHERE `server`= (?) AND date_format(Time, '%Y-%m-%d') = date_format(now() - INTERVAL 1 DAY, '%Y-%m-%d')");
				} else {
					query = ("SELECT * from reporter WHERE date_format(Time, '%Y-%m-%d') = date_format(now() - INTERVAL 1 DAY, '%Y-%m-%d')");
				}
				break;
			
			// Questa settimana
			case "week":
			case "w":
				title = ReporterGUIM.getString("message.history-time.week");
				if(hasServer) {
					query = ("SELECT * from reporter WHERE `server`= (?) AND YEARWEEK(Time)=YEARWEEK(NOW())");
				} else {
					query = ("SELECT * from reporter WHERE YEARWEEK(Time)=YEARWEEK(NOW())");
				}
				break;
			
			// Questo mese
			case "month":
			case "m":
				title = ReporterGUIM.getString("message.history-time.month");
				if(hasServer) {
					query = ("SELECT * from reporter WHERE `server`= (?) AND date_format(Time, '%Y-%m') = date_format(now(), '%Y-%m')");
				} else {
					query = ("SELECT * from reporter WHERE date_format(Time, '%Y-%m') = date_format(now(), '%Y-%m')");
				}
				break;
				
			// nessuna azione disponibile
			case "":
			default:
				p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.history-invalid-command").replace("[command]", "/"+ ValueCustom.getCommandMain() +" history [ (t)oday/ (y)esterday/ (w)eek/ (m)onth] [page]")));
				return;
		}
		
		Connection c = MysqlReport.connessioneMysql();
		
		try {
			// Conto di row disponbili 
			PreparedStatement int_number = (PreparedStatement) c.prepareStatement(query);
			if(hasServer) {
				int_number.setString(1, Utili.getServerName());
			}
			ResultSet rs_int = int_number.executeQuery();
			
			int tot = 0;
			while (rs_int.next()) {
				tot++;
			}
			
			// Numero delle pagine
			double totalpage = Math.ceil(tot / perpagina) + 1;
			query += " LIMIT "+ paginastart +", "+ perpagina;
			
			PreparedStatement search = (PreparedStatement) c.prepareStatement(query);
			if(hasServer) {
				search.setString(1, Utili.getServerName());
			}
			ResultSet rs = search.executeQuery();
			int num = 0;
			
			while (rs.next()) {
				if(num == 0) {
					p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.history-title").replace( "[pos]", title )));
				}
				
				String id = rs.getString("ID");
				String motivazione = rs.getString("Reason");
				String playerReport = rs.getString("PlayerReport");
				String time_r = rs.getString("Time");
				String status = rs.getString("status");
				String server = rs.getString("server");
				String timeago = TimeManger.getFrom(time_r);
				
				// Preparo la stringa
				String text = null;
				if(playerReport != null) {
					text = ReporterGUIM.getString("message.list-layout");
					text = text.replace("[id]", id);
					text = text.replace("[reported]", playerReport);
					text = text.replace("[reason]", motivazione);
					text = text.replace("[time]", timeago);
					text = text.replace("[status]", MysqlStatus.translatecolor(status));
				} else {
					text = ReporterGUIM.getString("message.list-layout-general");
					text = text.replace("[id]", id);
					text = text.replace("[reason]", motivazione);
					text = text.replace("[time]", timeago);
					text = text.replace("[status]", MysqlStatus.translatecolor(status));
				}
				
				text = text.replace("[server]", server);
				
				p.sendMessage(Utili.color(p,text));
				num++;
			}
			
			String footer = ReporterGUIM.getString("message.list-footer");
			footer = footer.replace("[current]", pagina+ "");
			footer = footer.replace("[totpage]", (int) totalpage + "");
			footer = footer.replace("[totalreport]", tot + "");

			p.sendMessage(Utili.color(p, footer));
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
