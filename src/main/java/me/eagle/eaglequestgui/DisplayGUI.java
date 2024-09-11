package me.eagle.eaglequestgui;

import me.eagle.eaglequestgui.utils.ItemNames;
import me.pikamug.quests.dependencies.BukkitDependencies;
import me.pikamug.quests.player.BukkitQuester;
import me.pikamug.quests.player.Quester;
import me.pikamug.quests.quests.BukkitQuest;
import me.pikamug.quests.quests.Quest;
import me.pikamug.quests.quests.components.Planner;
import me.pikamug.quests.util.BukkitLang;
import me.pikamug.quests.util.BukkitMiscUtil;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.browsit.milkgui.gui.GUI;
import org.browsit.milkgui.gui.type.PaginatedGUI;
import org.browsit.milkgui.item.Item;
import org.browsit.milkgui.item.ItemSection;
import org.browsit.milkgui.util.Rows;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;


public class DisplayGUI extends PaginatedGUI {

    public DisplayGUI(EagleQuestGUI paramQuestsGUI, Quester paramQuester, UUID npc, LinkedList<BukkitQuest> npcQuests) {
        super(new GUI(paramQuester.getPlayer(), ((BukkitDependencies)paramQuestsGUI.getQuests().getDependencies()).getNpcName(npc) + "'s Quests |", Rows.TWO));
//        for (byte b1 = 0; b1 < getGui().getRows().getSlots(); b1++) {
//            if (getGui().getItemFor(b1) == null)
//                setItem(b1, new Item("AIR"));
//        }
        getGuiSettings().setFlankedArrows(true);
        ConcurrentSkipListSet<ItemSection> concurrentSkipListSet = new ConcurrentSkipListSet<>();
        int b2 = 0;



        for (BukkitQuest bukkitQuest : npcQuests) { // just doing quests (so 2)
            if (bukkitQuest.getGUIDisplay() != null) {
                ItemStack display = bukkitQuest.getGUIDisplay().clone();
                ItemMeta meta = display.getItemMeta();

                String rewards = String.valueOf(bukkitQuest.getRewards().getMoney());

//                Iterator var8 =  paramQuester.getCurrentObjectives(bukkitQuest, false,false).iterator();
//
//                String message = "";
//                while(var8.hasNext()) {
//                    Objective obj = (Objective)var8.next();
//                    BukkitObjective objective = (BukkitObjective)obj;
//                    message = BukkitLang.BukkitFormatToken.convertString(paramQuester.getPlayer(), objective.getMessage());
//                    }


                LinkedList<String> lines = BukkitMiscUtil.makeLines(" ", " ", 40, ChatColor.DARK_GREEN);


                // ~ Initialise NPC Registry
                NPCRegistry npcRegistry = paramQuestsGUI.getCitizens().getNPCRegistry();

                // ~ Iterate through the Quest Stages
                for (int i = 0; i < bukkitQuest.getStages().size(); i++) {
                    lines.add(ChatColor.DARK_AQUA + "Stage " + (i + 1) + ":");

                    // Delivery Task
                    LinkedList<ItemStack> itemsDeliver = (LinkedList<ItemStack>) bukkitQuest.getStage(i).getItemsToDeliver();
                    LinkedList<UUID> deliveryTargets = bukkitQuest.getStage(i).getItemDeliveryTargets();

                    // Talk to Task
                    LinkedList<UUID> talkTargets = bukkitQuest.getStage(i).getNpcsToInteract();

                    // ~ Check if there are any delivery tasks
                    if (itemsDeliver != null && !itemsDeliver.isEmpty() && deliveryTargets != null && !deliveryTargets.isEmpty()) {
                        UUID lastNpc = null; // Keep track of the last NPC for deliver tasks

                        // ~ Iterate through the Delivery Items
                        for (int j = 0; j < itemsDeliver.size(); j++) {
                            UUID currentNpc = deliveryTargets.get(j); // Get the current NPC for this item

                            // Only show the NPC name if it's different from the last NPC
                            if (!currentNpc.equals(lastNpc)) {
                                lines.add(ChatColor.DARK_GREEN + "Bring to " + npcRegistry.getByUniqueId(currentNpc).getName() + ":");
                                lastNpc = currentNpc; // Update last NPC to the current one
                            }

                            // Get the item
                            ItemStack item = itemsDeliver.get(j);
                            ItemMeta itemMeta = item.getItemMeta();

                            String itemName;

                            // Check if the item has a custom name set
                            if (itemMeta != null && itemMeta.hasDisplayName()) {
                                itemName = itemMeta.getDisplayName(); // Use custom display name
                            } else {
                                itemName = ItemNames.lookupWithAmount(item); // Use default material name
                            }

                            // Add the item to the list
                            lines.add(ChatColor.WHITE + "  - " + ChatColor.GREEN + itemName);
                        }
                    }

                    // ~ Check if there are any talk to tasks
                    if (talkTargets != null && !talkTargets.isEmpty()) {
                        UUID lastNpcTalk = null; // Keep track of the last NPC for talk tasks

                        // ~ Iterate through the Talk to NPCs
                        for (int j = 0; j < talkTargets.size(); j++) {
                            UUID currentNpc = talkTargets.get(j); // Get the current NPC for this task

                            // Only show the NPC name if it's different from the last NPC
                            if (!currentNpc.equals(lastNpcTalk)) {
                                lines.add(ChatColor.DARK_GREEN + "Talk to " + npcRegistry.getByUniqueId(currentNpc).getName());
                                lastNpcTalk = currentNpc; // Update last NPC to the current one
                            }
                        }
                    }

                    // Get the start message and check for null
                    String startMessage = bukkitQuest.getStage(i).getStartMessage();

                    if (startMessage != null) {  // Check if startMessage is not null
                        // Check if the message contains ". " or "! " and split into multiple parts
                        if (startMessage.contains(", ") || startMessage.contains("! ")) {
                            String[] splitMessages = startMessage.split("(?<=, )|(?<=! )"); // Split by ", " or "! " while retaining the delimiters

                            // Iterate through all parts and add them as separate lines
                            for (String part : splitMessages) {
                                lines.add(ChatColor.ITALIC + part.trim()); // Add each part as a new line with italics
                            }
                        } else {
                            // If there's no ". ", add the message as it is
                            lines.add(ChatColor.ITALIC + startMessage);
                        }
                    }
                }





//                if (qlore.size() > 0) {lines.addAll(qlore);}
                lines.add(" ");
                lines.add(ChatColor.GOLD + "Rewards:");
                lines.add(ChatColor.YELLOW + "$" + rewards);
//                for (int i = 0; i < obj.toArray().length; i++) {
//                    lines.add(obj.get(i).getMessage());
//                }


                meta.setLore(lines);
                meta.addItemFlags(ItemFlag.values());
                display.setItemMeta(meta);

                bukkitQuest.setGUIDisplay(display);

                ItemStack questDisplay = bukkitQuest.prepareDisplay((BukkitQuester) paramQuester);
                //questDisplay.addUnsafeEnchantment(Enchantment.DURABILITY,1);


                Item item = new Item(questDisplay);


                List<String> list = item.getItemMeta().getLore();

                if (list == null)
                    continue;
                if (paramQuester.getCompletedQuests().contains(bukkitQuest) && paramQuester.getRemainingCooldown(bukkitQuest) > 0L &&
                        !bukkitQuest.getPlanner().getOverride() &&
                        item.getItemMeta() != null && item.getItemMeta().getLore() != null) {
                    Map<String, Object> map = item.serialize();
                    map.put("material", "SNOW_BALL");
                    item = new Item(map);
                    List<String> list1 = splitTime(paramQuester, ChatColor.AQUA, BukkitLang.get(paramQuester.getPlayer(), "plnTooEarly"), paramQuester.getRemainingCooldown((Quest) bukkitQuest));
                    list.add("");
                    list.addAll(list1);
                    item.setLore(list);
                }
                if (!paramQuester.isOnTime((Quest) bukkitQuest, false)) {
                    Planner planner = bukkitQuest.getPlanner();
                    long l1 = System.currentTimeMillis();
                    long l2 = planner.getStartInMillis();
                    long l3 = planner.getEndInMillis();
                    long l4 = l3 - l2;
                    long l5 = planner.getRepeat();
                    if (l2 != -1L && l1 < l2) {
                        Map<String, Object> map = item.serialize();
                        map.put("material", "WATCH");
                        item = new Item(map);
                        List<String> list1 = splitTime(paramQuester, ChatColor.YELLOW, BukkitLang.get(paramQuester.getPlayer(), "plnTooEarly"), l2 - l1);
                        list.add("");
                        list.addAll(list1);
                    } else if (l3 != -1L && l5 == -1L && l1 > l3) {
                        Map<String, Object> map = item.serialize();
                        map.put("material", "BARRIER");
                        item = new Item(map);
                        List<String> list1 = splitTime(paramQuester, ChatColor.RED, BukkitLang.get(paramQuester.getPlayer(), "plnTooLate"), l1 - l3);
                        list.add("");
                        list.addAll(list1);
                    } else if (l5 != -1L && l2 != -1L && l3 != -1L && l1 > l3) {
                        byte b = 2;
                        LinkedHashMap<Long, Long> linkedHashMap = new LinkedHashMap<Long, Long>() {
                            private static final long serialVersionUID = 3046838061019897713L;

                            protected boolean removeEldestEntry(Map.Entry<Long, Long> param1Entry) {
                                return (size() > 2);
                            }
                        };
                        long l6 = 0L;
                        if (paramQuester.getCompletedTimes().containsKey(bukkitQuest))
                            l6 = (Long) paramQuester.getCompletedTimes().get(bukkitQuest);
                        long l7 = 0L;
                        long l8 = l2;
                        long l9 = l3;
                        while (l1 >= l8) {
                            if (l8 < l6 && l6 < l9)
                                l7 = l9;
                            l8 += l5;
                            l9 = l8 + l4;
                            linkedHashMap.put(l8, l9);
                        }
                        boolean bool = false;
                        for (Map.Entry<Long, Long> entry : linkedHashMap.entrySet()) {
                            if ((Long) entry.getKey() <= l1 && l1 < (Long) entry.getValue()) {
                                bool = true;
                                break;
                            }
                        }
                        if (!bool || (bukkitQuest.getPlanner().getOverride() && l7 > 0L && l1 < l7)) {
                            Map<String, Object> map = item.serialize();
                            map.put("material", "WATCH");
                            item = new Item(map);
                            List<String> list1 = splitTime(paramQuester, ChatColor.YELLOW, BukkitLang.get(paramQuester.getPlayer(), "plnTooEarly"), l7 - l1);
                            list.add("");
                            list.addAll(list1);
                        }
                    }
                    item.setLore(list);
                }
                concurrentSkipListSet.add(new ItemSection(b2, item));
                b2++;
            }
        }
        setSections(concurrentSkipListSet);
        draw();
    }


    public void onOpen(InventoryOpenEvent paramInventoryOpenEvent) {}

    public void onClose(InventoryCloseEvent paramInventoryCloseEvent) {}

    private List<String> splitTime(Quester paramQuester, ChatColor paramChatColor, String paramString, long paramLong) {
        paramString = paramChatColor + paramString.replace("<quest>", BukkitLang.get(paramQuester.getPlayer(), "quest"));
        if (paramString.contains("<time>")) {
            int i = paramString.indexOf("<time>");
            return Arrays.asList(new String[] { paramString.substring(0, i), paramString
                    .substring(i).replace("<time>", BukkitMiscUtil.getTime(paramLong)) });
        }
        return Collections.emptyList();
    }
}


