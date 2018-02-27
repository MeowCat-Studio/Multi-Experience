package org.meowcat.MultiExperience;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;

public class EventListener implements Listener {
	Server getServer = Bukkit.getServer();
	FileConfiguration getConfig = Main.Main.getConfig();
	boolean isEnabled = Main.Main.isEnabled();

	@EventHandler
	public void ExperienceListener(PlayerExpChangeEvent event) {
		if (getConfig.getBoolean("configuration.multiexp-enabled")) {
			long CurrentTime = System.currentTimeMillis();
			long MultiTime = getConfig.getLong("multiexp.tick-time");
			if (CurrentTime <= MultiTime) {
				int Multiple = getConfig.getInt("multiexp.multiple");
				int PickedExp = event.getAmount();
				int CalculatedExp = PickedExp * Multiple;
				event.setAmount(Math.round(CalculatedExp));
				event.getPlayer().sendMessage(getConfig.getString("language.msg-getexp-1") + Multiple
						+ getConfig.getString("language." + getConfig.getString("language.use") + ".msg-getexp-2")
						+ CalculatedExp
						+ getConfig.getString("language." + getConfig.getString("language.use") + ".msg-getexp-3"));
			} else {
				getConfig.set("multiexp.enabled", false);
				getServer.broadcastMessage(getConfig.getString("language.msg-expired"));
			}
		}
	}

	@EventHandler
	public void ExperienceDropped(PlayerDeathEvent event) {
		if (getConfig.getBoolean("configuration.death-protect")) {
			int multiple = getConfig.getInt("multiexp.multiple");
			if (multiple > 0) {
				int DeathDroppedExp = event.getDroppedExp();
				event.setDroppedExp((int) (DeathDroppedExp / multiple));
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void VeryImportantPerson(PlayerExpChangeEvent event) {
		if (getConfig.getBoolean("configuration.vip-enabled")) {
			if (event.getPlayer().hasPermission("multiexp.vip")) {
				int PickedExp = event.getAmount();
				int Multiple = getConfig.getInt("multiexp.vip");
				int CalculatedExp = (int) (PickedExp * Multiple);
				event.setAmount(CalculatedExp);
			}
		}
	}
}
