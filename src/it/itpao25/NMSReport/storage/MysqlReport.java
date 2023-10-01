package it.itpao25.NMSReport.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import it.itpao25.NMSReport.Main;
import it.itpao25.NMSReport.command.ValueCustom;
import it.itpao25.NMSReport.config.ReporterGUIC;
import it.itpao25.NMSReport.config.ReporterGUIM;
import it.itpao25.NMSReport.notes.NoteMysql;
import it.itpao25.NMSReport.notes.NotesManager;
import it.itpao25.NMSReport.permission.PermissionUtil;
import it.itpao25.NMSReport.permission._Permission;
import it.itpao25.NMSReport.reflection.ReflectionUtil;
import it.itpao25.NMSReport.util.ConsoleMessage;
import it.itpao25.NMSReport.util.TimeManger;
import it.itpao25.NMSReport.util.Utili;

public class MysqlReport 
{
	
	/**  
	 *  Classe principale riguardante al database
	 *  Tramite il mysql si potrà usare comandi per vedere i report stats, solo con il file
	 *  Non sarà possibile
	 */
	
	/**
	 * Controllo se il database è abilitato, in caso faccio la connessione
	 * @throws SQLException 
	 */
	public MysqlReport() throws SQLException {
		// Se è abilitato il funzionamento del mysql allora attivo la connessione
		if(isMysqlEnable()) {
			Connection c = MysqlReport.connessioneMysql();
			Bukkit.getConsoleSender().sendMessage(Utili.color("&b[&3ReporterGUI&b] &2Database connection successful"));
    		creoDefaultTabelle(c);
    		c.close();
    		
    		// Controllo se la tabella report è corretta @since 1.6.8
			@SuppressWarnings("unused")
			MysqlCheckTables MysqlCheckTables = new MysqlCheckTables();
			
			connessioneMysql();
		}
	}
	
	/**
	 * Controllo se le funzioni del mysql sono abilitate 
	 */
	public static boolean isMysqlEnable()
	{	
		// Tento di aprire la connessione al database
		Connection c = MysqlReport.connessioneMysql();
		if(c == null) {
			return false;
		}
		try {
			c.close();
		} catch (SQLException e) {}
		
		if(ReporterGUIC.getBoolean("mysql.enable")) {
			return true;
		}
		return false;
	}
	
	/**
	 *  Effettuo la connessione al mysql 
	 *  */
	public static Connection connessioneMysql() {
		
		String username = ReporterGUIC.getString("mysql.username"); 
		String password = ReporterGUIC.getString("mysql.password");
		int ip = ReporterGUIC.getInt("mysql.port"); 
		String host = ReporterGUIC.getString("mysql.hostname"); 
		String name = ReporterGUIC.getString("mysql.namedatabase"); 
		
		String uri = "jdbc:mysql://"+ host +":"+ ip +"/"+ name +"";
		
		java.util.Properties options = new java.util.Properties();
        options.put("user", username);
        options.put("password", password);
        options.put("autoReconnect", "true");
        options.put("maxReconnects", "5");
        
		try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection con = null;
		try {
			con = DriverManager.getConnection(uri, options);
		} catch (SQLException e) {
			Main.getInstance().getLogger().info("Error in connection to mysql: "+ e.getMessage());
		}
		return con;
	}
	
	/** 
	 * Controllo lo stato della connessione 
	 * */
	public static boolean controlloConnessione(Connection con) throws SQLException {
		if(con != null && !con.isClosed()) {
			if(Main.debug) {
				Bukkit.getConsoleSender().sendMessage(Utili.color("&b[&3ReporterGUI&b] &2Database connection check and return false"));	
			}
			PreparedStatement ps;
            try {
                ps = con.prepareStatement("SELECT 1;");
                ps.execute();
                ps.close();
                
            } catch(SQLException ex) {
            	ex.printStackTrace();
            }
			return true;
		} else {
			if(Main.debug) {
				Bukkit.getConsoleSender().sendMessage(Utili.color("&b[&3ReporterGUI&b] &4Database connection check and return true, there is a problem"));
			}
			return false;
		}
	}
	
