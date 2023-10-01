package it.itpao25.NMSReport.reflection;


import it.itpao25.NMSReport.command.ValueCustom;
import it.itpao25.NMSReport.config.ReporterGUIM;
import it.itpao25.NMSReport.util.Utili;
import it.itpao25.NMSReport.util.VersionServer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;


public class ReflectionUtil {
	
	/**
	 * Invio un messaggio usando JSON e la libreria ProtocolLib
	 * Questa funzione è riservata all'invio del report 
	 * @see Segnalazione.java
	 * 
	 * @param json
	 * @param sender
	 */
	public static void sendMessageJSON(int id, String json, Player sender, boolean prefix) {
		
		if(prefix) {
			json = Utili.colorWithPrefix(sender, json);
		} else {
			json = Utili.color(json);
		}
		
		if(!VersionServer.is_compatible_json()) {
			sender.sendMessage(json);
			return;
		}
		
		String hover = ReporterGUIM.getString("message.hover-message-success");
		hover = hover.replace("[id]", id + "");
		
		json = JSONObject.escape(json);
		
		String messaggio = 
		"{" + 
	      "\"text\":\""+ json +"\"," + 
	      "\"clickEvent\": {" + 
	        "\"action\":\"run_command\", " + "" +
	        "\"value\": \"/"+ ValueCustom.getCommandMain() +" view-id "+ id +"\"" + 
	      "}," +
	      "\"hoverEvent\": {" + 
	        "\"action\":\"show_text\", " + "" +
	        "\"value\": \""+ Utili.color(hover) +"\"" + 
	      "}" +
	    "}";
		
		try {
			
			String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
			Object nmsPlayer = sender.getClass().getMethod("getHandle").invoke(sender);
			Object connection = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
			Class<?> chatSerializer = Class.forName("net.minecraft.server." + version + ".IChatBaseComponent$ChatSerializer");
			Class<?> chatComponent = Class.forName("net.minecraft.server." + version + ".IChatBaseComponent");
			Class<?> packet = Class.forName("net.minecraft.server." + version + ".PacketPlayOutChat");
			Constructor<?> constructor = packet.getConstructor(chatComponent);
			
			Object text = chatSerializer.getMethod("a", String.class).invoke(chatSerializer, messaggio);
			Object packetFinal = constructor.newInstance(text);
			
			Field field = packetFinal.getClass().getDeclaredField("a");
			field.setAccessible(true);
			field.set(packetFinal, text);
			connection.getClass().getMethod("sendPacket", Class.forName("net.minecraft.server." + version + ".Packet")).invoke(connection, packetFinal);
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	/**
	 * Invio un messaggio usando JSON e la libreria ProtocolLib
	 * Questa funzione è riservata all'invio del report 
	 * @see Segnalazione.java
	 * 
	 * @param json
	 * @param sender
	 */
	public static void sendBungeeMessageJSON(String json, Player sender, String server) {
		
		if(!VersionServer.is_compatible_json()) {
			sender.sendMessage(json);
			return;
		}
		
		json = JSONObject.escape(json);
		
		String hover = ReporterGUIM.getString("message.hover-message-success-bungeecord");
		hover = hover.replace("[server]", server);
		
		String messaggio = 
		"{" + 
	      "\"text\":\""+ json +"\"," + 
	      "\"clickEvent\": {" + 
	        "\"action\":\"run_command\", " + "" +
	        "\"value\": \"/"+ ValueCustom.getCommandMain() +" bungeetpprivate "+ server +"\"" + 
	      "}," +
	      "\"hoverEvent\": {" + 
	        "\"action\":\"show_text\", " + "" +
	        "\"value\": \""+ Utili.color(hover) +"\"" + 
	      "}" +
	    "}";
		
		try {
			
			String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
			Object nmsPlayer = sender.getClass().getMethod("getHandle").invoke(sender);
			Object connection = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
			Class<?> chatSerializer = Class.forName("net.minecraft.server." + version + ".IChatBaseComponent$ChatSerializer");
			Class<?> chatComponent = Class.forName("net.minecraft.server." + version + ".IChatBaseComponent");
			Class<?> packet = Class.forName("net.minecraft.server." + version + ".PacketPlayOutChat");
			Constructor<?> constructor = packet.getConstructor(chatComponent);
			
			Object text = chatSerializer.getMethod("a", String.class).invoke(chatSerializer, messaggio);
			Object packetFinal = constructor.newInstance(text);
			
			Field field = packetFinal.getClass().getDeclaredField("a");
			field.setAccessible(true);
			field.set(packetFinal, text);
			connection.getClass().getMethod("sendPacket", Class.forName("net.minecraft.server." + version + ".Packet")).invoke(connection, packetFinal);
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
	public static void sendMessageJSON(int id, String json, Player sender) {
		sendMessageJSON(id, json, sender, true);
	}
	
	/**
	 * Invio messagggio JSON per il join
	 * @param sender
	 * @param message
	 * @param target
	 */
	public static void sendMessageJSON_join(Player sender, String message, Player target) {
		
		message = Utili.colorWithPrefix(sender, message);
		
		if(!VersionServer.is_compatible_json()) {
			sender.sendMessage(message);
			return;
		}
		
		message = JSONObject.escape(message);
		
		String hover = ReporterGUIM.getString("message.message-on-join.message-hover").replace("[player]", target.getName());
		String messaggio = 
		"{" + 
	      "\"text\":\""+ message +"\"," + 
	      "\"clickEvent\": {" + 
	        "\"action\":\"run_command\", " + "" +
	        "\"value\": \"/"+ ValueCustom.getCommandMain() +" view "+ target.getName() +"\"" + 
	      "}," +
	      "\"hoverEvent\": {" + 
	        "\"action\":\"show_text\", " + "" +
	        "\"value\": \""+ Utili.color(hover) +"\"" + 
	      "}" +
	    "}";
		
		try {
			
			String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
			Object nmsPlayer = sender.getClass().getMethod("getHandle").invoke(sender);
			Object connection = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
			Class<?> chatSerializer = Class.forName("net.minecraft.server." + version + ".IChatBaseComponent$ChatSerializer");
			Class<?> chatComponent = Class.forName("net.minecraft.server." + version + ".IChatBaseComponent");
			Class<?> packet = Class.forName("net.minecraft.server." + version + ".PacketPlayOutChat");
			Constructor<?> constructor = packet.getConstructor(chatComponent);
			
			Object text = chatSerializer.getMethod("a", String.class).invoke(chatSerializer, messaggio);
			Object packetFinal = constructor.newInstance(text);
			
			Field field = packetFinal.getClass().getDeclaredField("a");
			field.setAccessible(true);
			field.set(packetFinal, text);
			connection.getClass().getMethod("sendPacket", Class.forName("net.minecraft.server." + version + ".Packet")).invoke(connection, packetFinal);
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
}
