package me.rowsell.antizombify;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PiglinAbstract;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class AntiZombify extends JavaPlugin implements Listener, CommandExecutor {

    FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        config.addDefault("Disabled Worlds", "[]");
        config.options().copyDefaults(true);
        saveConfig();

        this.getCommand("azreload").setExecutor(this);

        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("AntiZombify enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("AntiZombify disabled!");
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if(event.getEntityType() == EntityType.PIGLIN || event.getEntityType() == EntityType.PIGLIN_BRUTE) {
            boolean inDisabledWorld = false;
            for (String s : this.getConfig().getStringList("Disabled Worlds")) {
                if (event.getLocation().getWorld().getName().equals(s)) {
                    inDisabledWorld = true;
                    break;
                }
            }
            if (!inDisabledWorld) {
                PiglinAbstract piglin = (PiglinAbstract) event.getEntity();
                piglin.setImmuneToZombification(true);
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.reloadConfig();
        sender.sendMessage("AntiZombify config reloaded!");
        return true;
    }
}
