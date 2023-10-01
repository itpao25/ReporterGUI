package it.itpao25.NMSReport.event;

import it.itpao25.NMSReport.storage.MysqlStatus.ReportStatus;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Evento che mi permette di
 * @author itpao25
 *
 */
public class ChangedStatusReport extends Event implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private int id_report;
    private CommandSender excutor;
    private ReportStatus status;
    
    public ChangedStatusReport(ReportStatus status, int report, CommandSender excutor) {
    	this.status = status;
    	this.excutor = excutor;
    	this.id_report = report;
    }
    @Override
    public boolean isCancelled() {
        return cancelled;
    }
    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    /**
     * Ritorno con l'id della segnalazione
     * @return int
     */
    public int getReportID() {
    	return id_report;
    }
    
    /**
     * Ritorno con il CommandSender che sta provando ad approvare
     * la segnalazione
     */
    public CommandSender getExcutor() {
		return excutor;
    }
    
    public ReportStatus getStatus() {
    	return status;
    }
}
