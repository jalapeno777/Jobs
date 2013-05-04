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

package me.zford.jobs.bukkit.tasks;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Future;

import net.milkbowl.vault.economy.Economy;

import me.zford.jobs.bukkit.JobsPlugin;
import me.zford.jobs.bukkit.economy.BufferedEconomy;

public class BufferedPaymentThread extends Thread {
    
    private JobsPlugin plugin;
    private BufferedEconomy bufferedEconomy;
    
    private volatile boolean running = true;
    private int sleep;
    
    public BufferedPaymentThread(JobsPlugin plugin, BufferedEconomy bufferedEconomy, int duration) {
        super("Jobs-BufferedPaymentThread");
        this.plugin = plugin;
        this.bufferedEconomy = bufferedEconomy;
        this.sleep = duration * 1000;
    }

    @Override
    public void run() {
        plugin.getLogger().info("Started buffered payment thread");
        while (running) {
            try {
                sleep(sleep);
            } catch (InterruptedException e) {
                this.running = false;
                continue;
            }
            try {
                Future<Economy> economyFuture = plugin.getServer().getScheduler().callSyncMethod(plugin, new EconomyRegistrationCallable());
                Economy economy = economyFuture.get();
                if (economy != null)
                    bufferedEconomy.payAll(economy);
            } catch (InterruptedException e) {
                running = false;
                continue;
            } catch (CancellationException e) {
                running = false;
                continue;
            } catch (Throwable t) {
                t.printStackTrace();
                plugin.getLogger().severe("Exception in BufferedPaymentThread, stopping economy payments!");
                running = false;
            }
        }
        plugin.getLogger().info("Buffered payment thread shutdown");   
    }
    
    public void shutdown() {
        this.running = false;
        interrupt();
    }
    
    public class EconomyRegistrationCallable implements Callable<Economy> {
        @Override
        public Economy call() throws Exception {
            return plugin.getEconomy();
        }
    }
}
