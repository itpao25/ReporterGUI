package it.itpao25.NMSReport.command;

import java.sql.SQLException;

import it.itpao25.NMSReport.config.ReporterGUIM;
import it.itpao25.NMSReport.permission.PermissionUtil;
import it.itpao25.NMSReport.permission._Permission;
import it.itpao25.NMSReport.storage.MysqlReport;
import it.itpao25.NMSReport.storage.MysqlStatus;
import it.itpao25.NMSReport.util.Utili;

import org.bukkit.command.CommandSender;

public class CommandList 
{
	private String SYNTAX_COMMAND  = "&bUse &3/"+ ValueCustom.getCommandMain() +" list [-sall] [-solved | -expired | -declined | -open] [page] &bfor a list of reports";
	private String DB_ERROR_COMMAND  = "&4Error in listening to the database";
	
	// Gestione del debug tramite comando
	// In modo da modificare il valore nel config su debug: true | false
	public CommandList(CommandSender p, String[] args) 
	{
		if (MysqlReport.isMysqlEnable() == false)  {
			p.sendMessage(Utili.colorWithPrefix(p, Utili.getMessageDatabaseNotEnable()));
			return;
		}
		if( PermissionUtil._has( p, _Permission.PERM_LIST_OTHER ) || p.isOp() ) {
			// Se ha il permesso di vedere i report degli altri 
			if (args.length == 1) {
				try  {
					MysqlReport.getListReportIndex(p, 0, null, null);
				} catch (SQLException e)  {
					e.printStackTrace();
					p.sendMessage(Utili.colorWithPrefix(p, DB_ERROR_COMMAND));
				}
			} else if (args.length == 2) {
				
				// Se viene richiamata subito la pagina
				if(Utili.isNumero(args[1])) {
					try {
						MysqlReport.getListReportIndex(p, Integer.parseInt(args[1]), null, null);
					} catch (SQLException e) {
						e.printStackTrace();
						p.sendMessage(Utili.colorWithPrefix(p, DB_ERROR_COMMAND));
					}
				} else if (args[1].equalsIgnoreCase("-sall")) {
					// Visualizzo tutti i report tramite il comando list
					// Quindi, controllo il permesso del giocatore
					if( !PermissionUtil._has( p, _Permission.PERM_LIST_ALL ) && !p.isOp() ) {
						p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.nopermission")));
						return; 
					}
					try {
						MysqlReport.getListReportIndex(p, 0, "-sall", null);
					} catch (SQLException e) {
						e.printStackTrace();
						p.sendMessage(Utili.colorWithPrefix(p, DB_ERROR_COMMAND));
					}
				} else if (args[1].equalsIgnoreCase("-solved")) {
					// Lista delle segnalazioni approvate
					try {
						MysqlReport.getListReportIndex(p, 0, null, MysqlStatus.SOLVED);
					} catch (SQLException e) {
						e.printStackTrace();
						p.sendMessage(Utili.colorWithPrefix(p, DB_ERROR_COMMAND));
					}
				} else if (args[1].equalsIgnoreCase("-expired")) {
					try {
						MysqlReport.getListReportIndex(p, 0, null, MysqlStatus.EXPIRED);
					} catch (SQLException e) {
						e.printStackTrace();
						p.sendMessage(Utili.colorWithPrefix(p, DB_ERROR_COMMAND));
					}
				} else if (args[1].equalsIgnoreCase("-declined")) {
					try {
						MysqlReport.getListReportIndex(p, 0, null, MysqlStatus.DECLINED);
					} catch (SQLException e) {
						e.printStackTrace();
						p.sendMessage(Utili.colorWithPrefix(p, DB_ERROR_COMMAND));
					}
				} else if (args[1].equalsIgnoreCase("-open")) {
					try {
						MysqlReport.getListReportIndex(p, 0, null, MysqlStatus.OPEN);
					} catch (SQLException e) {
						e.printStackTrace();
						p.sendMessage(Utili.colorWithPrefix(p, DB_ERROR_COMMAND));
					}
				} else {
					p.sendMessage(Utili.colorWithPrefix(p, SYNTAX_COMMAND));
				}
			} else if (args.length == 3) 
			{
				// Con 3, tento di risolvere solo il numero
				if(Utili.isNumero(args[2])) {
					if (args[1].equalsIgnoreCase("-sall")) {
						
						try {
							MysqlReport.getListReportIndex(p, Integer.parseInt(args[2]), "-sall", null);
						} catch (SQLException e) {
							e.printStackTrace();
							p.sendMessage(Utili.colorWithPrefix(p, DB_ERROR_COMMAND));
						}
						
					} else if (args[1].equalsIgnoreCase("-solved")) {
						
						// Lista delle segnalazioni approvate
						try {
							MysqlReport.getListReportIndex(p, Integer.parseInt(args[2]), null, MysqlStatus.SOLVED);
						} catch (SQLException e) {
							e.printStackTrace();
							p.sendMessage(Utili.colorWithPrefix(p, DB_ERROR_COMMAND));
						}
						
					} else if (args[1].equalsIgnoreCase("-expired")) {
						
						try {
							MysqlReport.getListReportIndex(p, Integer.parseInt(args[2]), null, MysqlStatus.EXPIRED);
						} catch (SQLException e) {
							e.printStackTrace();
							p.sendMessage(Utili.colorWithPrefix(p, DB_ERROR_COMMAND));
						}
						
					} else if (args[1].equalsIgnoreCase("-declined")) {
						
						try {
							MysqlReport.getListReportIndex(p, Integer.parseInt(args[2]), null, MysqlStatus.DECLINED);
						} catch (SQLException e) {
							e.printStackTrace();
							p.sendMessage(Utili.colorWithPrefix(p, DB_ERROR_COMMAND));
						}
						
					} else if (args[1].equalsIgnoreCase("-open")) {
						
						try {
							MysqlReport.getListReportIndex(p, Integer.parseInt(args[2]), null, MysqlStatus.OPEN);
						} catch (SQLException e) {
							e.printStackTrace();
							p.sendMessage(Utili.colorWithPrefix(p, DB_ERROR_COMMAND));
						}
						
					} else {
						p.sendMessage(Utili.colorWithPrefix(p, SYNTAX_COMMAND));
					}
					
				// rrg list -sall -expired 
				} else if(args[1].equalsIgnoreCase("-sall") && args[2].equalsIgnoreCase("-expired")) {
					
					// Lista delle segnalazioni approvate
					try {
						MysqlReport.getListReportIndex(p, 0, "-sall", MysqlStatus.EXPIRED);
					} catch (SQLException e) {
						e.printStackTrace();
						p.sendMessage(Utili.colorWithPrefix(p, DB_ERROR_COMMAND));
					}
					
				// rrg list -sall -declined
				} else if(args[1].equalsIgnoreCase("-sall") && args[2].equalsIgnoreCase("-declined")) {
					
					// Lista delle segnalazioni approvate
					try {
						MysqlReport.getListReportIndex(p, 0, "-sall", MysqlStatus.DECLINED);
					} catch (SQLException e) {
						e.printStackTrace();
						p.sendMessage(Utili.colorWithPrefix(p, DB_ERROR_COMMAND));
					}
					
				// rrg list -sall -solved
				} else if(args[1].equalsIgnoreCase("-sall") && args[2].equalsIgnoreCase("-solved")) {
					
					// Lista delle segnalazioni approvate
					try {
						MysqlReport.getListReportIndex(p, 0, "-sall", MysqlStatus.SOLVED);
					} catch (SQLException e) {
						e.printStackTrace();
						p.sendMessage(Utili.colorWithPrefix(p, DB_ERROR_COMMAND));
					}
					
				// rrg list -sall -open
				} else if(args[1].equalsIgnoreCase("-sall") && args[2].equalsIgnoreCase("-open")) {
					
					// Lista delle segnalazioni approvate
					try {
						MysqlReport.getListReportIndex(p, 0, "-sall", MysqlStatus.OPEN);
					} catch (SQLException e) {
						e.printStackTrace();
						p.sendMessage(Utili.colorWithPrefix(p, DB_ERROR_COMMAND));
					}
				} else {
					p.sendMessage(Utili.colorWithPrefix(p, SYNTAX_COMMAND));
				}
			} else if (args.length == 4) 
			{
				// rrg list <-sall> <-approved | -declined | -expired | -open> <page
				if(Utili.isNumero(args[3]) && args[1].equalsIgnoreCase("-sall")) {
					
					if (args[2].equalsIgnoreCase("-solved")) {
						
						// Lista delle segnalazioni approvate
						try {
							MysqlReport.getListReportIndex(p, Integer.parseInt(args[3]), "-sall", MysqlStatus.SOLVED);
						} catch (SQLException e) {
							e.printStackTrace();
							p.sendMessage(Utili.colorWithPrefix(p, DB_ERROR_COMMAND));
						}
						
					} else if (args[1].equalsIgnoreCase("-expired")) {
						
						try {
							MysqlReport.getListReportIndex(p, Integer.parseInt(args[3]), "-sall", MysqlStatus.EXPIRED);
						} catch (SQLException e) {
							e.printStackTrace();
							p.sendMessage(Utili.colorWithPrefix(p, DB_ERROR_COMMAND));
						}
						
					} else if (args[1].equalsIgnoreCase("-declined")) {
						
						try {
							MysqlReport.getListReportIndex(p, Integer.parseInt(args[3]), "-sall", MysqlStatus.DECLINED);
						} catch (SQLException e) {
							e.printStackTrace();
							p.sendMessage(Utili.colorWithPrefix(p, DB_ERROR_COMMAND));
						}
						
					} else if (args[1].equalsIgnoreCase("-open")) {
						
						try {
							MysqlReport.getListReportIndex(p, Integer.parseInt(args[3]), "-sall", MysqlStatus.OPEN);
						} catch (SQLException e) {
							e.printStackTrace();
							p.sendMessage(Utili.colorWithPrefix(p, DB_ERROR_COMMAND));
						}
					}
				} else {
					p.sendMessage(Utili.colorWithPrefix(p, SYNTAX_COMMAND));
				}
			} else {
				p.sendMessage(Utili.colorWithPrefix(p, SYNTAX_COMMAND));
			}
		} else if( PermissionUtil._has( p, _Permission.PERM_LIST_OWN ) || p.isOp() ) {
			// Se non ha il permesso per vedere gli altri report, ma solo i suoi
			if (args.length == 1) {
				try  {
					MysqlReport.getListReportIndexOwn(p, 0);
				} catch (SQLException e)  {
					e.printStackTrace();
					p.sendMessage(Utili.colorWithPrefix(p, DB_ERROR_COMMAND));
				}
			} else if (args.length == 2) 
			{
				// Se viene richiamata subito la pagina
				if(Utili.isNumero(args[1])) {
					try {
						MysqlReport.getListReportIndexOwn(p, Integer.parseInt(args[1]));
					} catch (SQLException e) {
						e.printStackTrace();
						p.sendMessage(Utili.colorWithPrefix(p, DB_ERROR_COMMAND));
					}
				}
			} else {
				p.sendMessage(Utili.colorWithPrefix(p, "&bUse &3/"+ ValueCustom.getCommandMain() +" list &bfor view all your reports"));
			}
		} else {
			// Nessun permesso
			p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.nopermission")));
			return; 
		}
	}
}
