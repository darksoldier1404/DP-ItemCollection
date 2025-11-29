package com.darksoldier1404.dpic.obj;

import com.darksoldier1404.dpic.ItemCollection;
import com.darksoldier1404.dppc.api.inventory.DInventory;
import com.darksoldier1404.dppc.data.DataCargo;
import com.darksoldier1404.dppc.utils.NBT;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.Collectors;

import static com.darksoldier1404.dpic.ItemCollection.categories;
import static com.darksoldier1404.dpic.ItemCollection.plugin;

public class CollectionUser implements DataCargo {
    private UUID uuid;
    private HashMap<String, Map<Integer, Set<Integer>>> collections = new HashMap<>(); // categoryName, list of collected item page, slots
    private HashMap<String, List<Integer>> claimedCategoryRewards = new HashMap<>(); // categoryName, list of claimed reward steps
    private List<Integer> claimedTotalRewards = new ArrayList<>(); // list of claimed total reward steps

    public CollectionUser() {
    }

    public CollectionUser(UUID uuid) {
        this.uuid = uuid;
    }

    public CollectionUser(UUID uuid, HashMap<String, Map<Integer, Set<Integer>>> collections, HashMap<String, List<Integer>> claimedCategoryRewards, List<Integer> claimedTotalRewards) {
        this.uuid = uuid;
        this.collections = collections;
        this.claimedCategoryRewards = claimedCategoryRewards;
        this.claimedTotalRewards = claimedTotalRewards;
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public HashMap<String, Map<Integer, Set<Integer>>> getCollections() {
        return collections;
    }

    public void setCollections(HashMap<String, Map<Integer, Set<Integer>>> collections) {
        this.collections = collections;
    }

    public HashMap<String, List<Integer>> getClaimedCategoryRewards() {
        return claimedCategoryRewards;
    }

    public void setClaimedCategoryRewards(HashMap<String, List<Integer>> claimedCategoryRewards) {
        this.claimedCategoryRewards = claimedCategoryRewards;
    }

    public List<Integer> getClaimedTotalRewards() {
        return claimedTotalRewards;
    }

    public void setClaimedTotalRewards(List<Integer> claimedTotalRewards) {
        this.claimedTotalRewards = claimedTotalRewards;
    }

    public int getCollectedItemCount(String categoryName) {
        int count = 0;
        if (collections.containsKey(categoryName)) {
            for (int page : collections.get(categoryName).keySet()) {
                if (collections.get(categoryName).get(page) != null) {
                    count += collections.get(categoryName).get(page).size();
                }
            }
        }
        return count;
    }

    public int getCollectedItemCount() {
        int total = 0;
        for (String categoryName : collections.keySet()) {
            total += getCollectedItemCount(categoryName);
        }
        return total;
    }

    public boolean hasReceivedStepReward(int step) {
        return claimedTotalRewards.contains(step);
    }

    public void markStepRewardReceived(int step) {
        if (!claimedTotalRewards.contains(step)) {
            claimedTotalRewards.add(step);
        }
    }

    public CollectionUser collect(String categoryName, int page, int slot) {
        collections.putIfAbsent(categoryName, new HashMap<>());
        collections.get(categoryName).putIfAbsent(page, new HashSet<>());
        Map<Integer, Set<Integer>> map = collections.get(categoryName);
        Set<Integer> set = map.get(page);
        set.add(slot);
        map.put(page, set);
        collections.put(categoryName, map);
        return this;
    }

    public boolean hasCollected(String categoryName, int page, int slot) {
        return collections.containsKey(categoryName) && collections.get(categoryName).containsKey(page) && collections.get(categoryName).get(page).contains(slot);
    }

    public CollectionUser claimCategoryReward(String categoryName, int step) {
        claimedCategoryRewards.putIfAbsent(categoryName, new ArrayList<>());
        if (!claimedCategoryRewards.get(categoryName).contains(step)) {
            claimedCategoryRewards.get(categoryName).add(step);
        }
        return this;
    }

    public boolean hasClaimedCategoryReward(String categoryName, int step) {
        return claimedCategoryRewards.containsKey(categoryName) && claimedCategoryRewards.get(categoryName).contains(step);
    }

    public CollectionUser claimTotalReward(int step) {
        if (!claimedTotalRewards.contains(step)) {
            claimedTotalRewards.add(step);
        }
        return this;
    }

    public boolean hasClaimedTotalReward(int step) {
        return claimedTotalRewards.contains(step);
    }

    public void openRewardClaimInventory(Player p, String categoryName, boolean openTotalRewards) {
        if (plugin.checkItem == null) {
            p.sendMessage("§c[DPIC] §f체크 아이템이 설정되지 않았습니다. 관리자에게 문의하세요.");
            return;
        }
        DInventory inv = new DInventory("콜렉션 리워드 수령 : " + categoryName, 54, true, true, plugin);
        inv.setChannel(1);
        Category category;
        if (openTotalRewards) {
            category = ItemCollection.totalRewardCategory;
        } else {
            category = categories.get(categoryName);
        }
        if (category == null) return;
        inv.setObj(category);
        List<ItemStack> items = new ArrayList<>();
        for (int step : category.getRewards().keySet().stream().sorted().collect(Collectors.toList())) {
            Reward reward = category.getRewards().get(step);
            if (reward == null) continue;
            boolean canClaim = category.isStepReached(this, step);
            boolean claimed;
            if (openTotalRewards) {
                claimed = hasClaimedTotalReward(step);
            } else {
                claimed = hasClaimedCategoryReward(categoryName, step);
            }
            ItemStack item;
            if (claimed) {
                item = ItemCollection.checkItem.clone();
            } else {
                item = new ItemStack(Material.NETHER_STAR);
            }
            String name = (claimed ? "§7" : (canClaim ? "§a" : "§c")) + reward.getName();
            List<String> lore = new ArrayList<>();
            lore.add("§f- 리워드 단계: §e" + step);
            lore.add("§f- 수령 가능 여부: " + (claimed ? "§7이미 수령함" : (canClaim ? "§a수령 가능" : "§c수령 불가")));
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(name);
            meta.setLore(lore);
            item.setItemMeta(meta);
            item = NBT.setStringTag(item, "dpic_rewarditem", "true");
            item = NBT.setBooleanTag(item, "dpic_canclaim", canClaim && !claimed);
            item = NBT.setBooleanTag(item, "dpic_claimed", claimed);
            item = NBT.setStringTag(item, "dpic_category", categoryName);
            item = NBT.setStringTag(item, "dpic_rewardname", reward.getName());
            item = NBT.setIntTag(item, "dpic_step", step);
            items.add(item);
        }
        inv.addPageItems(items);
        inv.update();
        inv.applyChanges();
        inv.openInventory(p);
    }

    @Override
    public YamlConfiguration serialize() {
        YamlConfiguration data = new YamlConfiguration();
        data.set("uuid", uuid.toString());
        // // HashMap<String, Map<Integer, Set<Integer>>>
        for (String key : collections.keySet()) {
            for (int page : collections.get(key).keySet()) {
                for (int slot : collections.get(key).get(page)) {
                    data.set("collections." + key + "." + page + "." + slot, slot);
                }
            }
        }
        for (String key : claimedCategoryRewards.keySet()) {
            for (int i = 0; i < claimedCategoryRewards.get(key).size(); i++) {
                data.set("claimed-category-rewards." + key + "." + i, claimedCategoryRewards.get(key).get(i));
            }
        }
        for (int i = 0; i < claimedTotalRewards.size(); i++) {
            data.set("claimed-total-rewards." + i, claimedTotalRewards.get(i));
        }
        return data;
    }

    @Override
    public CollectionUser deserialize(YamlConfiguration data) {
        this.uuid = UUID.fromString(data.getString("uuid"));
        if (data.contains("collections")) {
            for (String key : data.getConfigurationSection("collections").getKeys(false)) {
                Map<Integer, Set<Integer>> pages = new HashMap<>();
                for (String pageKey : data.getConfigurationSection("collections." + key).getKeys(false)) {
                    Set<Integer> slots = new HashSet<>();
                    for (String slotKey : data.getConfigurationSection("collections." + key + "." + pageKey).getKeys(false)) {
                        slots.add(data.getInt("collections." + key + "." + pageKey + "." + slotKey));
                    }
                    pages.put(Integer.parseInt(pageKey), slots);
                }
                this.collections.put(key, pages);
            }
        }
        if (data.contains("claimed-category-rewards")) {
            for (String key : data.getConfigurationSection("claimed-category-rewards").getKeys(false)) {
                List<Integer> claimed = new ArrayList<>();
                for (String index : data.getConfigurationSection("claimed-category-rewards." + key).getKeys(false)) {
                    claimed.add(data.getInt("claimed-category-rewards." + key + "." + index));
                }
                this.claimedCategoryRewards.put(key, claimed);
            }
        }
        if (data.contains("claimed-total-rewards")) {
            for (String key : data.getConfigurationSection("claimed-total-rewards").getKeys(false)) {
                this.claimedTotalRewards.add(data.getInt("claimed-total-rewards." + key));
            }
        }
        return this;
    }
}
