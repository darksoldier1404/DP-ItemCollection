package com.darksoldier1404.dpic.obj;

import com.darksoldier1404.dppc.api.inventory.DInventory;

import static com.darksoldier1404.dpic.ItemCollection.plugin;

public class TotalRewardCategory extends Category {

    public TotalRewardCategory() {
        setName("Total Rewards");
        setType(CategoryType.TOTAL_REWARD_ONLY);
        setInventory(new DInventory("Total Reward Collection", 54, true, true, plugin));
        setTotalRewardCategory(true);
    }

    @Override
    public boolean isStepReached(CollectionUser collectionUser, int step) {
        return collectionUser.getCollectedItemCount() >= step;
    }
}
