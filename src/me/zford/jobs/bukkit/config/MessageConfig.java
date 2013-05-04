/**
 * Jobs Plugin for Bukkit
 * Copyright (C) 2011 Zak Ford <zak.j.ford@gmail.com>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.zford.jobs.bukkit.config;

import java.io.File;
import java.io.IOException;

import me.zford.jobs.bukkit.JobsPlugin;
import me.zford.jobs.util.ChatColor;

import org.bukkit.configuration.file.YamlConfiguration;

public class MessageConfig {
    
    private enum JobsMessageEnum {
        STATS_NO_JOB("ChatColor.REDPlease join a job first."),
        STATS_JOB("lvl%joblevel% %jobcolour%%jobname%ChatColor.WHITE : %jobexp%/%jobmaxexp% exp"),
        BROWSE_NO_JOBS("There are no jobs you can join."),
        BROWSE_JOBS_HEADER("You are allowed to join the following jobs :"),
        BROWSE_JOBS_FOOTER("For more information type in /jobs info [JobName]"),
        ADMIN_COMMAND_SUCCESS("Your command has been performed."),
        ADMIN_COMMAND_FAILED("ChatColor.REDThere was an error in the command."),
        FIRE_TARGET("You have been fired from %jobcolour%%jobname%."),
        FIRE_TARGET_NO_JOB("Plyer does not have the job %jobcolour%%jobname%."),
        EMPLOY_TARGET("You have been employed in %jobcolour%%jobname%."),
        PROMOTE_TARGET("You have been promoted %levelsgained% levels in %jobcolour%%jobname%."),
        DEMOTE_TARGET("You have been demoted %levelslost% levels in %jobcolour%%jobname%."),
        GRANTXP_TARGET("You have been granted %expgained% experience in %jobcolour%%jobname%."),
        REMOVEXP_TARGET("You have lost %explost% experience in %jobcolour%%jobname%."),
        TRANSFER_TARGET("You have been transferred from %oldjobcolour%%oldjobname% to %newjobcolour%%newjobname%."),
        JOIN_TOO_MANY_JOBS("ChatColor.REDYou have already joined too many jobs."),
        BREAK_NONE("%jobcolour%%jobname%ChatColor.WHITE does not get money for breaking anything."),
        PLACE_NONE("%jobcolour%%jobname%ChatColor.WHITE does not get money for placing anything."),
        KILL_NONE("%jobcolour%%jobname%ChatColor.WHITE does not get money for killing anything."),
        FISH_NONE("%jobcolour%%jobname%ChatColor.WHITE does not get money for fishing."),
        CRAFT_NONE("%jobcolour%%jobname%ChatColor.WHITE does not get money from crafting."),
        SMELT_NONE("%jobcolour%%jobname%ChatColor.WHITE does not get money from smelting."),
        BREW_NONE("%jobcolour%%jobname%ChatColor.WHITE does not get money from brewing."),
        ENCHANT_NONE("%jobcolour%%jobname%ChatColor.WHITE does not get money from enchanting."),
        AT_MAX_LEVEL("ChatColor.YELLOW-- You have reached the maximum level --"),
        SKILL_UP_BROADCAST("%playername% has been promoted to a %titlecolour%%titlename% %jobcolour%%jobname%ChatColor.WHITE."),
        SKILL_UP_NO_BROADCAST("Congratulations, you have been promoted to a %titlecolour%%titlename% %jobcolour%%jobname%ChatColor.WHITE."),
        LEVEL_UP_BROADCAST("%playername% is now a level %joblevel% %jobcolour%%jobname%ChatColor.WHITE."),
        LEVEL_UP_NO_BROADCAST("You are now a level %joblevel% %jobcolour%%jobname%ChatColor.WHITE."),
        JOIN_JOB_SUCCESS("You have joined the job %jobcolour%%jobname%ChatColor.WHITE."),
        JOIN_JOB_FAILED_ALREADY_IN("You are already in the job %jobcolour%%jobname%ChatColor.WHITE."),
        JOIN_JOB_FAILED_TOO_MANY("You have already joined too many jobs."),
        JOIN_JOB_FAILED_NO_SLOTS("You cannot join the job %jobcolour%%jobname%ChatColor.WHITE, there are no slots available."),
        LEAVE_JOB_SUCESS("You have left the job %jobcolour%%jobname%ChatColor.WHITE."),
        ERROR_NO_JOB("ChatColor.REDThe job you have selected does not exist!"),
        ERROR_NO_PERMISSION("ChatColor.REDYou do not have permission to do that!"),
        JOIN_TOO_MANY_JOB("ChatColor.REDYou have joined too many jobs."),
        LEAVE_JOB_FAILED_TOO_MANY("ChatColor.REDYou have joined too many jobs!"),
        LEAVE_JOB_SUCCESS("You have left the job %jobcolour%%jobname%ChatColor.WHITE.");
        
        private String message;
        
        private JobsMessageEnum(String message) {
            this.message = message;
        }
        
        public String getMessage() {
            return message;
        }
        
        public String toConfigName() {
            return name().toLowerCase().replace('_', '-');
        }
    }
    
    private YamlConfiguration config = new YamlConfiguration();
    private JobsPlugin plugin;
    
    /**
     * Constructor
     */
    public MessageConfig(JobsPlugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Reloads the config
     */
    public void reload() {
        File file = new File(plugin.getDataFolder(), "messageConfig.yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        boolean loaded = false;
        try {
            config.load(file);
            loaded = true;
        } catch (Exception e) {
            plugin.getServer().getLogger().severe("==================== Jobs ====================");
            plugin.getServer().getLogger().severe("Unable to load messageConfig.yml!");
            plugin.getServer().getLogger().severe("Check your config for formatting issues!");
            plugin.getServer().getLogger().severe("Default messages were loaded instead!");
            plugin.getServer().getLogger().severe("Error: "+e.getMessage());
            plugin.getServer().getLogger().severe("==============================================");
        }
        StringBuilder header = new StringBuilder()
            .append("Configuration file for the messages").append(System.getProperty("line.separator"))
            .append(System.getProperty("line.separator"))
            .append("Replace the messages if you want.").append(System.getProperty("line.separator"))
            .append(System.getProperty("line.separator"))
            .append("ChatColor.<Color> will make any words following (including spaces that colour).")
            .append(System.getProperty("line.separator"))
            .append(System.getProperty("line.separator"))
            .append("Supported colors:").append(System.getProperty("line.separator"));
        for (ChatColor color : ChatColor.values()) {
            header.append("   "+color.name().toUpperCase()).append(System.getProperty("line.separator"));
        }
        header.append(System.getProperty("line.separator"))
            .append("Each message has slightly different parameters. The parameters available")
            .append(System.getProperty("line.separator"))
            .append("are the ones that are already in the message (and none others)")
            .append(System.getProperty("line.separator")).append(System.getProperty("line.separator"))
            .append("NOTE:").append(System.getProperty("line.separator"))
            .append("  Any character other than normal characters will not get read and will crash the ")
            .append(System.getProperty("line.separator"))
            .append("configuration.").append(System.getProperty("line.separator"))
            .append(System.getProperty("line.separator"));
        
        config.options().header(header.toString());
        
        for(JobsMessageEnum message : JobsMessageEnum.values()) {
            String key = message.toConfigName();
            Object value = config.get(key);
            if(!(value instanceof String)) {
                config.set(key, message.getMessage());
            }
        }
        if (loaded) {
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Parse ChatColors from YML configuration file
     * @param value - configuration string
     * @return string with replaced colors
     */
    private String parseColors(String value) {
        if (value == null)
            return null;
        for (ChatColor color : ChatColor.values()) {
            value = value.replace("ChatColor."+color.name().toUpperCase(), color.toString());
        }
        return value;
    }
    
    /**
     * Get the message with the correct key
     * @param key - the key of the message
     * @return the message
     */
    public String getMessage(String key) {
        return parseColors(config.getString(key));
    }
}
