package org.meowcat.MultiExperience;

import org.bukkit.configuration.file.FileConfiguration;

public class ExpThread extends Thread {

    FileConfiguration getConfig = Main.plugin.getConfig();
    
	@Override
	public void run()
	{ 
		while (Main.plugin.isEnabled()) {
			try {
				sleep(getConfig.getInt("configuration.broadcast-interval-time") * 60000);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (!Main.plugin.isEnabled()) {
				return;
			}
			if (getConfig.getBoolean("multiexp.enabled")) {
				long CurrentTime = System.currentTimeMillis();
				long MultiTime = getConfig.getLong("multiexp.tick-time");
				if (CurrentTime <= MultiTime) {
					long RemainingTime = (MultiTime - CurrentTime) / 60000;
					double Multiple = getConfig.getDouble("multiexp.multiple");
					String broadcast = getConfig.getString("language.msg-holding-1") + Multiple + getConfig.getString("language.msg-holding-2") + Math.floor(RemainingTime) + getConfig.getString("language.msg-holding-3");
					Main.plugin.getServer().broadcastMessage(broadcast);
				}
				else {
					getConfig.set("multiexp.enabled", false);
				}
			}
		}
	}
}
