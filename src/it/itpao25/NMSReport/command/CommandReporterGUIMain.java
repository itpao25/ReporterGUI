package it.itpao25.NMSReport.command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.itpao25.NMSReport.notes.CommandNote;
import it.itpao25.NMSReport.util.Utili;
import it.itpao25.NMSReport.util.VersionServer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Gestione di tutti i comandi su /reportergui
 * personalizzabile dalla 1.6.9 
 * @author itpao25
 */
public class CommandReporterGUIMain extends AbstractCommand {
	
	public CommandReporterGUIMain(String command) {
		super(command);
	}

	@SuppressWarnings("unused")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) 
	{
		
		// Comando debug
		if ((args.length > 0 && args[0].equalsIgnoreCase("debug"))) 
		{
			CommandDebug CommandDebug = new CommandDebug(sender, args);
			
		// Comando view
		} else if ((args.length > 0 && args[0].equalsIgnoreCase("view"))) 
		{
			
			CommandView CommandView = new CommandView(sender, args);
			
		// Comando view-id
		} else if ((args.length > 0 && args[0].equalsIgnoreCase("view-id"))) 
		{
			
			CommandViewID CommandViewID = new CommandViewID(sender, args);
			
		// Comando approve
		} else if ((args.length > 0 && args[0].equalsIgnoreCase("solve"))) 
		{

			CommandSolve CommandApproved = new CommandSolve(sender, args);
			
		// Comando decline
		} else if ((args.length > 0 && args[0].equalsIgnoreCase("decline"))) 
		{
			
			CommandDecline CommandDeclined = new CommandDecline(sender, args);
								
		// Comando open
		} else if ((args.length > 0 && args[0].equalsIgnoreCase("open"))) 
		{
			
			CommandOpen CommandOpen = new CommandOpen(sender, args);
			
		// Comando duplicate
		} else if ((args.length > 0 && args[0].equalsIgnoreCase("duplicate"))) 
		{
			CommandDuplicate CommandDuplicate = new CommandDuplicate(sender, args);
			
		// Comando explired
		} else if ((args.length > 0 && args[0].equalsIgnoreCase("expire"))) 
		{
			CommandExpired CommandExpired = new CommandExpired(sender, args);
						
		// Comando list
		} else if ((args.length > 0 && args[0].equalsIgnoreCase("list"))) 
		{
			
			CommandList CommandList = new CommandList(sender, args);
		
		// Comando notify
		} else if ((args.length > 0 && args[0].equalsIgnoreCase("notify"))) 
		{
			
			CommandNotify CommandNotify = new CommandNotify(sender, args);
			
		// Comando clear
		} else if ((args.length > 0 && args[0].equalsIgnoreCase("clear"))) 
		{
			
			CommandClear CommandClear = new CommandClear(sender, args);
			
		} else if ((args.length > 0 && args[0].equalsIgnoreCase("reload"))) 
		{
			
			try {
				CommandReload CommandReload = new CommandReload(sender, args);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else if ((args.length > 0 && args[0].equalsIgnoreCase("stats"))) 
		{
			
			CommandStats CommandStats = new CommandStats(sender, args);
			
		} else if ((args.length > 0 && args[0].equalsIgnoreCase("tp"))) 
		{
			CommandTp CommandTp = new CommandTp(sender, args);
			
		} else if ((args.length > 0 && args[0].equalsIgnoreCase("search") 
				|| args.length > 0 && args[0].equalsIgnoreCase("s"))) 
		{
			CommandSearch CommandSearch = new CommandSearch(sender, args);
			
		} else if ((args.length > 0 && args[0].equalsIgnoreCase("note"))) 
		{
			try {
				
				CommandNote CommandNote = new CommandNote(sender, args);
			} catch (NumberFormatException | SQLException e) {
				e.printStackTrace();
			}
		// History manager
		} else if ((args.length > 0 && args[0].equalsIgnoreCase("history"))) 
		{
			CommandHistory CommandHistory = new CommandHistory(sender, args);
		
		// History manager
		} else if ((args.length > 0 && args[0].equalsIgnoreCase("bungeetpprivate"))) 
		{
			CommandBungeeTPPrivate CommandHistory = new CommandBungeeTPPrivate(sender, args);
			
		} else {
			// Nessun argomento
			Utili.invalidArgMessage(sender);
		}
		
		return false;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		return getPlayerNames(args[0]);
	}
	
	private List<String> getPlayerNames(String player) {
		List<String> list = new ArrayList<>();
		for (Player players : VersionServer.getOnlinePlayers()) {
			if (players.getName().contains(player)) {
				list.add(players.getName());
			}
		}
		return list;
	}
}
