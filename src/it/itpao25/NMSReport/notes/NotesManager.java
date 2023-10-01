package it.itpao25.NMSReport.notes;

import it.itpao25.NMSReport.NoteObject;
import it.itpao25.NMSReport.command.ValueCustom;
import it.itpao25.NMSReport.config.ReporterGUIM;
import it.itpao25.NMSReport.event.NoteCreateEvent;
import it.itpao25.NMSReport.storage.MysqlReport;
import it.itpao25.NMSReport.util.TimeManger;
import it.itpao25.NMSReport.util.Utili;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class NotesManager {
	
	/**
	 * Aggiungo una nota nel database
	 * @param id_report
	 * @param text
	 * @param player
	 * @return
	 */
	public static HashMap<Integer, String> addNote(int id_report, String text, CommandSender player) {	
		String query = ("INSERT INTO `reporter_notes` (id, id_report, insert_by, date, text) VALUES (NULL, ?, ?, ?, ?)");
	
		try {
			if(MysqlReport.checkReportIsExist(id_report) == false) {
				player.sendMessage(Utili.colorWithPrefix(player, ReporterGUIM.getString("message.report-not-found")));
				return null;
			}
		} catch (SQLException e1) {
				// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			
			Connection c = MysqlReport.connessioneMysql();
			// Preparo la query al database
			PreparedStatement s = (PreparedStatement) c.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			s.setInt(1, id_report);
			s.setString(2, player.getName());
			s.setString(3, TimeManger.GetCurrentTimeStamp());
			s.setString(4, text);
			s.execute();
			
			ResultSet rs = s.getGeneratedKeys();
			if (rs.next()) {
				int generatedkey = rs.getInt(1);
				NoteCreateEvent event = new NoteCreateEvent(player, id_report, generatedkey, text);
				Bukkit.getServer().getPluginManager().callEvent(event);
				if (!event.isCancelled()) {
			    	// Prendo l'id della nota per il report
			    	// Esempio - Report #1 -> Nota #1
			    	//		     Report #1 -> Nota #2 ecc..
					query = ("SELECT * from `reporter_notes` WHERE `id_report`='"+ id_report + "' ORDER BY `reporter_notes`.`id` ASC");
					s = (PreparedStatement) c.prepareStatement(query);
		    		rs = s.executeQuery();
		    		int index = 1;
		    		
		    		HashMap<Integer, String> campi = new HashMap<Integer, String>();
			    	while (rs.next()) {
			    		campi.put(index, rs.getString("id")+ "_"+ generatedkey);
			    		index++;
			    	}
			    	s.close();
			    	rs.close();
			    	return campi;
				}
		    }
		    s.close();
		    c.close();
		    return null;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	    return null;
	}
	
	/**
	 * Elimino una nota
	 * @param id_notes
	 * @param i 
	 * @return
	 * @throws SQLException 
	 */
	public static boolean delNote(int id_report, int id_nota) throws SQLException {
		int request = getIDNote(id_report, id_nota);
		if( request == 0) {
			return false;
		}
		String query = ("DELETE FROM `reporter_notes` WHERE id=" + request);
		MysqlReport.MysqlReportRunQuery(query);
		return true;
	}
	
	/**
	 * Converto i due valori per l'ordinamento delle note
	 * @param id_report
	 * @param id_nota
	 * @return
	 * @throws SQLException
	 */
	public static int getIDNote(int id_report, int id_nota) throws SQLException {
		
		Connection c = MysqlReport.connessioneMysql();
		String query = ("SELECT * from `reporter_notes` WHERE `id_report`="+ id_report + " ORDER BY `reporter_notes`.`id` ASC");
		PreparedStatement s = (PreparedStatement) c.prepareStatement(query);
		ResultSet rs = s.executeQuery();
		int index = 1;
		
		HashMap<Integer, Integer> campi = new HashMap<Integer, Integer>();
    	while (rs.next()) {
    		campi.put(index, rs.getInt("id"));
    		index++;
    	}
    	
    	if(campi.containsKey(id_nota)) {
    		int return_var = campi.get(id_nota);
    		return return_var;
    	}
    	rs.close();
    	s.close();
    	c.close();
		return 0;
	}
	/**
	 * Controllo se la nota è presente nel database
	 * @param id_report
	 * @param id_notes
	 * @return
	 */
	public boolean isNoteExist(int id_report, int id_notes) {
//		String query = ("SELECT FROM `reporter_notes WHERE ");
		return false;
	}
	
	/**
	 * 
	 * @param p
	 * @param id_report
	 * @throws SQLException
	 */
	@SuppressWarnings("deprecation")
	public static void print_notes(CommandSender p, int id_report, int limit, boolean hasViewID) throws SQLException {
		if(MysqlReport.checkReportIsExist(id_report)) {
			Connection c = MysqlReport.connessioneMysql();
			String query;
			if(limit == -1) {
				query = ("SELECT * from `reporter_notes` WHERE `id_report`='"+ id_report + "' ORDER BY `reporter_notes`.`id` DESC");
			} else {
				query = ("SELECT * from `reporter_notes` WHERE `id_report`='"+ id_report + "' ORDER BY `reporter_notes`.`id` DESC LIMIT "+ limit);
			}
			
			PreparedStatement s = (PreparedStatement) c.prepareStatement(query);
			ResultSet rs = s.executeQuery();
			NoteObject obj = new NoteObject(id_report);
			int numero_index = obj.getNumeroNote();
			int index = 1;
			while(rs.next()) {
				if(index == 1) {
					if(hasViewID) {
						p.sendMessage(Utili.color( p, ReporterGUIM.getString("message.info-report-notes-title").replace("[id]", id_report + "")));
					} else {
						p.sendMessage(Utili.colorWithPrefix( p, "List of notes for report #"+ id_report));
					}
				}
				String nota_message = rs.getString("text") != null ? rs.getString("text") : "";
				String text = ReporterGUIM.getString("message.note-list-layout");
				text = text.replace("[id]", numero_index + "");
				text = text.replace("[text]", nota_message);
				
				if(rs.getString("insert_by") != null && Bukkit.getOfflinePlayer(rs.getString("insert_by")) != null) {
					OfflinePlayer oplayer = Bukkit.getOfflinePlayer(rs.getString("insert_by"));
					if(oplayer != null && oplayer.isOnline()) {
						text = text.replace("[nickname]", ChatColor.GREEN + rs.getString("insert_by") + ChatColor.RESET);
					} else {
						text = text.replace("[nickname]", ChatColor.GOLD + rs.getString("insert_by") + ChatColor.RESET);
					}
				} else {
					text = text.replace("[nickname]", ChatColor.GOLD + rs.getString("insert_by") + ChatColor.RESET);
				}
				p.sendMessage(Utili.color(p, text));
				index++;
				numero_index--;
			}
			// Conto le note
			if(hasViewID && obj.getNumeroNote() > limit) {
				p.sendMessage(Utili.color(ReporterGUIM.getString("message.info-report-note-nexts").replace("[command]", "&3/"+ ValueCustom.getCommandMain() +" note list "+ id_report + "&r")));
			}
			if(index == 1) {
				if(hasViewID == false) {
					p.sendMessage(Utili.colorWithPrefix( p, "&cNo notes were found for this report"));
				}
				return;
			}
			c.close();
		} else {
			if(hasViewID == false) {
				p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.report-not-found")));
			}
		}
	}
}
