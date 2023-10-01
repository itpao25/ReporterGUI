package it.itpao25.NMSReport.telegram;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import it.itpao25.NMSReport.Main;
import it.itpao25.NMSReport.config.ReporterGUIC;

public class TelegramManager {
	
	public static boolean enable = false;
	private final static String endpoint = "https://api.telegram.org/";
	
	/**
	 * Controllo se il sistema Telegram è attivato dal config
	 * @return
	 */
	public TelegramManager() {
		TelegramManager.enable = isEnable();
	}
	public static boolean isEnable() {
		if( ReporterGUIC.getString("telegram.enable") != null) {
			boolean value = ReporterGUIC.getBoolean("telegram.enable");
			if(value == true) {
				return true;
			}
			return false;
		}
		return false;
	}
	
	/**
	 * Gestione dell'ID del bot di telegram
	 * @link {https://core.telegram.org/bots}
	 * @return id bot
	 */
	private static String getIdBot() {
		if( ReporterGUIC.getString("telegram.id-bot") != null ) {
			String value = ReporterGUIC.getString("telegram.id-bot");
			if (value == null || value.trim().isEmpty()) {
				throw new NullPointerException("Telegram: The bot id can't be null!");
			}
			return value;
		}
		return null;
	}
	
	/**
	 * Gestione dell'id del gruppo dove deve essere invato il messaggio 
	 * @return
	 */
	private static String getIdGroups() {
		if( ReporterGUIC.getString("telegram.chat-id") != null ) {
			String value = ReporterGUIC.getString("telegram.chat-id");
			if (value == null || value.trim().isEmpty()) {
				throw new NullPointerException("Telegram: The channel id can't be null!");
			}
			return value;
		}
		return null;
	}
	
	private static String getMessage(HashMap<String, String> data_report) {
		
		if (data_report.containsKey("to")) {
			if ( ReporterGUIC.get().getStringList("telegram.message") != null ) {
				List<String> list = ReporterGUIC.get().getStringList("telegram.message");
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
			
			if ( ReporterGUIC.get().getStringList("telegram.message-general") != null ) {
				List<String> list = ReporterGUIC.get().getStringList("telegram.message-general");
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
	
	public static boolean send(HashMap<String, String> data_report) {
		try {
			Main.getInstance().getLogger().info("Sending telegram message..");
			String stringa = URLEncoder.encode(getMessage(data_report), "UTF-8");
			String request = endpoint + "bot"+ getIdBot() + "/sendMessage?text="+ stringa +"&parse_mode=HTML&chat_id="+ getIdGroups();
			
			if( Main.debug ) {
				Main.getInstance().getLogger().info("Telegram uri: "+ request);
			}
			URL u = new URL(request);
			InputStream localInputStream = u.openConnection().getInputStream();
			
			// Leggo il contenuto per il debug
			BufferedReader reader = new BufferedReader( new InputStreamReader( localInputStream )  );
			String line = null;
			StringBuilder sb = new StringBuilder();
			while( ( line = reader.readLine() ) != null )  {
				sb.append(line);
			}
			reader.close();
			
			if( Main.debug ) {
				Main.getInstance().getLogger().info("Telegram data: "+ line);
			}
			
			JSONObject o = (JSONObject) JSONValue.parse(sb.toString());
			if( o.get("ok") != null ) {
				
				JSONObject result = (JSONObject) o.get("result");
				JSONObject chat = (JSONObject) result.get("chat");
				String jobject = chat.get("id").toString();
				Main.getInstance().getLogger().info("Telegram accept message to chat "+ jobject);
				
			} else {
				Main.getInstance().getLogger().info("Request failed!");
			}
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return false;
	}
}
