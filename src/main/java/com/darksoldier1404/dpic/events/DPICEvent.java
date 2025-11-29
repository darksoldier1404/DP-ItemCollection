package com.darksoldier1404.dpic.events;

import com.darksoldier1404.dpic.functions.DPICFunction;
import com.darksoldier1404.dpic.obj.Category;
import com.darksoldier1404.dpic.obj.CollectionUser;
import com.darksoldier1404.dpic.obj.Reward;
import com.darksoldier1404.dpic.obj.TotalRewardCategory;
import com.darksoldier1404.dppc.api.inventory.DInventory;
import com.darksoldier1404.dppc.events.dinventory.DInventoryClickEvent;
import com.darksoldier1404.dppc.events.dinventory.DInventoryCloseEvent;
import com.darksoldier1404.dppc.utils.NBT;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import static com.darksoldier1404.dpic.ItemCollection.plugin;

public class DPICEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!plugin.udata.containsKey(p.getUniqueId())) {
            plugin.udata.put(p.getUniqueId(), new CollectionUser(p.getUniqueId()));
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        plugin.udata.save(p.getUniqueId());
    }


    @EventHandler
    public void onInventoryClose(DInventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        DInventory inv = (DInventory) e.getInventory().getHolder();
        if (inv.isValidHandler(plugin)) {
            if (inv.isValidChannel(101)) { // collection item edit save
                inv.applyChanges();
                DPICFunction.saveCollectionItems(p, inv);
                return;
            }
            if (inv.isValidChannel(201)) { // reward item edit save
                inv.applyChanges();
                DPICFunction.saveRewardItems(p, inv);
                return;
            }
            if (inv.isValidChannel(301)) { // check item edit save
                inv.applyChanges();
                DPICFunction.saveCheckItem(p, inv);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(DInventoryClickEvent e) {
        DInventory inv = e.getDInventory();
        Player p = (Player) e.getWhoClicked();
        if (inv.isValidHandler(plugin)) {
            ItemStack item = e.getCurrentItem();
            if (item == null || item.getType().isAir()) {
                return;
            }
            if (inv.isValidChannel(0)) {
                e.setCancelled(true);
                if (e.getClickedInventory().getType() == InventoryType.PLAYER) {
                    return;
                }
                if (NBT.hasTagKey(item, "dpic_item")) {
                    e.setCancelled(true);
                    Category category = plugin.categories.get(inv.getObj());
                    if (category != null) {
                        category.collect(p, inv, new DInventory.PageItemSet(inv.getCurrentPage(), e.getSlot(), NBT.removeTag(item, "dpic_item")));
                        return;
                    }
                    return;
                }
            }
            if (inv.isValidChannel(1)) {
                e.setCancelled(true);
                if (e.getClickedInventory().getType() == InventoryType.PLAYER) {
                    return;
                }
                Category category = (Category) inv.getObj();
                if (category != null) {
                    boolean canClaim = NBT.getBooleanTag(item, "dpic_canclaim");
                    boolean isClaimed = NBT.getBooleanTag(item, "dpic_claimed");
                    if (isClaimed) {
                        p.sendMessage(plugin.prefix + "§c이미 수령한 리워드입니다.");
                        return;
                    }
                    if (!canClaim) {
                        p.sendMessage(plugin.prefix + "§c이 리워드는 아직 수령할 수 없습니다.");
                        return;
                    }
                    if (plugin.checkItem == null) {
                        p.sendMessage("§c[DPIC] §f체크 아이템이 설정되지 않았습니다. 관리자에게 문의하세요.");
                        return;
                    }
                    String rewardKey = NBT.getStringTag(item, "dpic_rewardname");
                    Reward reward = plugin.rewards.get(rewardKey);
                    if (reward.giveReward(p)) {
                        CollectionUser user = plugin.udata.get(p.getUniqueId());
                        if (user != null) {
                            int step = NBT.getIntegerTag(item, "dpic_step");
                            if (category instanceof TotalRewardCategory) {
                                user.claimTotalReward(step);
                            } else {
                                user.claimCategoryReward(category.getName(), step);
                            }
                            p.sendMessage(plugin.prefix + "§a콜렉션 리워드를 수령하였습니다.");
                            user.openRewardClaimInventory(p, category.getName(), category.isTotalRewardCategory());
                        }
                    }
                }
            }
        }
    }
}
