package me.eagle.eaglequestgui;

import me.eagle.eaglequestgui.listeners.QuesterListener;
import me.pikamug.quests.Quests;
import net.citizensnpcs.Citizens;
import org.browsit.milkgui.MilkGUI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class EagleQuestGUI extends JavaPlugin {

    private Quests quests;
    private Citizens citizens;

    @Override
    public void onEnable() {
        getLogger().info("EagleQuestGUI enabled");
        getLogger().info("This plugin is NOT for public use. Only for use by me (Liquid_Eagle). Any unknown use is recorded.");

        // Hook into the Quests plugin
        if (Bukkit.getServer().getPluginManager().getPlugin("Quests")== null) {
            getLogger().severe("Quests plugin not found! Disabling...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        this.quests = (Quests) Bukkit.getServer().getPluginManager().getPlugin("Quests");
        getLogger().info("Successfully hooked into Quests!");

        // Hook into the Citizens plugin
        if (getServer().getPluginManager().getPlugin("Citizens") != null) {
            this.citizens = (Citizens) Bukkit.getServer().getPluginManager().getPlugin("Citizens");
            getLogger().info("Successfully hooked into Citizens!");
        } else {
            getLogger().severe("Citizens plugin not found! Disabling...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

            // Register the event listener
            MilkGUI.INSTANCE.register(this);
            if (this.quests.getDependencies().isPluginAvailable("Citizens"))
                getServer().getPluginManager().registerEvents(new QuesterListener(this), this);
                //getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);
        }



    public Quests getQuests() {
        return this.quests;
    }
    public Citizens getCitizens() {
        return this.citizens;
    }
}


