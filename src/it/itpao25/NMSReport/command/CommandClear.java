package it.itpao25.NMSReport.command;

import java.sql.SQLException;

import it.itpao25.NMSReport.config.ReporterGUIC;
import it.itpao25.NMSReport.permission.PermissionUtil;
import it.itpao25.NMSReport.permission._Permission;
import it.itpao25.NMSReport.storage.MysqlReport;
import it.itpao25.NMSReport.storage.MysqlStatus;
import it.itpao25.NMSReport.util.Utili;

import org.bukkit.command.CommandSender;

public class CommandClear {
	public CommandClear(CommandSender p, String[] args) 
	{
		if(!PermissionUtil._has( p, _Permission.PERM_USE ) && !p.isOp()) {
			p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIC.getString("message.nopermission")));
			return; 
		}
		if (MysqlReport.isMysqlEnable() == false) {
			p.sendMessage(Utili.colorWithPrefix(p, Utili.getMessageDatabaseNotEnable()));
			return;
		}
		if (args.length == 2) {
			if(args[1].equals("-id")) {
				p.sendMessage(Utili.colorWithPrefix(p, "&bUse &3/"+ ValueCustom.getCommandMain() +" < -all | --all-approved | --all-declined | --all-expired | nickname | -id > [id] &bto remove reports"));
				return;
			} else if(args[1].equals("-all")) {
				// Elimino tutti i report
				try {
					MysqlReport.clearAllReport(p);
				} catch (SQLException e) 
				{
					p.sendMessage(Utili.colorWithPrefix(p, "Error in listening to the database"));
				}
				return;
			} else if(args[1].equals("--all-solved")) {
				// Elimino tutti i report approvati
				try {
					MysqlReport.clearAllReportStatus(p, MysqlStatus.SOLVED);
				} catch (SQLException e) 
				{
					p.sendMessage(Utili.colorWithPrefix(p, "Error in listening to the database"));
				}
				return;
			} else if(args[1].equals("--all-declined")) {
				// Elimino tutti i report declinati
				try {
					MysqlReport.clearAllReportStatus(p, MysqlStatus.DECLINED);
				} catch (SQLException e) 
				{
					p.sendMessage(Utili.colorWithPrefix(p, "Error in listening to the database"));
				}
				return;
			} else if(args[1].equals("--all-expired")) {
				// Eliminati tutti i report scaduti
				try {
					MysqlReport.clearAllReportStatus(p, MysqlStatus.EXPIRED);
				} catch (SQLException e) 
				{
					p.sendMessage(Utili.colorWithPrefix(p, "Error in listening to the database"));
				}
				return;
			} else {	
				try {
					MysqlReport.clearReport(args[1], p);
				} catch (SQLException e) 
				{
					p.sendMessage(Utili.colorWithPrefix(p, "Error in listening to the database"));
				}
				return;
			}
		} else if (args.length == 3) {
			if(args[1].equals("-id") && Utili.isNumero(args[2])) {
				try {
					MysqlReport.deleteReport(p,Integer.parseInt(args[2]));
				} catch (NumberFormatException | SQLException e) {
					e.printStackTrace();
					p.sendMessage(Utili.colorWithPrefix(p, "Error in listening to the database"));
				}
				return;
			}
		}
		p.sendMessage(Utili.colorWithPrefix(p, "&bUse &3/"+ ValueCustom.getCommandMain() +" clear < -all | --all-approved | --all-declined | --all-expired | nickname | -id > [id] &bto remove reports"));
	}
}
