package it.itpao25.NMSReport.storage;

import it.itpao25.NMSReport.config.ReporterGUIM;
import it.itpao25.NMSReport.util.TimeManger;
import it.itpao25.NMSReport.util.Utili;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.bukkit.command.CommandSender;

public class MysqlSearch {
	
	private String text;
	private CommandSender p;
	private int index;
	
	public MysqlSearch() {
		
	}
	
	/**
	 * Imposto dall'esterno il giocatore che deve essere
	 * segnalato
	 * 
	 * @param p
	 * @return
	 */
	public boolean setPlayer(CommandSender p) {
		this.p = p;
		return true;
	}
	
	/**
	 * Imposto dall'esterno la stringa per eseguire la ricerca
	 * @param text
	 * @return
	 */
	public boolean setString(String text) {
		this.text = text;
		
		return true;
	}
	
	public boolean setIndex(int index) {
		this.index = index;
		
		return true;
	}
	
	/**
	 * Eseguo la query per iniziare la ricerca
	 * @return
	 */
	private List<String> query() {
		if(this.text == null || this.p == null) {
			return null;
		}
		int perpagina = 10;
		int pagina = index == 0 || index < 0 ? 1 : index;
		int paginastart = perpagina * (pagina -1);
		
		String query = ("SELECT * FROM reporter WHERE Reason LIKE ? OR server LIKE ?");
		try {
			
			Connection c = MysqlReport.connessioneMysql();
			PreparedStatement s = (PreparedStatement) c.prepareStatement(query);
			s.setString(1, "%"+ this.text +"%");
			s.setString(2, "%"+ this.text +"%");
			ResultSet rs = s.executeQuery();
			
			// Conto quante segnalazioni ci sono nel database
			int tot = 0;
			while (rs.next()) {
				tot++;
			}
			
			// Nuemro delle pagine
			double totalpage = Math.ceil(tot / perpagina) + 1;
			
			String querysearch = ("SELECT * FROM reporter WHERE Reason LIKE (?) OR server LIKE (?) ORDER BY `reporter`.`ID` DESC LIMIT "+ paginastart +", "+ perpagina  +"");
			PreparedStatement search = (PreparedStatement) c.prepareStatement(querysearch);
			search.setString(1, "%"+ this.text +"%");
			search.setString(2, "%"+ this.text +"%");
			
			ResultSet run_s = search.executeQuery();
			String title = ReporterGUIM.getString("message.search-title").replace("[word]", this.text);
			p.sendMessage(Utili.colorWithPrefix(p, title));
			
			int num = 0;
			while (run_s.next()) {
				
				String reason = run_s.getString("Reason");
				String by = run_s.getString("PlayerFrom");
				String to = run_s.getString("PlayerReport");
				String id = run_s.getString("id");
				String world = run_s.getString("WorldReport");
				String time = run_s.getString("Time");
				String server = rs.getString("server");
				String status = run_s.getString("status");
				
				// Visualizzazione campo "server"
				String server_string = null;
				if(Utili.hasServerName()) {
					if(server.equals(Utili.getServerName())) {
						String var_replace = ReporterGUIM.getString("message.info-report-same") != null ? ReporterGUIM.getString("message.info-report-same") : "(this server)";
						server_string = (var_replace);
					}
				}
				
				
				
				if( ReporterGUIM.getString("message.info-report-id") != null ) p.sendMessage(Utili.color(p, ReporterGUIM.getString("message.info-report-id").replace("[id]", id + "")));
				if(to != null) {
					if( ReporterGUIM.getString("message.info-report-reported") != null ) p.sendMessage(Utili.color(p, ReporterGUIM.getString("message.info-report-reported").replace("[reported]", to)));
				}
				if( ReporterGUIM.getString("message.info-report-reason") != null ) p.sendMessage(Utili.color(p, ReporterGUIM.getString("message.info-report-reason").replace("[reason]", reason)));
				if( ReporterGUIM.getString("message.info-report-reportedby") != null ) p.sendMessage(Utili.color(p, ReporterGUIM.getString("message.info-report-reportedby").replace("[reportedby]", by)));
				if( ReporterGUIM.getString("message.info-report-time") != null ) p.sendMessage(Utili.color(p, ReporterGUIM.getString("message.info-report-time").replace("[time]", time)).replace("[ago]", TimeManger.getFrom(time)));
				if( ReporterGUIM.getString("message.info-report-server") != null ) {
					server = server != null ? server : "";
					String string = ReporterGUIM.getString("message.info-report-server").replace("[server]", server);
					string = server_string != null ? string.replace("[var]", server_string) : string.replace("[var]", "");
					p.sendMessage(Utili.color(p, string));
				}
				if(world != null) {
					if( ReporterGUIM.getString("message.info-report-reportedworld") != null && world != null && world.equals("")) {
						 p.sendMessage(Utili.color(p, ReporterGUIM.getString("message.info-report-reportedworld").replace("[world]", "&bPlayer offline")));
					} else {
						p.sendMessage(Utili.color(p, ReporterGUIM.getString("message.info-report-reportedworld").replace("[world]", world)));
					}
				}
				if( ReporterGUIM.getString("message.info-report-status") != null ) {
					p.sendMessage(Utili.color(p, ReporterGUIM.getString("message.info-report-status").replace("[status]", MysqlStatus.translatecolor(status))));
				}
				Utili.EmptyMessage(p);	
				num++;
			}
			if(num == 0) {
				p.sendMessage(Utili.colorWithPrefix(p,"&bNo reports found for this search!"));
				return null;
			}
			
			String footer = ReporterGUIM.getString("message.search-result");
			footer = footer.replace("[current]", pagina+ "");
			footer = footer.replace("[totpage]", (int) totalpage + "");
			footer = footer.replace("[totalreport]", tot + "");
			
			p.sendMessage(Utili.color(p, footer));
			s.close();
			c.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Eseguo il render della ricerca
	 * @return
	 */
	public boolean render() {
		query();
		return false;
	}
}
