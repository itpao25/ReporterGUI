package it.itpao25.NMSReport.task;

import java.text.SimpleDateFormat;
import java.util.Date;

import it.itpao25.NMSReport.Main;
import it.itpao25.NMSReport.config.ReporterGUIC;
import it.itpao25.NMSReport.storage.MysqlReport;
import it.itpao25.NMSReport.util.ConsoleMessage;

import org.bukkit.Bukkit;

public class ExpireReports {

	public ExpireReports() {

		if (ReporterGUIC.getString("task-expire-time.enable") != null) {
			if (ReporterGUIC.getBoolean("task-expire-time.enable") == false) {
				return;
			}
		} else {
			return;
		}
		
		int time = ReporterGUIC.getString("task-expire-time.delay") != null ? ReporterGUIC.getInt("task-expire-time.delay") : 5;
		new ConsoleMessage("&aAuto-expiration enabled. Check every " + time + " minutes");

		Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(), new Runnable() {
			@Override
			public void run() {
				if (MysqlReport.isMysqlEnable()) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					MysqlReport.TaskExpireReports(sdf.format(new Date()));
				}
			}

		}, 0, 20 * 60 * time);
	}
}
