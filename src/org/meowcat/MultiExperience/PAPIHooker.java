package org.meowcat.MultiExperience;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import me.clip.placeholderapi.external.EZPlaceholderHook;

public class PAPIHooker extends EZPlaceholderHook {
	private Main Main;

	public PAPIHooker(Plugin plugin) {
		super(plugin, "multiexp");
	}

	@Override
	public String onPlaceholderRequest(Player player, String string) {
		if (string.equals("time")) {
			if (Main.getConfig().getBoolean("multiexp.enabled")) {
				long CurrentTime = System.currentTimeMillis();
				long MultiTime = Main.getConfig().getLong("multiexp.tick-time");
				if (CurrentTime <= MultiTime) {
					long RemainingTime = (MultiTime - CurrentTime) / 60000;
					String Time = Main.segmentation(String.valueOf(Math.floor(RemainingTime)));
					return Time;
				}
				String message = Main.getConfig().getString("language.papi-expired");
				return message;
			} else {
				String message = Main.getConfig().getString("language.papi-expired");
				return message;
			}
		} else if (string.equals("multiple")) {
			String Multiple = Main.segmentation(Main.getConfig().getString("multiexp.multiple"));
			return Multiple;
		}
		return null;
	}
}
