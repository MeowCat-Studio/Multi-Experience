package org.meowcat.MultiExperience;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin implements Listener {
	public static Main Main;
	private String latestVer;

	@Override
	public void onEnable() {
		saveDefaultConfig();
		Main = this;
		getServer().getPluginManager().registerEvents(new EventListener(), this);
		if (getConfig().getBoolean("configuration.auto-broadcast-enabled")) {
			long interval = getConfig().getInt("configuration.broadcast-interval-time") * 60000;
			new BukkitRunnable() {
				public void run() {
					if (getConfig().getBoolean("configuration.multiexp-enabled")) {
						long CurrentTime = System.currentTimeMillis();
						long MultiTime = getConfig().getLong("multiexp.tick-time");
						if (CurrentTime <= MultiTime) {
							long RemainingTime = (MultiTime - CurrentTime) / 60000;
							double Multiple = Main.getConfig().getDouble("multiexp.multiple");
							getServer().broadcastMessage(getConfig()
									.getString("language." + getConfig().getString("language.use") + ".msg-holding-1")
									+ Multiple
									+ getConfig().getString(
											"language." + getConfig().getString("language.use") + ".msg-holding-2")
									+ Math.floor(RemainingTime) + getConfig().getString(
											"language." + getConfig().getString("language.use") + ".msg-holding-3"));
						} else {
							getConfig().set("configuration.multiexp-enabled", false);
						}
					}
				}
			}.runTaskTimer(this, interval, interval);
		}
		Plugin plugin = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
		if (plugin != null) {
			Bukkit.getConsoleSender().sendMessage(
					getConfig().getString("language." + getConfig().getString("language.use") + ".papi-hooking"));
			boolean success = new PAPIHooker(this).hook();
			if (success == true) {
				Bukkit.getConsoleSender().sendMessage(
						getConfig().getString("language." + getConfig().getString("language.use") + ".papi-success"));
			} else {
				Bukkit.getConsoleSender().sendMessage(
						getConfig().getString("language." + getConfig().getString("language.use") + ".papi-failed"));
			}
		}
		Bukkit.getConsoleSender().sendMessage("Multi Experience by MeowCat Studio");
		Bukkit.getConsoleSender().sendMessage("Offical Website http://www.meowcat.org/");
		Bukkit.getConsoleSender().sendMessage("[MultiExperience]"
				+ getConfig().getString("language." + getConfig().getString("language.use") + ".console-enable"));
		if (getConfig().getBoolean("configuration.auto-broadcast-enabled")) {
			updatecheck();
		} else {
			Bukkit.getConsoleSender().sendMessage(getConfig()
					.getString("language." + getConfig().getString("language.use") + ".update-check-disabled"));
			Bukkit.getConsoleSender().sendMessage("https://www.spigotmc.org/resources/multi-experience.53558/");
			Bukkit.getConsoleSender().sendMessage("https://dev.bukkit.org/projects/multi-experience/");
			Bukkit.getConsoleSender().sendMessage("https://github.com/MeowCat-Studio/Multi-Experience/");
			Bukkit.getConsoleSender().sendMessage("http://www.mcbbs.net/thread-784315-1-1.html");
		}
	}

	@Override
	public void onDisable() {
		saveConfig();
		Bukkit.getConsoleSender().sendMessage("Multi Experience by MeowCat Studio");
		Bukkit.getConsoleSender().sendMessage("Offical Website http://www.meowcat.org/");
		Bukkit.getConsoleSender().sendMessage("[MultiExperience]"
				+ getConfig().getString("language." + getConfig().getString("language.use") + ".console-disable"));
	}

	public String getLatestVersion() {
		String ver = null;
		try {
			URL url = new URL("http://www.meowcat.org/minecraft/multiexp/version.mct");
			InputStream is = url.openStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			ver = br.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
		latestVer = ver;
		return ver;
	}

	public boolean isLatestVersion() {
		boolean islatest = false;
		String latest = getLatestVersion();
		String current = getDescription().getVersion();
		if (latest.equalsIgnoreCase(current)) {
			islatest = true;
		}
		return islatest;
	}

	public void updatecheck() {
		new BukkitRunnable() {
			public void run() {
				if (isLatestVersion()) {
					Bukkit.getConsoleSender().sendMessage(getConfig()
							.getString("language." + getConfig().getString("language.use") + ".console-latest"));
				} else {
					Bukkit.getConsoleSender()
							.sendMessage(getConfig()
									.getString("language." + getConfig().getString("language.use") + ".console-newver")
									+ latestVer);
					Bukkit.getConsoleSender().sendMessage("https://www.spigotmc.org/resources/multi-experience.53558/");
					Bukkit.getConsoleSender().sendMessage("https://dev.bukkit.org/projects/multi-experience/");
					Bukkit.getConsoleSender().sendMessage("https://github.com/MeowCat-Studio/Multi-Experience/");
					Bukkit.getConsoleSender().sendMessage("http://www.mcbbs.net/thread-784315-1-1.html");
				}
			}
		}.runTaskAsynchronously(this);
	}

	public static boolean isNumber(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	@Override
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
						player.sendMessage(getConfig().getString(
								"language." + getConfig().getString("language.use") + ".syntaxerror-integer"));
					}
					if (arguments.length > 2) {
						if (isNumber(arguments[2])) {
							MultiTime = Long.valueOf(arguments[2]);
						} else {
							player.sendMessage(getConfig().getString(
									"language." + getConfig().getString("language.use") + ".syntaxerror-integer"));
						}
					}
				}
				long RemainingTime = MultiTime * 60000 + CurrentTime;
				getConfig().set("multiexp.multiple", Multiple);
				getConfig().set("multiexp.tick-time", RemainingTime);
				player.sendMessage(getConfig()
						.getString("language." + getConfig().getString("language.use") + ".msg-signal-1") + Multiple
						+ getConfig().getString("language." + getConfig().getString("language.use") + ".msg-signal-2")
						+ MultiTime
						+ getConfig().getString("language." + getConfig().getString("language.use") + ".msg-signal-3"));
				if (arguments.length > 3) {
					if (arguments[3].equalsIgnoreCase("true") || arguments[3].equalsIgnoreCase("t")) {
						getConfig().set("configuration.multiexp-enabled", true);
						getServer().broadcastMessage(getConfig()
								.getString("language." + getConfig().getString("language.use") + ".msg-broadcast-1")
								+ Multiple
								+ getConfig().getString(
										"language." + getConfig().getString("language.use") + ".msg-broadcast-2")
								+ MultiTime + getConfig().getString(
										"language." + getConfig().getString("language.use") + ".msg-broadcast-3"));
					}
				}
			} else if (arguments[0].equalsIgnoreCase("enable")) {
				long CurrentTime = System.currentTimeMillis();
				long MultiTime = getConfig().getLong("multiexp.tick-time");
				if (CurrentTime <= MultiTime) {
					long RemainingTime = (MultiTime - CurrentTime) / 60000;
					int Time = (int) Math.floor(RemainingTime);
					int Multiple = getConfig().getInt("multiexp.multiple");
					getConfig().set("configuration.multiexp-enabled", true);
					getServer().broadcastMessage(getConfig()
							.getString("language." + getConfig().getString("language.use") + ".msg-broadcast-1")
							+ Multiple
							+ getConfig()
									.getString("language." + getConfig().getString("language.use") + ".msg-broadcast-2")
							+ Time + getConfig().getString(
									"language." + getConfig().getString("language.use") + ".msg-broadcast-3"));
				} else {
					player.sendMessage(getConfig()
							.getString("language." + getConfig().getString("language.use") + ".msg-failure"));
				}
			} else if (arguments[0].equalsIgnoreCase("disable")) {
				getConfig().set("configuration.multiexp-enabled", false);
				getConfig().set("multiexp.tick-time", 0);
				getServer().broadcastMessage(
						getConfig().getString("language." + getConfig().getString("language.use") + ".msg-ended"));
			} else if (arguments[0].equalsIgnoreCase("reload")) {
				saveDefaultConfig();
				reloadConfig();
				player.sendMessage(
						getConfig().getString("language." + getConfig().getString("language.use") + ".reload"));
			}
		} else {
			player.sendMessage("¡ìbMulti Experience by MeowCat Studio");
			player.sendMessage("¡ìbhttp://www.meowcat.org/");
			player.sendMessage(getConfig().getString("language." + getConfig().getString("language.use") + ".usage"));
		}
		return true;
	}
}
