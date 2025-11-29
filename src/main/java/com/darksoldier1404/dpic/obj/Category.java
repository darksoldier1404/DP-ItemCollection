package com.darksoldier1404.dpic.obj;

import com.darksoldier1404.dppc.api.inventory.DInventory;
import com.darksoldier1404.dppc.data.DataCargo;
import com.darksoldier1404.dppc.utils.NBT;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

import static com.darksoldier1404.dpic.ItemCollection.plugin;

public class Category implements DataCargo {
    private String name;
    private CategoryType type;
    private DInventory inventory;
    private int maxPage;
    private HashMap<Integer, Reward> rewards = new HashMap<>();
    private boolean isTotalRewardCategory = false;

    public Category() {
    }

    public Category(String name, CategoryType type, DInventory inventory, int maxPage, HashMap<Integer, Reward> rewards) {
        this.name = name;
        this.type = type;
        this.inventory = inventory;
        this.maxPage = maxPage;
        this.rewards = rewards;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryType getType() {
        return type;
    }

    public void setType(CategoryType type) {
        this.type = type;
    }

    public DInventory getInventory() {
        return inventory;
    }

    public void setInventory(DInventory inventory) {
        this.inventory = inventory;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
        if (inventory != null) {
            this.inventory.setPages(maxPage);
        }
    }

    public HashMap<Integer, Reward> getRewards() {
        return rewards;
    }

    public void setRewards(HashMap<Integer, Reward> rewards) {
        this.rewards = rewards;
    }

    public boolean isTotalRewardCategory() {
        return isTotalRewardCategory;
    }

    public void setTotalRewardCategory(boolean totalRewardCategory) {
        isTotalRewardCategory = totalRewardCategory;
    }

    public void openInventoryForUser(Player p) {
        if (plugin.checkItem == null) {
            p.sendMessage("§c[DPIC] §f체크 아이템이 설정되지 않았습니다. 관리자에게 문의하세요.");
            return;
        }
        if (inventory != null) {
            DInventory inv = inventory.clone();
            inv.setChannel(0);
            inv.setCurrentPage(0);
            inv.setObj(this.name);
            CollectionUser user = plugin.udata.get(p.getUniqueId());
            inv.applyAllItemChanges(pageItem -> {
                ItemStack item = pageItem.getItem();
                if (user.getCollections().containsKey(name)) {
                    if (user.getCollections().get(name).containsKey(pageItem.getPage())) {
                        if (user.getCollections().get(name).get(pageItem.getPage()).contains(pageItem.getSlot())) {
                            pageItem.setItem(plugin.checkItem.clone());
                        }else{
                            pageItem.setItem(NBT.setStringTag(item, "dpic_item", "true"));
                        }
                    } else {
                        pageItem.setItem(NBT.setStringTag(item, "dpic_item", "true"));
                    }
                    return pageItem;
                } else {
                    pageItem.setItem(NBT.setStringTag(item, "dpic_item", "true"));
                    return pageItem;
                }
            });
            inv.update();
            inv.applyChanges();
            inv.openInventory(p);
        }
    }

    public void collect(Player p, DInventory inv, DInventory.PageItemSet pageItem) {
        System.out.println("collect called : " + pageItem.getPage() + " : " + pageItem.getSlot());
        if (plugin.checkItem == null) {
            p.sendMessage("§c[DPIC] §f체크 아이템이 설정되지 않았습니다. 관리자에게 문의하세요.");
            return;
        }
        if (inv != null) {
            CollectionUser user = plugin.udata.get(p.getUniqueId());
            if (user.hasCollected(getName(), pageItem.getPage(), pageItem.getSlot())) {
                p.sendMessage("이미 수집한 아이템입니다.");
                return;
            }
            ItemStack item = pageItem.getItem();
            for (ItemStack pi : p.getInventory().getStorageContents()) {
                if (item.isSimilar(pi) && item.getAmount() <= pi.getAmount()) {
                    p.getInventory().removeItem(item);
                    p.sendMessage("아이템 수집 완료!");
                    plugin.udata.put(p.getUniqueId(), user.collect(this.name, pageItem.getPage(), pageItem.getSlot()));
                    inv.setPageItem(pageItem.getSlot(), NBT.setStringTag(plugin.checkItem.clone(), "dpic_collected", "true"));
                    inv.update();
                    inv.applyChanges();
                    break;
                }
            }
        }
    }

    public boolean isStepReached(CollectionUser collectionUser, int step) {
        if (collectionUser.getCollections().containsKey(name)) {
            int collectedCount = collectionUser.getCollectedItemCount(name);
            return collectedCount >= step;
        }
        return false;
    }

    @Override
    public YamlConfiguration serialize() {
        YamlConfiguration data = new YamlConfiguration();
        data.set("name", name);
        data.set("type", type.toString());
        if (!rewards.isEmpty()) {
            for (Integer slot : rewards.keySet()) {
                data.set("rewards." + slot, rewards.get(slot).getName());
            }
        }
        data = inventory.serialize(data);
        return data;
    }

    @Override
    public Category deserialize(YamlConfiguration data) {
        this.name = data.getString("name");
        if (data.getString("type") != null) {
            this.type = CategoryType.valueOf(data.getString("type"));
        }
        if (data.getConfigurationSection("rewards") != null) {
            data.getConfigurationSection("rewards").getKeys(false).forEach(slot -> {
                Reward reward = new Reward();
                reward.setName(data.getString("rewards." + slot));
                this.rewards.put(Integer.parseInt(slot), reward);
            });
        }
        this.inventory = new DInventory("아이템 콜렉션", 54, true, true, plugin).deserialize(data);
        this.maxPage = this.inventory.getPages();
        return this;
    }
}
