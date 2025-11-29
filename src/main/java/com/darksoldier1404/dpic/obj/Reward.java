package com.darksoldier1404.dpic.obj;

import com.darksoldier1404.dppc.api.inventory.DInventory;
import com.darksoldier1404.dppc.data.DataCargo;
import com.darksoldier1404.dppc.utils.InventoryUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.darksoldier1404.dpic.ItemCollection.plugin;

public class Reward implements DataCargo {
    private String name;
    private List<String> commandRewards = new ArrayList<>();
    private DInventory inventory; // item rewards

    public Reward() {
    }

    public Reward(String name, List<String> commandRewards, DInventory inventory) {
        this.name = name;
        this.commandRewards = commandRewards;
        this.inventory = inventory;
    }

    public Reward(String name, DInventory inventory) {
        this.name = name;
        this.inventory = inventory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCommandRewards() {
        return commandRewards;
    }

    public void setCommandRewards(List<String> commandRewards) {
        this.commandRewards = commandRewards;
    }

    public DInventory getInventory() {
        return inventory;
    }

    public void setInventory(DInventory inventory) {
        this.inventory = inventory;
    }

    public boolean giveReward(Player p) {
        if (plugin.checkItem == null) {
            p.sendMessage("§c[DPIC] §f체크 아이템이 설정되지 않았습니다. 관리자에게 문의하세요.");
            return false;
        }
        if (inventory != null) {
            List<ItemStack> items = Arrays.stream(inventory.getContents()).filter(item -> item != null && !item.getType().isAir()).collect(Collectors.toList());
            if (InventoryUtils.hasEnoughSpace(p.getInventory().getStorageContents(), items)) {
                for (ItemStack item : inventory.getContents()) {
                    if (item != null && !item.getType().isAir()) {
                        p.getInventory().addItem(item);
                    }
                }
            } else {
                p.sendMessage(plugin.prefix + "인벤토리에 아이템을 넣을 공간이 부족합니다. 인벤토리를 정리해주세요.");
                return false;
            }
        }
        if (!commandRewards.isEmpty()) {
            for (String command : commandRewards) {
                plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command.replace("<player>", p.getName()));
            }
        }
        return true;
    }

    @Override
    public YamlConfiguration serialize() {
        YamlConfiguration data = new YamlConfiguration();
        data.set("name", name);
        if (!commandRewards.isEmpty()) {
            for (int i = 0; i < commandRewards.size(); i++) {
                data.set("command-rewards." + i, commandRewards.get(i));
            }
        }
        data = inventory.serialize(data);
        return data;
    }

    @Override
    public Object deserialize(YamlConfiguration data) {
        if (data != null) {
            this.name = data.getString("name");
            if (data.getConfigurationSection("command-rewards") != null) {
                data.getConfigurationSection("command-rewards").getKeys(false).forEach(key -> {
                    String command = data.getString("command-rewards." + key);
                    if (command != null && !command.isEmpty()) {
                        commandRewards.add(command);
                    }
                });
            }
            this.inventory = new DInventory("아이템 콜렉션 리워드", 27, plugin).deserialize(data);
            return this;
        }
        return null;
    }
}