	/* Creo le tabelle di default */
	private static void creoDefaultTabelle(Connection con) {
		try {	
			if(MysqlReport.controlloConnessione(con)) {
				String sqlCreate = "CREATE TABLE IF NOT EXISTS reporter"
	            + "(ID int NOT NULL AUTO_INCREMENT PRIMARY KEY,"
	            + "PlayerReport varchar(25),"
	            + "UUIDReport varchar(255),"
            	+ "PlayerFrom varchar(25),"
            	+ "UUIDPlayerFrom varchar(255),"
	            + "Reason varchar(255),"
            	+ "WorldReport varchar(30),"
            	+ "WorldFrom varchar(30),"
            	+ "server varchar(32),"
            	+ "status varchar(20),"
            	+ "Time varchar(30),"
            	+ "Coord_from varchar(100),"
            	+ "Coord_to varchar(100),"
            	+ "Expire_time DATETIME)";
				
			    java.sql.Statement stmt = con.createStatement();
			    stmt.execute(sqlCreate);
			    
			    String checkUpdate = "SELECT * FROM reporter";
			    ResultSet resultSet = stmt.executeQuery(checkUpdate);
			    
			    // Update 1.8.1.1
			    ResultSetMetaData rsmd = resultSet.getMetaData();
			    if(rsmd.getColumnTypeName(10).equals("INT")) {
			    	String query = "ALTER TABLE `reporter` CHANGE `status` `status` varchar(20) NULL DEFAULT NULL";
			    	stmt.execute(query);
			    	new ConsoleMessage("&2Update 1.8.1.1 : filed status updated");	
			    }
			    
			    // Update 1.8.2
			    if(hasColumn(stmt.executeQuery(checkUpdate), "Coord_from") == false) {
			    	String query = "ALTER TABLE `reporter` ADD `Coord_from` varchar(100)";
			    	stmt.execute(query);
			    	new ConsoleMessage("Update database: version 1.8.2 (Coord_from)");
			    }
			    if(hasColumn(stmt.executeQuery(checkUpdate), "Coord_to") == false) {
			    	String query = "ALTER TABLE `reporter` ADD `Coord_to` varchar(100)";
			    	stmt.execute(query);
			    	new ConsoleMessage("Update database: version 1.8.2 (Coord_to)");
			    }
			    
			    // Update 1.8.3.1
			    String query_update = "SELECT * FROM `reporter` WHERE `status`= 'APPROVED'";
			    PreparedStatement s = (PreparedStatement) con.prepareStatement(query_update);
			    ResultSet rs = s.executeQuery();
			    
				int count = 0;
				while (rs.next()) {
					count++;
				}
				
				if(count != 0) {
				    String query_update2 = "UPDATE `reporter` SET `status`= 'SOLVED' WHERE `status`= 'APPROVED'";
				    stmt.execute(query_update2);
				    new ConsoleMessage("Update database version 1.8.3.1: "+ count +" report(s) changed in status 'SOLVED' from 'APPROVED'");
				}
				
				// Update 1.8.4
				if(hasColumn(stmt.executeQuery(checkUpdate), "Expire_time") == false) {
			    	String query = "ALTER TABLE `reporter` ADD `Expire_time` DATETIME";
			    	stmt.execute(query);
			    	new ConsoleMessage("Update database: version 1.8.4 (Expire_time)");
			    }
			    
				// Update 1.8.4.1
				if(hasColumn(stmt.executeQuery(checkUpdate), "Perm_group_from") == false) {
			    	String query = "ALTER TABLE `reporter` ADD `Perm_group_from` TEXT";
			    	stmt.execute(query);
			    	new ConsoleMessage("Update database: version 1.8.4.1 (Perm_group_from)");
			    }
				if(hasColumn(stmt.executeQuery(checkUpdate), "Perm_group_to") == false) {
			    	String query = "ALTER TABLE `reporter` ADD `Perm_group_to` TEXT";
			    	stmt.execute(query);
			    	new ConsoleMessage("Update database: version 1.8.4.1 (Perm_group_to)");
			    }
				
			    stmt.close();
			    // Notes
			    new NoteMysql();
			    
			}
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Ritorno con la connessione
	 * @return
	 */
	public static Connection getConnessione(){
		Connection c = MysqlReport.connessioneMysql();
		return c;
	}
	
	/**
	 * Eseguo query senza ritornare, implementando il sistema degli errori
	 * @param query
	 * @throws SQLException
	 */
	public static void MysqlReportRunQuery(String query) throws SQLException {
		Connection c = MysqlReport.connessioneMysql();
		if(MysqlReport.controlloConnessione(c)) {
			if(Main.debug) {
				Bukkit.getConsoleSender().sendMessage("Run a query: "+ query +"");
			}
			Statement stmt = c.createStatement();
			stmt.execute(query);
			stmt.close();
			c.close();
		}
	}	
	
	/**
	 * Controllo se un determinato utente è presente tra le persone segnalate
	 * @return boolean se il player intendicato è presente nel database
	 * @throws SQLException 
	 */
	public static boolean checkPlayerRepStorage(CommandSender send, String p, boolean hasreported) throws SQLException {
	
		/* Prevent sql injection */
		String query;
		if(hasreported == false) {
			query = ("SELECT * from reporter WHERE PlayerFrom=(?)");
		} else {
			query = ("SELECT * FROM reporter WHERE PlayerReport=(?)");
		}
		
		Connection c = MysqlReport.connessioneMysql();
		
		PreparedStatement s = (PreparedStatement) c.prepareStatement(query);
		s.setString(1, p);
		ResultSet rs = s.executeQuery();
		
		int count = 0;
		while (rs.next()) {
			count++;
		}
		
		if(count > 0) {
			return true;
		} else {		
			send.sendMessage(Utili.colorWithPrefix(send, "No report for this user"));
			return false;
		}
	}
	
	/**
	 * Controllo se la segnalazione avendo l'id esiste o meno
	 * 
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	public static boolean checkReportIsExist(int id) throws SQLException {
		
		/* Prevent sql injection */
		String query = ("SELECT * from reporter WHERE ID=(?)");
		Connection c = MysqlReport.connessioneMysql();
		
		PreparedStatement s = (PreparedStatement) c.prepareStatement(query);
		s.setLong(1, id);
		
		ResultSet rs = s.executeQuery();
		
		int count = 0;
		while (rs.next()) {
			count++;
		}
		s.close();
		c.close();
		
		if(count > 0) {
			return true;
		} else {
			return false;
		}
		
	}
	
	
	/**
	 * Controllo se la segnalazione è impostata con il numero 1, cioè in attesa
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public static boolean isOpen(int id) throws SQLException {
		
		/* Prevent sql injection */
		String query = ("SELECT * from reporter WHERE ID=(?) AND status=1");
		Connection c = MysqlReport.connessioneMysql();
		PreparedStatement s = (PreparedStatement) c.prepareStatement(query);
		s.setLong(1, id);
		
		ResultSet rs = s.executeQuery();
		
		int count = 0;
		while (rs.next()) {
			count++;
		}
		s.close();
		rs.close();
		c.close();
		
		if(count > 0) {
			
			return true;
		} else {
			
			return false;
		}
	}
	/**
	 * Faccio la lista dei report per il player cercato con il comando view
	 * (I report che il giocatore ha inviato)
	 * 
	 * @param p -> giocatore del comando
	 * @param target -> giocatore segnalato
	 * @param hasReported (Se è necessario mostrare i report verso il giocatore)
	 * @throws SQLException 
	 */	
	public static void listReportForPlayer(CommandSender p, String target, int index, boolean hasreported) throws SQLException {
		int perpagina = 10;
		int pagina = index == 0 || index < 0 ? 1 : index;
		int paginastart = perpagina * (pagina -1);
		
		Connection c = MysqlReport.connessioneMysql();
		String query;
		if(hasreported) {
			query = ("SELECT * from reporter WHERE PlayerReport=(?) ORDER BY ID ASC");
		} else {
			query = ("SELECT * from reporter WHERE PlayerFrom=(?) ORDER BY ID ASC");
		}
		
		PreparedStatement s = (PreparedStatement) c.prepareStatement(query);
		s.setString(1, target);
		
		ResultSet rs = s.executeQuery();
		
		int count = 0;
		while (rs.next()) {
			count++;
		}
		
		
		// Nuemro delle pagine
		double totalpage = Math.ceil(count / perpagina) + 1;
		
		String querysearch;
		if(hasreported) {
			querysearch = ("SELECT * from reporter WHERE PlayerReport=(?) ORDER BY `reporter`.`ID` DESC LIMIT "+ paginastart +", "+ perpagina  +"");
		} else {
			querysearch = ("SELECT * from reporter WHERE PlayerFrom=(?) ORDER BY `reporter`.`ID` DESC LIMIT "+ paginastart +", "+ perpagina  +"");
		}
		
		PreparedStatement search = (PreparedStatement) c.prepareStatement(querysearch);
		search.setString(1, target);
		
		ResultSet run_s = search.executeQuery();
		
		int num = 0;
		while (run_s.next()) {
			if(num == 0) {
				p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.list-title")));
			}
			
			int id = run_s.getInt("ID");
			String motivazione = run_s.getString("Reason");
			String playerReport = run_s.getString("PlayerReport");
			String time = run_s.getString("Time");
			String status = run_s.getString("status");
			String timeago = TimeManger.getFrom(time);
			String server = run_s.getString("server");
			
			// Preparo la stringa
			String text = null;
			if(playerReport != null) {
				text = ReporterGUIM.getString("message.list-layout");
				text = text.replace("[id]", id + "");
				text = text.replace("[reported]", playerReport);
				text = text.replace("[reason]", motivazione);
				text = text.replace("[time]", timeago);
				text = text.replace("[status]", MysqlStatus.translatecolor(status));
			} else {
				text = ReporterGUIM.getString("message.list-layout-general");
				text = text.replace("[id]", id + "");
				text = text.replace("[reason]", motivazione);
				text = text.replace("[time]", timeago);
				text = text.replace("[status]", MysqlStatus.translatecolor(status));
			}
			
			text = text.replace("[server]", server);
			
			if(p instanceof Player) {
				ReflectionUtil.sendMessageJSON( id, text, ((Player) p), false);
			} else {
				p.sendMessage(Utili.color(p,text));
			}
			
			num++;
		}
		if(num == 0) {
			p.sendMessage(Utili.colorWithPrefix(p,"&bNo reports found!"));
			return;
		}
		
		String footer = ReporterGUIM.getString("message.list-footer");
		footer = footer.replace("[current]", pagina+ "");
		footer = footer.replace("[totpage]", (int) totalpage + "");
		footer = footer.replace("[totalreport]", count + "");
		
		p.sendMessage(Utili.color(p, footer));
		
		s.close();
		rs.close();
		c.close();
	}
	
