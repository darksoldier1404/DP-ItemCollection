package com.darksoldier1404.dpic;

import com.darksoldier1404.dpic.commands.DPICCommand;
import com.darksoldier1404.dpic.events.DPICEvent;
import com.darksoldier1404.dpic.functions.DPICFunction;
import com.darksoldier1404.dpic.obj.Category;
import com.darksoldier1404.dpic.obj.CollectionUser;
import com.darksoldier1404.dpic.obj.Reward;
import com.darksoldier1404.dpic.obj.TotalRewardCategory;
import com.darksoldier1404.dppc.annotation.DPPCoreVersion;
import com.darksoldier1404.dppc.data.DPlugin;
import com.darksoldier1404.dppc.data.DataContainer;
import com.darksoldier1404.dppc.data.DataType;
import com.darksoldier1404.dppc.utils.ConfigUtils;
import com.darksoldier1404.dppc.utils.PluginUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@DPPCoreVersion(since = "5.3.0")
public class ItemCollection extends DPlugin {
    public static ItemCollection plugin;
    public static DataContainer<String, Category> categories;
    public static DataContainer<String, Reward> rewards;
    public static DataContainer<UUID, CollectionUser> udata;
    public static TotalRewardCategory totalRewardCategory;
    public static @Nullable ItemStack checkItem;

    public ItemCollection() {
        super(false);
        plugin = this;
        init();
        categories = loadDataContainer(new DataContainer<>(this, DataType.CUSTOM, "categories"), Category.class);
        rewards = loadDataContainer(new DataContainer<>(this, DataType.CUSTOM, "rewards"), Reward.class);
        udata = loadDataContainer(new DataContainer<>(this, DataType.CUSTOM, "udata"), CollectionUser.class);
    }

    public static ItemCollection getInstance() {
        return plugin;
    }

    @Override
    public void onLoad() {
        DPICFunction.initPlaceholder();
    }

    @Override
    public void onEnable() {
        checkItem = config.getItemStack("Settings.checkItem");
        YamlConfiguration data = ConfigUtils.loadCustomData(plugin, "totalRewardCategory", "totalRewardCategory");
        if (data != null) {
            totalRewardCategory = (TotalRewardCategory) new TotalRewardCategory().deserialize(data);
        }else{
            totalRewardCategory = new TotalRewardCategory();
        }
        ConfigUtils.saveCustomData(plugin, totalRewardCategory.serialize(), "totalRewardCategory", "totalRewardCategory");
        PluginUtil.addPlugin(plugin, 27465);
        getServer().getPluginManager().registerEvents(new DPICEvent(), plugin);
        getCommand("dpic").setExecutor(new DPICCommand().getBuilder());
    }

    @Override
    public void onDisable() {
        ConfigUtils.saveCustomData(plugin, totalRewardCategory.serialize(), "totalRewardCategory", "totalRewardCategory");
        saveAllData();
    }
}
