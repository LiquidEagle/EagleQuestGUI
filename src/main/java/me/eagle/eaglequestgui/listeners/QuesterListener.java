package me.eagle.eaglequestgui.listeners;

import me.eagle.eaglequestgui.DisplayGUI;
import me.eagle.eaglequestgui.EagleQuestGUI;
import me.pikamug.quests.events.quester.BukkitQuesterPreOpenGUIEvent;
import me.pikamug.quests.player.Quester;
import org.browsit.milkgui.item.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;

/**
 * Get Quest Pre Open GUI Event to open our custom GUI
 */
public class QuesterListener implements Listener {
    final EagleQuestGUI plugin;

    public QuesterListener(EagleQuestGUI paramQuestsGUI) {
        this.plugin = paramQuestsGUI;
    }

    @EventHandler
    public void onQuesterPreOpenGUIEvent(BukkitQuesterPreOpenGUIEvent QuesterPreOpenGUIEvent) {

            QuesterPreOpenGUIEvent.setCancelled(true);
            DisplayGUI displayGUI = new DisplayGUI(this.plugin, QuesterPreOpenGUIEvent.getQuester(), QuesterPreOpenGUIEvent.getNPC(), QuesterPreOpenGUIEvent.getQuests());
            displayGUI.openInventory(QuesterPreOpenGUIEvent.getQuester().getPlayer());


    }
}