	/**
	 * Funzione utilizzata dal comando /reportergui view-id 
	 * Per l'informazione di una segnalazione attraverso l'id
	 * 
	 * @param p
	 * @param id
	 * @throws SQLException
	 */
	public static void learReportID(CommandSender p, int id) throws SQLException {
		
		// Query principale
		String query = ("SELECT * from reporter WHERE ID=(?)");
		Connection c = MysqlReport.connessioneMysql();
		PreparedStatement s = (PreparedStatement) c.prepareStatement(query);
		s.setLong(1, id);
		
		ResultSet rs = s.executeQuery();
		int num = 0;
		
		while (rs.next()) {
			
			String motivazione = rs.getString("Reason");
			String segnalato_da = rs.getString("PlayerFrom");
			
			if(p.getName().toLowerCase().equals(segnalato_da.toLowerCase()) == false) {
				if( PermissionUtil._has( p, _Permission.PERM_VIEW_ID_OTHER ) == false && p.isOp() == false ) {
					p.sendMessage(Utili.color(ReporterGUIM.getString("message.info-report-notpermission")));
					return;
				}
			}
			
			String nomePlayer = rs.getString("PlayerReport");
			int id_mysql = rs.getInt("id");
			String nomeMondoSegnalato = rs.getString("WorldReport");
			String time = rs.getString("Time");
			String server = rs.getString("server");
			String status = rs.getString("status");
			String expire_time = rs.getString("Expire_time");
			String perm_group_from = rs.getString("Perm_group_from");
			String perm_group_to = rs.getString("Perm_group_to");
			
			// Visualizzazione campo "server"
			String server_string = null;
			if(Utili.hasServerName()) {
				if(server.equals(Utili.getServerName())) {
					String var_replace = ReporterGUIM.getString("message.info-report-same") != null ? ReporterGUIM.getString("message.info-report-same") : "(this server)";
					server_string = (var_replace);
				}
			}
			if(num == 0) {
				if(!(p instanceof Player)) {
					p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.info-report-title").replace("[id]", id_mysql + "")));
				} else {
					String titolo = ReporterGUIM.getString("message.info-report-title").replace("[id]", id_mysql+ "");
					ReflectionUtil.sendMessageJSON( id_mysql, titolo, ((Player) p));
				}
				
				if( ReporterGUIM.getString("message.info-report-id") != null ) p.sendMessage(Utili.color(p, ReporterGUIM.getString("message.info-report-id").replace("[id]", id_mysql + "")));
				if(nomePlayer != null) {
					if( ReporterGUIM.getString("message.info-report-reported") != null ) p.sendMessage(Utili.color(p, ReporterGUIM.getString("message.info-report-reported").replace("[reported]", nomePlayer)));
				}
				if( ReporterGUIM.getString("message.info-report-reason") != null ) p.sendMessage(Utili.color(p, ReporterGUIM.getString("message.info-report-reason").replace("[reason]", motivazione)));
				if( ReporterGUIM.getString("message.info-report-reportedby") != null ) p.sendMessage(Utili.color(p, ReporterGUIM.getString("message.info-report-reportedby").replace("[reportedby]", segnalato_da)));
				if( ReporterGUIM.getString("message.info-report-time") != null ) p.sendMessage(Utili.color(p, ReporterGUIM.getString("message.info-report-time").replace("[time]", time)).replace("[ago]", TimeManger.getFrom(time)));
				if( ReporterGUIM.getString("message.info-report-server") != null && server != null) {
					server = server != null ? server : "";
					String string = ReporterGUIM.getString("message.info-report-server").replace("[server]", server);
					string = server_string != null ? string.replace("[var]", server_string) : string.replace("[var]", "");
					p.sendMessage(Utili.color(p, string));
				}
				if(nomeMondoSegnalato != null) {
					if( ReporterGUIM.getString("message.info-report-reportedworld") != null && nomeMondoSegnalato != null && nomeMondoSegnalato.equals("NULL")) {
						 p.sendMessage(Utili.color(p, ReporterGUIM.getString("message.info-report-reportedworld").replace("[world]", "&bPlayer offline")));
					} else {
						p.sendMessage(Utili.color(p, ReporterGUIM.getString("message.info-report-reportedworld").replace("[world]", nomeMondoSegnalato)));
					}
				}
				if( ReporterGUIM.getString("message.info-report-status") != null ) {
					p.sendMessage(Utili.color(p, ReporterGUIM.getString("message.info-report-status").replace("[status]", MysqlStatus.translatecolor(status))));
				}
				// Tempo di scadenza del report
				if(expire_time != null) {
					if( ReporterGUIM.getString("message.info-report-autoexpire") != null) {
						p.sendMessage(Utili.color(p, ReporterGUIM.getString("message.info-report-autoexpire").replace("[time]", expire_time).replace("[ago]", TimeManger.getTo(expire_time))));
					}
				}
				// Gruppo del giocatore reportato
				if(perm_group_from != null) {
					if( ReporterGUIM.getString("message.info-report-reported-group") != null) {
						p.sendMessage(Utili.color(p, ReporterGUIM.getString("message.info-report-reported-group").replace("[group]", perm_group_from)));
					}
				}
				// Gruppo del giocatore che ha reportato
				if(perm_group_to != null) {
					if( ReporterGUIM.getString("message.info-report-reportedby-group") != null) {
						p.sendMessage(Utili.color(p, ReporterGUIM.getString("message.info-report-reportedby-group").replace("[group]", perm_group_to)));
					}
				}
				
				// Printo 10 note per il report
				NotesManager.print_notes(p, id, 10, true);
				Utili.EmptyMessage(p);	
				num++;
			}
		}
		if(num == 0) {
			p.sendMessage(Utili.colorWithPrefix(p,"&bReport not found! Use &3/reportergui list"));
		}
		s.close();
		rs.close();
		c.close();
	}
	/**
	 * Elimina una segnalazione avendo l'id
	 * 
	 * @param p
	 * @param id
	 * @throws SQLException 
	 */
	public static void deleteReport(CommandSender p, int id) throws SQLException {
		
		if(checkReportIsExist(id)) {
			
			String query = ("DELETE FROM reporter WHERE ID=(?)");
			Connection c = MysqlReport.connessioneMysql();
			
			PreparedStatement s = (PreparedStatement) c.prepareStatement(query);
			s.setLong(1, id);
			s.execute();
			s.close();
			c.close();
			p.sendMessage(Utili.colorWithPrefix(p,"The report "+ id +" was removed successfully!"));
			
		} else {
			
			p.sendMessage(Utili.colorWithPrefix(p,"This report does not exist!"));
		}
		
	}
	
