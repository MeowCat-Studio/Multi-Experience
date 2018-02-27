package org.meowcat.MultiExperience;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class Commands implements CommandExecutor {
	Server getServer = Bukkit.getServer();
	FileConfiguration getConfig = Main.Main.getConfig();
	public static boolean isNumber(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public boolean onCommand(CommandSender player, Command command, String label, String[] arguments) {
		if (arguments.length > 0) {
			if (arguments[0].equalsIgnoreCase("set")) {
				int Multiple = 2;
				long MultiTime = 60;
				long CurrentTime = System.currentTimeMillis();
				if (arguments.length > 1) {
					if (isNumber(arguments[1])) {
						Multiple = Integer.valueOf(arguments[1]);
					} else {
						player.sendMessage(getConfig.getString(
								"language." + getConfig.getString("language.use") + ".syntaxerror-integer"));
					}
					if (arguments.length > 2) {
						if (isNumber(arguments[2])) {
							MultiTime = Long.valueOf(arguments[2]);
						} else {
							player.sendMessage(getConfig.getString(
									"language." + getConfig.getString("language.use") + ".syntaxerror-integer"));
						}
					}
				}
				long RemainingTime = MultiTime * 60000 + CurrentTime;
				getConfig.set("multiexp.multiple", Multiple);
				getConfig.set("multiexp.tick-time", RemainingTime);
				player.sendMessage(getConfig
						.getString("language." + getConfig.getString("language.use") + ".msg-signal-1") + Multiple
						+ getConfig.getString("language." + getConfig.getString("language.use") + ".msg-signal-2")
						+ MultiTime
						+ getConfig.getString("language." + getConfig.getString("language.use") + ".msg-signal-3"));
				if (arguments.length > 3) {
					if (arguments[3].equalsIgnoreCase("true") || arguments[3].equalsIgnoreCase("t")) {
						getConfig.set("configuration.multiexp-enabled", true);
						getServer.broadcastMessage(getConfig
								.getString("language." + getConfig.getString("language.use") + ".msg-broadcast-1")
								+ Multiple
								+ getConfig.getString(
										"language." + getConfig.getString("language.use") + ".msg-broadcast-2")
								+ MultiTime + getConfig.getString(
										"language." + getConfig.getString("language.use") + ".msg-broadcast-3"));
					}
				}
			} else if (arguments[0].equalsIgnoreCase("enable")) {
				long CurrentTime = System.currentTimeMillis();
				long MultiTime = getConfig.getLong("multiexp.tick-time");
				if (CurrentTime <= MultiTime) {
					long RemainingTime = (MultiTime - CurrentTime) / 60000;
					int Time = (int) Math.floor(RemainingTime);
					int Multiple = getConfig.getInt("multiexp.multiple");
					getConfig.set("configuration.multiexp-enabled", true);
					getServer.broadcastMessage(getConfig
							.getString("language." + getConfig.getString("language.use") + ".msg-broadcast-1")
							+ Multiple
							+ getConfig
									.getString("language." + getConfig.getString("language.use") + ".msg-broadcast-2")
							+ Time + getConfig.getString(
									"language." + getConfig.getString("language.use") + ".msg-broadcast-3"));
				} else {
					player.sendMessage(getConfig
							.getString("language." + getConfig.getString("language.use") + ".msg-failure"));
				}
			} else if (arguments[0].equalsIgnoreCase("disable")) {
				getConfig.set("configuration.multiexp-enabled", false);
				getConfig.set("multiexp.tick-time", 0);
				getServer.broadcastMessage(
						getConfig.getString("language." + getConfig.getString("language.use") + ".msg-ended"));
			} else if (arguments[0].equalsIgnoreCase("reload")) {
				Main.Main.saveDefaultConfig();
				Main.Main.reloadConfig();
				player.sendMessage(
						getConfig.getString("language." + getConfig.getString("language.use") + ".reload"));
			}
		} else {
			player.sendMessage("¡ìbMulti Experience by MeowCat Studio");
			player.sendMessage("¡ìbhttp://www.meowcat.org/");
			player.sendMessage(getConfig.getString("language." + getConfig.getString("language.use") + ".usage"));
		}
		return true;
	}
}
