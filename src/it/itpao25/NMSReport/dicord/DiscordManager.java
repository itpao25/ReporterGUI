package it.itpao25.NMSReport.dicord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import it.itpao25.NMSReport.Main;
import it.itpao25.NMSReport.config.ReporterGUIC;
import it.itpao25.NMSReport.validator.ValidatorVersion;

public final class DiscordManager {
	
	public static boolean enable = false;
	
	private final static String endpoint = "https://discordapp.com/api";
	public DiscordWS ws;
	
	public DiscordManager() {
		DiscordManager.enable = isEnable();
	}
	
	/**
	 * Controllo se il sistema Discord è attivato dal config
	 * @return
	 */
	public boolean isEnable() {
		if( ReporterGUIC.getString("discord.enable") != null) {
			boolean value = ReporterGUIC.getBoolean("discord.enable");
			if(value == true) {
				return true;
			}
			return false;
		}
		return false;
	}
	
	/**
	 * Gestione dell'id del gruppo dove deve essere invato il messaggio 
	 * @return
	 */
	private static String getIdGroups() {
		if( ReporterGUIC.getString("discord.channel") != null ) {
			String value = ReporterGUIC.getString("discord.channel");
			if (value == null || value.trim().isEmpty()) {
				return null;
			}
			return value;
		}
		return null;
	}
	
	/**
	 * Ritorno con il token del bot impostato nel config
	 * @return
	 */
	private static String getToken() {
		if(ReporterGUIC.getString("discord.token_bot") != null) {
			String value = ReporterGUIC.getString("discord.token_bot");
			if(value == null || value.trim().isEmpty() ) {
				return null;
			}
			return value;
		}
		return null;
	}
	
	private static String getMessage(HashMap<String, String> data_report) {
		
		if (data_report.containsKey("to")) {
			if ( ReporterGUIC.get().getStringList("discord.message") != null ) {
				List<String> list = ReporterGUIC.get().getStringList("discord.message");
				String output = "";
				for (String s : list)
				{
					if (data_report.containsKey("from")) {
						s = s.replace("[from]", data_report.get("from"));
					}
					if (data_report.containsKey("to")) {
						s = s.replace("[to]", data_report.get("to"));
					}
					if (data_report.containsKey("reason")) {
						s = s.replace("[reason]", data_report.get("reason"));
					}
					if (data_report.containsKey("nameworldfrom")) {
						s = s.replace("[worldfrom]", data_report.get("nameworldfrom"));
					}
					if (data_report.containsKey("nameworldto") && data_report.get("nameworldto") != null) {
						s = s.replace("[worldto]", data_report.get("nameworldto"));
					}
					if (data_report.containsKey("server")) {
						s = s.replace("[server]", data_report.get("server"));
					}
				    output += s + "\n";
				}
				return output.toString();
			}
		} else {
			
			if ( ReporterGUIC.get().getStringList("discord.message-general") != null ) {
				List<String> list = ReporterGUIC.get().getStringList("discord.message-general");
				String output = "";
				for (String s : list)
				{
					if (data_report.containsKey("from")) {
						s = s.replace("[from]", data_report.get("from"));
					}
					if (data_report.containsKey("reason")) {
						s = s.replace("[reason]", data_report.get("reason"));
					}
					if (data_report.containsKey("nameworldfrom")) {
						s = s.replace("[worldfrom]", data_report.get("nameworldfrom"));
					}
					if (data_report.containsKey("server")) {
						s = s.replace("[server]", data_report.get("server"));
					}
				    output += s + "\n";
				}
				return output.toString();
			}
			
		}
		return null;
	}
	
	public boolean send(HashMap<String, String> data_report) {
		
		Main.getInstance().getLogger().info("Sending discord message..");		
		ws = openWS();

		try {
			
			if(getIdGroups() == null) {
				throw new NullPointerException("Discord: id group can not be null");
			}
			if(getToken() == null) {
				throw new NullPointerException("Discord: token can not be null");
			}
			
			// URL Per l'invio del messaggio
			URL obj = new URL(endpoint + "/channels/"+ getIdGroups() +"/messages");
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			
			String message = getMessage(data_report);
			
			// Imposto i parametri per la POST
			con.setRequestMethod("POST");
			con.setRequestProperty("Authorization", "Bot "+ getToken());
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("User-Agent", "DiscordBot (https://github.com/itpao25/ReporterGUIWeb, "+ ValidatorVersion.ver() +")");
			
			String urlParameters = "{\"content\":\""+ JSONObject.escape(message) + "\"}";
			byte[] outputInBytes = urlParameters.getBytes("UTF-8");
			// Send post request
			con.setDoOutput(true);
			OutputStream wr = con.getOutputStream();
			wr.write(outputInBytes);
			wr.flush();
			wr.close();
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			// print result
			JSONObject o = (JSONObject) JSONValue.parse(response.toString());
			if( o.get("id") != null ) {
				String channel_id = o.get("channel_id").toString();
				String message_id = o.get("id").toString();
				Main.getInstance().getLogger().info("Discord accept message (#"+ message_id +") to chat "+ channel_id);
			}
			ws.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	private DiscordWS openWS() {
		try {
			return new DiscordWS( new URI("wss://gateway.discord.gg/") );
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	class DiscordWS extends WebSocketClient {
		public DiscordWS(URI serverURI) {
			super(serverURI);
			try {
				setSocket(SSLSocketFactory.getDefault().createSocket(serverURI.getHost(), 443));
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.connect();
		}

		@Override
		public void onOpen(ServerHandshake handshakedata) {
			// TODO Auto-generated method stub
			if(getToken() == null) {
				throw new NullPointerException("Discord: token can not be null");
			}
			send("{\"op\":2,\"d\":{\"token\":\"" + getToken() + "\",\"properties\":{\"$os\":\"Linux\",\"$browser\":\"ReporterGUI\",\"$device\":\"ReporterGUI\",\"$referrer\":\"\",\"$referring_domain\":\"\"},\"v\":2}}");
		}

		@Override
		public void onMessage(String message) {
		}

		@Override
		public void onClose(int code, String reason, boolean remote) {
		}

		@Override
		public void onError(Exception ex) {
		}
		
	}
}