	/**
	 * Lista dei report attraverso il comando /reportergui list [index]
	 * 
	 * Quindi ampliata con la pagina da visualizzare
	 * @param p -> Player che ha eseguito il comando
	 * @param index -> Pagina da visualizzare
	 * @throws SQLException 
	 */
	public static void getListReportIndex(CommandSender p, int index, String server, String status_param) throws SQLException {
		
		int perpagina = 10;
		int pagina = index == 0 || index < 0 ? 1 : index;
		int paginastart = perpagina * (pagina -1);
		
		boolean hasserver = false;
		boolean hasfilter_status = false;
		
		Connection c = MysqlReport.connessioneMysql();
		String query = null;
		
		// Controllo se è abilitata la funzione server multipli
		if(server != null && server.equals("-sall")) {
			query = "SELECT * from reporter ";
		} else {
			if( Utili.hasServerName()) {
				hasserver = true;
				query = "SELECT * from reporter WHERE `server`= (?) ";
			} else {
				query = "SELECT * from reporter ";
			}
		}
		// Controllo se è stato applicato qualche filtro per gli status
		if(status_param != null) {
			if( Utili.hasServerName() && hasserver) {
				query = "SELECT * from reporter WHERE `server`= (?) AND `status` = '"+ status_param +"' ";
			} else {
				query = "SELECT * from reporter WHERE `status` = '"+ status_param +"' ";
			}
			hasfilter_status = true;
		}
		
		query += "ORDER BY `reporter`.`ID` ASC";
		
		PreparedStatement s = (PreparedStatement) c.prepareStatement(query);
		// Giungo alla statement, dove devo fare lo stesso controllo di prima
		if( Utili.hasServerName()) {
			if(hasserver) {
				s.setString(1, Utili.getServerName());
			}
		}
		ResultSet rs = s.executeQuery();
		
		// Conto quante segnalazioni ci sono nel database
		int tot = 0;
		while (rs.next()) {
			tot++;
		}
		
		// Numero delle pagine
		double totalpage = Math.ceil(tot / perpagina) + 1;
		String querysearch = null;
		
		// Eseguo la query finale
		if(hasserver && hasfilter_status) {
			querysearch = ("SELECT * from reporter WHERE `server`= (?) AND `status` = '"+ status_param +"' ORDER BY `reporter`.`ID` DESC LIMIT "+ paginastart +", "+ perpagina  +"");
		} else if(hasserver && hasfilter_status == false) {
			querysearch = ("SELECT * from reporter WHERE `server`= (?) ORDER BY `reporter`.`ID` DESC LIMIT "+ paginastart +", "+ perpagina  +"");
		} else if(hasserver == false && hasfilter_status) {
			querysearch = ("SELECT * from reporter WHERE `status` = '"+ status_param +"' ORDER BY `reporter`.`ID` DESC LIMIT "+ paginastart +", "+ perpagina  +"");
		} else {
			querysearch = ("SELECT * from reporter ORDER BY `reporter`.`ID` DESC LIMIT "+ paginastart +", "+ perpagina  +"");
		}
		
		PreparedStatement search = (PreparedStatement) c.prepareStatement(querysearch);
		// Imposto nella ricerca principale, il nome del server
		if(hasserver) { 
			search.setString(1, Utili.getServerName());
		}
		
		ResultSet run_s = search.executeQuery();
		
		int num = 0;
		while (run_s.next()) {
			if(num == 0) {
				p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.list-title")));
			}
			
			int id = run_s.getInt("ID");
			String motivazione = run_s.getString("Reason");
			String playerReport = run_s.getString("PlayerReport");
			String time = run_s.getString("Time");
			String timeago = TimeManger.getFrom(time);
			String status = run_s.getString("status");
			String server_name = run_s.getString("server");
			
			// Preparo la stringa
			String text = null;
			if(playerReport != null) {
				text = ReporterGUIM.getString("message.list-layout");
				text = text.replace("[id]", id+ "");
				text = text.replace("[reported]", playerReport);
				text = text.replace("[reason]", motivazione);
				text = text.replace("[time]", timeago);
				text = text.replace("[status]", MysqlStatus.translatecolor(status));
			} else {
				text = ReporterGUIM.getString("message.list-layout-general");
				text = text.replace("[id]", id+ "");
				text = text.replace("[reason]", motivazione);
				text = text.replace("[time]", timeago);
				text = text.replace("[status]", MysqlStatus.translatecolor(status));
			}
			
			text = text.replace("[server]", server_name);
			
			if(p instanceof Player) {
				ReflectionUtil.sendMessageJSON( id, text, ((Player) p), false);
			} else {
				p.sendMessage(Utili.color(p,text));
			}
			
			num++;
		}
		// Se non è presente nessun report invio il messaggio di errore
		if(num == 0) {
			if(hasserver) {
				// In caso cerco nel server specifico, cambio il messaggio
				p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.list-not-found-forserver").replace("[command-list]", "/"+ValueCustom.getCommandMain() + " list -sall")));
			} else {
				p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.list-not-found")));
			}
			return;
		}
		String footer = ReporterGUIM.getString("message.list-footer");
		footer = footer.replace("[current]", pagina+ "");
		footer = footer.replace("[totpage]", (int) totalpage + "");
		footer = footer.replace("[totalreport]", tot + "");
		
		p.sendMessage(Utili.color(p, footer));
		c.close();
	}
	
