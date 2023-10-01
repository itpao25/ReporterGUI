package it.itpao25.NMSReport.event;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NoteCreateEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;
	
	private int report_id;
	private int id_nota;
	private String text;
	private CommandSender sender;
	
	public NoteCreateEvent(CommandSender sender, int report_id, int id_nota, String text) {
		this.report_id = report_id;
		this.id_nota = id_nota;
		this.text = text;
		this.sender = sender;
	}
	public int getIdReport() {
		return this.report_id;
	}
	public int getIdNota() {
		return this.id_nota;
	}
	public CommandSender getSender() {
		return this.sender;
	}
	public String getTextNote() {
		return this.text;
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
}
