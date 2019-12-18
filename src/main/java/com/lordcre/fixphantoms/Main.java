package com.lordcre.fixphantoms;

import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public final class Main extends JavaPlugin implements Listener {
    private final Logger logger = Logger.getLogger(this.getName());
    private FileConfiguration config;
    Set<Player> phantomDisabled = ConcurrentHashMap.newKeySet();


    @Override
    public void onEnable() {
        initConfig();
        getServer().getPluginManager().registerEvents(this, this);
        new StatResetTask(this).runTaskTimerAsynchronously(this, 0L, 1200L);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length == 0) {
            return false;
        }

        String permissionMessage = cmd.getPermissionMessage();
        assert (permissionMessage != null);

        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("fp.reload")) {
                sender.sendMessage(permissionMessage);
                return true;
            }
            this.reloadConfig();
            config = this.getConfig();

            logger.info("[FixPhantoms] Configuration reloaded.");
            if (sender instanceof Player) {
                sender.sendMessage("[FixPhantoms] Configuration reloaded.");
            }
            return true;
        }
        return false;
    }
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
       
        if ((p.getLocation().getBlock().getBiome() == Biome.MUSHROOM_FIELDS) || (p.getLocation().getBlock().getBiome() == Biome.MUSHROOM_FIELD_SHORE)) {
        	if (!(phantomDisabled.contains(p))) {
        	phantomDisabled.add(p);
        	p.setStatistic(Statistic.TIME_SINCE_REST, 0);
        	}
        }
        else {
        	if ((phantomDisabled.contains(p))) {
            	phantomDisabled.remove(p);
        	}
        }
    }

    boolean isWorldEnabled(World world) {
        if (world == null) {
            return false;
        }
        return config.getList("enabledWorlds").contains(world.getName());
    }
    

    private void initConfig() {
        config = this.getConfig();

        MemoryConfiguration defaultConfig = new MemoryConfiguration();

        ArrayList<String> worldNames = new ArrayList<>();
        worldNames.add("world");

        defaultConfig.set("enabledWorlds", worldNames);

        config.setDefaults(defaultConfig);
        config.options().copyDefaults(true);
        saveConfig();
    }
}