	/**
	 * Lista dei report totali
	 * Richiamata dalla class Stats del package it.itpao25.NMSReport.Stats
	 * @throws SQLException 
	 */
	public static int getTotalReport() throws SQLException {
		
		String query = null;
		boolean has = false;
		
		if( Utili.hasServerName()) {
			has = true;
			query = ("SELECT * from reporter WHERE `server`= (?)");
		} else {
			query = ("SELECT * from reporter");
		}
		
		Connection c = MysqlReport.connessioneMysql();
		PreparedStatement s = (PreparedStatement) c.prepareStatement(query);
		if(has) {
			s.setString(1, Utili.getServerName());
		}
		ResultSet rs = s.executeQuery();
		
		int count = 0;
		while (rs.next()) {
			count++;
		}
		s.close();
		rs.close();
		c.close();
		return count;
		
	}
	
	/**
	 * Lista dei report completati (Status = 2)
	 * Richiamata della class Stats del package it.itpao25.NMSReport.Stats
	 * @throws SQLException 
	 */
	public static int getTotalReportApproved() throws SQLException {
		String query = null;
		boolean has = false;
		
		if( Utili.hasServerName()) {
			has = true;
			query = ("SELECT * from reporter WHERE `server`= (?) AND `status`='" + MysqlStatus.SOLVED +"'");
		} else {
			query = ("SELECT * from reporter WHERE `status`='" + MysqlStatus.SOLVED +"'");
		}
		
		Connection c = MysqlReport.connessioneMysql();
		PreparedStatement s = (PreparedStatement) c.prepareStatement(query);
		if(has) {
			s.setString(1, Utili.getServerName());
		}
		
		ResultSet rs = s.executeQuery();
		int count = 0;
		while (rs.next()) {
			count++;
		}
		s.close();
		rs.close();
		c.close();
		
		return count;
	}
	
