package it.itpao25.NMSReport.validator;

import org.bukkit.entity.Player;

import it.itpao25.NMSReport.util.Utili;

public class ValidatorVerTask {
	public ValidatorVerTask(Player p) {
		String vl = ValidatorVersion.check();
		if(vl.equals(ValidatorVersion.WAIT)) {
			p.sendMessage(Utili.colorWithPrefix( p ,"&cAn update is available from spigotmc!" ));
		}
	}
}
