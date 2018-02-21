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
		getServer().getPluginManager().registerEvents(new ExpListener(), this);
		if (getConfig().getBoolean("configuration.auto-broadcast-enabled")) {
			long interval = getConfig().getInt("configuration.broadcast-interval-time") * 60000;
			new BukkitRunnable() {
				public void run() {
					if (!isEnabled()) {
						return;
					}
					if (getConfig().getBoolean("multiexp.enabled")) {
						long CurrentTime = System.currentTimeMillis();
						long MultiTime = getConfig().getLong("multiexp.tick-time");
						if (CurrentTime <= MultiTime) {
							long RemainingTime = (MultiTime - CurrentTime) / 60000;
							double Multiple = Main.getConfig().getDouble("multiexp.multiple");
							getServer().broadcastMessage(getConfig().getString("language.msg-holding-1") + Multiple + getConfig().getString("language.msg-holding-2") + Math.floor(RemainingTime) + getConfig().getString("language.msg-holding-3"));
						} else {
							getConfig().set("multiexp.enabled", false);
						}
					}
				}
			}.runTaskTimer(this, interval, interval);
		}
		Plugin plugin = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
		if (plugin != null) {
			System.out.println("Hooking PlaceholderAPI");
			boolean success = new PAPIHooker(this).hook();
			if (success == true) {
				System.out.println("Hook PlaceholderAPI Successfully!");
			} else {
				System.out.println("Hook PlaceholderAPI Failed!");
			}
		}
		Bukkit.getConsoleSender().sendMessage("Multi Experience by MeowCat Studio");
		Bukkit.getConsoleSender().sendMessage("Offical Website http://www.meowcat.org/");
		Bukkit.getConsoleSender().sendMessage("[MultiExperience]Plugin Enabled.");
		updatecheck();
	}

	@Override
	public void onDisable() {
		saveConfig();
		Bukkit.getConsoleSender().sendMessage("Multi Experience by MeowCat Studio");
		Bukkit.getConsoleSender().sendMessage("Offical Website http://www.meowcat.org/");
		Bukkit.getConsoleSender().sendMessage("[MultiExperience]Plugin Disabled.");
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
					Bukkit.getConsoleSender().sendMessage("Good,you use the latest version.");
				} else {
					Bukkit.getConsoleSender().sendMessage("Please use the newer version " + latestVer);
					Bukkit.getConsoleSender().sendMessage("https://www.spigotmc.org/resources/multi-experience.53558/");
					Bukkit.getConsoleSender().sendMessage("https://dev.bukkit.org/projects/multi-experience/");
					Bukkit.getConsoleSender().sendMessage("https://github.com/MeowCat-Studio/Multi-Experience/");
				}
			}
		}.runTaskAsynchronously(this);
	}

	public String segmentation(String arguments) {
		return arguments.substring(0, arguments.lastIndexOf("."));
	}

	@Override
	public boolean onCommand(CommandSender player, Command command, String label, String[] arguments) {
		if (arguments.length > 0) {
			if (arguments[0].equalsIgnoreCase("enable")) {
				float Multiple = 2;
				long MultiTime = 60;
				long CurrentTime = System.currentTimeMillis();
				if (arguments.length > 1) {
					Multiple = Float.valueOf(arguments[1]);
					if (arguments.length > 2) {
						MultiTime = Long.valueOf(arguments[2]);
					}
				}
				long RemainingTime = MultiTime * 60000 + CurrentTime;
				getConfig().set("multiexp.enabled", true);
				getConfig().set("multiexp.multiple", Multiple);
				getConfig().set("multiexp.tick-time", RemainingTime);
				player.sendMessage(getConfig().getString("language.msg-signal-1") + Multiple + getConfig().getString("language.msg-signal-2") + MultiTime + getConfig().getString("language.msg-signal-3"));
				getServer().broadcastMessage(getConfig().getString("language.msg-broadcast-1") + Multiple + getConfig().getString("language.msg-broadcast-2") + MultiTime + getConfig().getString("language.msg-broadcast-3"));
			} else if (arguments[0].equalsIgnoreCase("disable")) {
				getConfig().set("multiexp.enabled", false);
				getConfig().set("multiexp.tick-time", 0);
				getServer().broadcastMessage(getConfig().getString("language.msg-ended"));
			} else if (arguments[0].equalsIgnoreCase("reload")) {
				saveDefaultConfig();
				reloadConfig();
				player.sendMessage(getConfig().getString("language.reload"));
			}
		} else {
			player.sendMessage("¡ìbMulti Experience by MeowCat Studio");
			player.sendMessage("¡ìbhttp://www.meowcat.org/");
			player.sendMessage(getConfig().getString("language.usage"));
		}
		return true;
	}
}