	/**
	 * Lista dei report aperti
	 * Richiamata della class Stats del package it.itpao25.NMSReport.Stats
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static int getTotalOpen() throws SQLException {
		String query = null;
		boolean has = false;
		
		if( Utili.hasServerName()) {
			has = true;
			query = ("SELECT * from reporter WHERE `server`= (?) AND status='" + MysqlStatus.OPEN+"'");
		} else {
			query = ("SELECT * from reporter WHERE status='" + MysqlStatus.OPEN+"'");
		}
		
		Connection c = MysqlReport.connessioneMysql();
		PreparedStatement s = (PreparedStatement) c.prepareStatement(query);
		if(has) {
			s.setString(1, Utili.getServerName());
		}
		ResultSet rs = s.executeQuery();
		
		int count = 0;
		while (rs.next()) {
			count++;
		}
		
		rs.close();
		s.close();
		c.close();
		
		return count;
	}
	
	/**
	 * Ritorno con il numero delle segnalazioni che ha fatto un utente e sono state apprvate
	 * Richiamata della class Stats del package it.itpao25.NMSReport.Stats
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static int getTotalReportOwnApproved(OfflinePlayer p) throws SQLException {
		String query = null;
		boolean has = false;
		
		if( Utili.hasServerName()) {
			has = true;
			query = ("SELECT * from reporter WHERE `server`= (?) AND `status`='" + MysqlStatus.SOLVED +"' AND `PlayerFrom`=(?)");
		} else {
			query = ("SELECT * from reporter WHERE `status`='" + MysqlStatus.SOLVED +"' AND `PlayerFrom`=(?)");
			
		}
		
		Connection c = MysqlReport.connessioneMysql();
		PreparedStatement s = (PreparedStatement) c.prepareStatement(query);
		if(has) {
			s.setString(1, Utili.getServerName());
			s.setString(2, p.getName());
		} else {
			s.setString(1, p.getName());
		}
		ResultSet rs = s.executeQuery();
		
		int count = 0;
		while (rs.next()) {
			count++;
		}
		
		rs.close();
		s.close();
		c.close();
		
		return count;
	}
	
	/**
	 * Int del numero di report che ha fatto un player
	 * Richiamata della class Stats del package it.itpao25.NMSReport.Stats
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static int getTotalReportOwn(OfflinePlayer p) throws SQLException {
		String query = null;
		boolean has = false;
		
		if( Utili.hasServerName()) {
			has = true;
			query = ("SELECT * from reporter WHERE `server`= (?) AND `status`='" + MysqlStatus.SOLVED +"' AND `PlayerReport`=(?)");
		} else {
			query = ("SELECT * from reporter WHERE `status`='" + MysqlStatus.SOLVED +"' AND `PlayerReport`=(?)"); 
		}
		
		Connection c = MysqlReport.connessioneMysql();
		PreparedStatement s = (PreparedStatement) c.prepareStatement(query);
		if(has) {
			s.setString(1, Utili.getServerName());
			s.setString(2, p.getName());
		} else {
			s.setString(1, p.getName());
		}
		ResultSet rs = s.executeQuery();
		
		int count = 0;
		while (rs.next()) {
			count++;
		}
		
		rs.close();
		s.close();
		c.close();
		
		return count;
	}
	
	/**
	 * Conto le segnalazioni aperte per il giocatore online
	 * @param p
	 * @return
	 * @throws SQLException
	 */
	public static int getTotalReportOwnOpen(Player p) throws SQLException {
		Connection c = MysqlReport.connessioneMysql();
		String query = null;
		boolean hasserver = false;
		
		query = ("SELECT * from reporter WHERE PlayerReport=(?) AND status='" + MysqlStatus.OPEN + "'");
		if(Utili.hasServerName()) {
			hasserver = true;
			query = ("SELECT * from reporter WHERE `PlayerReport`=(?) AND status='" + MysqlStatus.OPEN + "' AND `server`= (?)");
		}
		
		PreparedStatement s = (PreparedStatement) c.prepareStatement(query);
		s.setString(1, p.getName());
		if(hasserver) {
			s.setString(2, Utili.getServerName());
		}
		s.execute();
		
		ResultSet rs = s.executeQuery();
		
		int count = 0;
		while (rs.next()) {
			count++;
		}
		
		rs.close();
		s.close();
		c.close();
		
		return count;
	}
	
