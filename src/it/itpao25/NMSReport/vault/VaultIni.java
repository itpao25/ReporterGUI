package it.itpao25.NMSReport.vault;

import it.itpao25.NMSReport.Main;
import it.itpao25.NMSReport.config.ReporterGUIC;
import it.itpao25.NMSReport.util.Utili;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultIni {
	
	public boolean enable = false;
	
	/**
	 * Controllo se è abilitato Vault
	 */
	public VaultIni() {
		Plugin plugin = Bukkit.getPluginManager().getPlugin("Vault");
		if(plugin == null || plugin.isEnabled() == false) {
			Bukkit.getConsoleSender().sendMessage(Utili.color("&b[&3ReporterGUI&b] &cVault hook is not enable"));
			return;
		} else {
			if(!ReporterGUIC.getBoolean("vault.enable")) {
				Bukkit.getConsoleSender().sendMessage(Utili.color("&b[&3ReporterGUI&b] &cVault hook is not enable (by config.yml)"));
				return;
			}
			enable = true;
			Bukkit.getConsoleSender().sendMessage(Utili.color("&b[&3ReporterGUI&b] &aVault enabled"));
			setupPermissions();
		}
	}
	
	static net.milkbowl.vault.permission.Permission perms = null;
	
	public boolean setupPermissions() {
		RegisteredServiceProvider<net.milkbowl.vault.permission.Permission> rsp = Main.getInstance().getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
		perms = rsp.getProvider();
		return perms != null;
    }
	
	/**
	 * Ritorno con il nome del gruppo primario per l'utente
	 * @param world
	 * @param player
	 * @return
	 */
	public static String getPrimaryGroup(String world, OfflinePlayer player) {
		return VaultIni.perms.getPrimaryGroup(world, player);
	}
}
