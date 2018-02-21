package org.meowcat.MultiExperience;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.meowcat.MultiExperience.Main;

public class ExpThread extends Thread {

	Server getServer = Bukkit.getServer();
    FileConfiguration getConfig = Main.Main.getConfig();
	boolean isEnabled = Main.Main.isEnabled();
    
	@Override
	public void run()
	{ 
		while (isEnabled) {
			try {
				sleep(getConfig.getInt("configuration.broadcast-interval-time") * 60000);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (!isEnabled) {
				return;
			}
			if (getConfig.getBoolean("multiexp.enabled")) {
				long CurrentTime = System.currentTimeMillis();
				long MultiTime = getConfig.getLong("multiexp.tick-time");
				if (CurrentTime <= MultiTime) {
					long RemainingTime = (MultiTime - CurrentTime) / 60000;
					double Multiple = getConfig.getDouble("multiexp.multiple");
					getServer.broadcastMessage(getConfig.getString("language.msg-holding-1") + Multiple + getConfig.getString("language.msg-holding-2") + Math.floor(RemainingTime) + getConfig.getString("language.msg-holding-3"));
				}
				else {
					getConfig.set("multiexp.enabled", false);
				}
			}
		}
	}
}
