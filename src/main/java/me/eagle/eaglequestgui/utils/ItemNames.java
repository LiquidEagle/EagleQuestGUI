package me.eagle.eaglequestgui.utils;



/**
 * Class to get the displayed name (as the client shows) for an item.
 * Credit to https://www.spigotmc.org/threads/how-to-get-a-user-friendly-item-name.373484/
 */
public class ItemNames {
    public static String capitalize(String input, String split) {
        String output = "";
        for (String s : input.split(split)) {
            output += s.substring(0, 1).toUpperCase()+s.substring(1).toLowerCase()+" ";
        }
        return output.substring(0, output.length()-1);
    }
}