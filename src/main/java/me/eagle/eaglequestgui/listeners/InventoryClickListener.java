package me.eagle.eaglequestgui.listeners;

import me.eagle.eaglequestgui.EagleQuestGUI;
import me.pikamug.quests.Quests;
import me.pikamug.quests.player.BukkitQuester;
import me.pikamug.quests.quests.BukkitQuest;
import me.pikamug.quests.quests.Quest;
import me.eagle.eaglequestgui.DisplayGUI;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

import java.util.Collection;


/**
 * Unused Listener, but functionality is there.
*/

public class InventoryClickListener implements Listener {

    final EagleQuestGUI plugin;

    public InventoryClickListener(EagleQuestGUI plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Check if the clicked inventory is the custom GUI
        Inventory inventory = event.getInventory();
        if (inventory.getHolder() instanceof DisplayGUI) {
            event.setCancelled(true); // Prevent moving items
            System.out.println("Clicked a Quest GUI");
            // Get the clicked item
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem == null || clickedItem.getType() == Material.AIR) {
                return; // No item clicked
            }

            // Get item meta and display name
            ItemMeta meta = clickedItem.getItemMeta();
            if (meta != null && meta.hasDisplayName()) {
                String questName = ChatColor.stripColor(meta.getDisplayName()); // Extract the quest name

                // Get the BukkitQuester and quests
                Quests qp = plugin.getQuests();
                BukkitQuester quester = (BukkitQuester) qp.getQuester(event.getWhoClicked().getUniqueId());
                Collection<Quest> questList = qp.getLoadedQuests(); // Get all loaded quests

                // Find the quest by name
                BukkitQuest quest = null;
                for (Quest q : questList) {
                    if (q instanceof BukkitQuest) {
                        BukkitQuest bukkitQuest = (BukkitQuest) q;
                        if (ChatColor.stripColor(bukkitQuest.prepareDisplay(quester).getItemMeta().getDisplayName()).equals(questName)) {
                            quest = bukkitQuest;
                            break;
                        }
                    }
                }

                // Start the quest if found
                if (quest != null) {
                    quester.takeQuest(quest, false);
                    event.getWhoClicked().sendMessage(ChatColor.GREEN + "Quest " + questName + " started!");
                } else {
                    event.getWhoClicked().sendMessage(ChatColor.RED + "Quest not found!");
                }
            }
        }
    }
}