	/**
	 * Elimino tutti i report per un utente
	 * @param p
	 * @param player
	 * @throws SQLException
	 */
	public static void clearReport(String p, CommandSender player) throws SQLException {
		Connection c = MysqlReport.connessioneMysql();
		// Controllo se il player che si sta cercando di clearare contiene almeno un report
		String queryCheck = ("SELECT PlayerReport from reporter WHERE PlayerReport=(?)");
		
		PreparedStatement check = (PreparedStatement) c.prepareStatement(queryCheck);
		check.setString(1, p);
		ResultSet rs = check.executeQuery();
		
		int count = 0;
		while (rs.next()) {
			count++;
		}
		
		if(count >= 1) {
			String query = ("DELETE FROM reporter WHERE PlayerReport=(?)");
			PreparedStatement s = (PreparedStatement) c.prepareStatement(query);
			s.setString(1, p);
			s.execute();
			String nomeGiocatore = null;
			while (rs.next()) {
				nomeGiocatore = rs.getString("PlayerReport");
			}
			s.close();
			player.sendMessage(Utili.colorWithPrefix(player, "&aEliminated "+ count +" report(s) for the user "+ nomeGiocatore +""));
			
		} else {
			player.sendMessage(Utili.colorWithPrefix(player, "&aNo reports were found for this player"));
		}
		check.close();
		c.close();
	}
	
	/**
	 * Elimino tutti i report per un utente
	 * @param p
	 * @param player
	 * @throws SQLException
	 */
	public static void clearAllReportStatus(CommandSender player, String status) throws SQLException {
		Connection c = MysqlReport.connessioneMysql();
		// Controllo se il player che si sta cercando di clearare contiene almeno un report
		String queryCheck = ("SELECT PlayerReport from reporter WHERE `status`='" + status +"'");
		
		PreparedStatement check = (PreparedStatement) c.prepareStatement(queryCheck);
		ResultSet rs = check.executeQuery();
		
		int count = 0;
		while (rs.next()) {
			count++;
		}
		
		if(count >= 1) {
			String query = ("DELETE FROM reporter WHERE `status`='" + status +"'");
			PreparedStatement s = (PreparedStatement) c.prepareStatement(query);
			s.execute();
			s.close();
			player.sendMessage(Utili.colorWithPrefix(player, "&aEliminated "+ count +" report(s)"));
			
		} else {
			player.sendMessage(Utili.colorWithPrefix(player, "&aNo reports were found in this status"));
		}
		check.close();
		c.close();
	}
	
	/**
	 * Elimino tutti i report per un utente
	 * @param p
	 * @param player
	 * @throws SQLException
	 */
	public static void clearAllReport(CommandSender player) throws SQLException {
		Connection c = MysqlReport.connessioneMysql();
		// Controllo se il player che si sta cercando di clearare contiene almeno un report
		String queryCheck = ("SELECT PlayerReport from reporter");
		
		PreparedStatement check = (PreparedStatement) c.prepareStatement(queryCheck);
		ResultSet rs = check.executeQuery();
		
		int count = 0;
		while (rs.next()) {
			count++;
		}
		
		if(count >= 1) {
			String query = ("TRUNCATE TABLE reporter");
			PreparedStatement s = (PreparedStatement) c.prepareStatement(query);
			s.execute();
			s.close();
			
			String query2 = ("TRUNCATE TABLE reporter_notes");
			PreparedStatement s2 = (PreparedStatement) c.prepareStatement(query2);
			s2.execute();
			s2.close();
			player.sendMessage(Utili.colorWithPrefix(player, "&aEliminated "+ count +" report(s)"));
		} else {
			player.sendMessage(Utili.colorWithPrefix(player, "&aNo reports were found"));
		}
		check.close();
		c.close();
	}
	
