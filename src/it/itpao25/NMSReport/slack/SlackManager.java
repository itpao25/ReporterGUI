package it.itpao25.NMSReport.slack;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import it.itpao25.NMSReport.Main;
import it.itpao25.NMSReport.config.ReporterGUIC;

public class SlackManager {
	public static boolean enable = false;
	/**
	 * Controllo se il sistema Slack è attivato dal config
	 * @return
	 */
	public SlackManager() {
		SlackManager.enable = isEnable();
	}
	public static boolean isEnable() {
		if( ReporterGUIC.getString("slack.enable") != null) {
			boolean value = ReporterGUIC.getBoolean("slack.enable");
			if(value == true) {
				return true;
			}
			return false;
		}
		return false;
	}
	/**
	 * Gestione dell'url (Outh0 key)
	 * @link {https://api.slack.com/incoming-webhooks}
	 * @return uri bot
	 */
	private static String getURI() {
		if( ReporterGUIC.getString("slack.uri") != null ) {
			String value = ReporterGUIC.getString("slack.uri");
			if (value == null || value.trim().isEmpty()) {
				return null;
			}
			return value;
		}
		return null;
	}
	
	/**
	 * Gestione del nome del bot (Slack)
	 * @link {https://api.slack.com/incoming-webhooks}
	 * @return name bot
	 */
	private static String getName() {
		if( ReporterGUIC.getString("slack.name-bot") != null ) {
			String value = ReporterGUIC.getString("slack.name-bot");
			if (value == null || value.trim().isEmpty()) {
				return null;
			}
			return value;
		}
		return null;
	}
	
	/**
	 * Gestione del canale (Slack)
	 * @link {https://api.slack.com/incoming-webhooks}
	 * @return name channel
	 */
	private static String getChannel() {
		if( ReporterGUIC.getString("slack.channel") != null ) {
			String value = ReporterGUIC.getString("slack.channel");
			if (value == null || value.trim().isEmpty()) {
				throw new NullPointerException("Slack: The channel name can't be null!");
			}
			if(!value.startsWith("#")) {
				throw new NullPointerException("Slack: The channel name must begin with \"#\"!");
			}
			return value;
		}
		return null;
	}
	
	/**
	 * Gestione dell'icona del canale
	 * @link {https://api.slack.com/incoming-webhooks}
	 * @return icon channel
	 */
	private static String getIcon() {
		if( ReporterGUIC.getString("slack.icon_emoji") != null ) {
			String value = ReporterGUIC.getString("slack.icon_emoji");
			if (value == null || value.trim().isEmpty()) {
				return null;
			}
			return value;
		}
		return null;
	}
	private static String getMessage(HashMap<String, String> data_report) {
		
		if (data_report.containsKey("to")) {
			if ( Main.getInstance().getConfig().getStringList("slack.message") != null ) {
				List<String> list = ReporterGUIC.get().getStringList("slack.message");
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
					if (data_report.containsKey("nameworldto")) {
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
			
			if ( Main.getInstance().getConfig().getStringList("slack.message-general") != null ) {
				List<String> list = ReporterGUIC.get().getStringList("slack.message-general");
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
		
		String message = getMessage(data_report);
		Main.getInstance().getLogger().info("Sending Slack message..");
		String stringa = ("payload={\"channel\": \""+ getChannel() +"\", \"username\": \""+ getName() +"\", \"text\": \""+ message +"\", \"icon_emoji\": \""+ getIcon() +"\"}");
		if( Main.debug ) {
			Main.getInstance().getLogger().info("Slack uri: "+ getURI()+ "?" + stringa);
		}
		try {
			//L'URL a cui fare la POST
			URL url = new URL( getURI() );
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			connection.setRequestProperty("charset", "UTF-8");
			connection.setRequestProperty("Content-Length", Integer.toString(stringa.getBytes().length));
			connection.setUseCaches(false);
			 
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(stringa);
			wr.flush();
			wr.close();
			 
			int responseCode = connection.getResponseCode();
			if(responseCode == 200) {
				Main.getInstance().getLogger().info("Successfully sent the message by Slack");
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return false;
	}
	
	
}
