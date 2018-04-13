package org.meowcat.MultiExperience;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import org.bukkit.Bukkit;
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
		getCommand("multiexperience").setExecutor(new Commands());
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
}