	/**
	 * Lista dei report attraverso il comando /reportergui list [index]
	 * Questa lista contiene solo i report own
	 * 
	 * Quindi ampliata con la pagina da visualizzare
	 * @param p -> Player che ha eseguito il comando
	 * @param index -> Pagina da visualizzare
	 * @throws SQLException 
	 */
	public static void getListReportIndexOwn(CommandSender p, int index) throws SQLException {
		
		int perpagina = 10;
		int pagina = index == 0 || index < 0 ? 1 : index;
		int paginastart = perpagina * (pagina -1);
		
		Connection c = MysqlReport.connessioneMysql();
		String query = null;
		
		query = "SELECT * from reporter WHERE `PlayerFrom`='"+ p.getName() +"' ";
		query += "ORDER BY `reporter`.`ID` ASC";
		PreparedStatement s = (PreparedStatement) c.prepareStatement(query);
		ResultSet rs = s.executeQuery();
		
		// Conto quante segnalazioni ci sono nel database
		int tot = 0;
		while (rs.next()) {
			tot++;
		}
		
		// Numero delle pagine
		double totalpage = Math.ceil(tot / perpagina) + 1;
		String querysearch = null;
		
		// Eseguo la query finale
		querysearch = ("SELECT * from reporter WHERE `PlayerFrom`='"+ p.getName() +"' ORDER BY `reporter`.`ID` DESC LIMIT "+ paginastart +", "+ perpagina  +"");
		PreparedStatement search = (PreparedStatement) c.prepareStatement(querysearch);
		
		ResultSet run_s = search.executeQuery();
		
		int num = 0;
		while (run_s.next()) {
			if(num == 0) {
				p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.list-title-own")));
			}
			
			String id = run_s.getString("ID");
			String motivazione = run_s.getString("Reason");
			String playerReport = run_s.getString("PlayerReport");
			String time = run_s.getString("Time");
			String timeago = TimeManger.getFrom(time);
			String status = run_s.getString("status");
			String server = run_s.getString("server");
			
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
		// Se non è presente nessun report invio il messaggio di errore
		if(num == 0) {
			p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.list-not-found")));
			return;
		}
		
		String footer = ReporterGUIM.getString("message.list-footer");
		footer = footer.replace("[current]", pagina+ "");
		footer = footer.replace("[totpage]", (int) totalpage + "");
		footer = footer.replace("[totalreport]", tot + "");
		
		p.sendMessage(Utili.color(p, footer));
		c.close();
	}
	
	/**
     * Controllo se una colonna esiste nel database
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public static boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columns = rsmd.getColumnCount();
        for (int x = 1; x <= columns; x++) {
            if (columnName.equals(rsmd.getColumnName(x))) {
                return true;
            }
        }
        return false;
    }
    	
    /**
     * Ritorno con le coordinate del report
     * @param id
     * @return
     * @throws SQLException 
     */
    public static String getCoordReport(Player p, int id) throws SQLException {
    	if(checkReportIsExist(id) == false) {
    		p.sendMessage(Utili.colorWithPrefix(p,"&bReport not found! Use &3/reportergui list"));
    		return null;
    	}
    	
    	String query = ("SELECT * from reporter WHERE ID=(?)");
		Connection c = MysqlReport.connessioneMysql();
		
		PreparedStatement s = (PreparedStatement) c.prepareStatement(query);
		s.setLong(1, id);
		
		ResultSet rs = s.executeQuery();
		
		String coord_stringa = null;
		while (rs.next()) {
			coord_stringa = rs.getString("Coord_from");
		}
		rs.close();
		s.close();
		
		if(coord_stringa != null && coord_stringa.trim().equals("") == false) {
			return coord_stringa;
		}
		
		p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.tp-report-notvalid")));
		
    	return null;
    }
    
    /**
     * Sistema di controllo dei reports da impostare come scaduti
     * 
     * @param date
     * @return
     */
    public static boolean TaskExpireReports(String date) {
		
    	String query = ("SELECT * FROM `reporter` WHERE `status` = 'OPEN' AND `Expire_time` <= (?)");
		Connection c = MysqlReport.connessioneMysql();
		
		PreparedStatement s = null;
		try {
			
			s = (PreparedStatement) c.prepareStatement(query);
			s.setString(1, date);
			ResultSet rs = s.executeQuery();
			while (rs.next()) {
				if(MysqlStatus.expired(Bukkit.getConsoleSender(), rs.getInt("id"), false)) {
					new ConsoleMessage("&6The report #"+ rs.getString("id") +" has been set as expired! (Automatic Task)");
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
    	return true;
    }
}