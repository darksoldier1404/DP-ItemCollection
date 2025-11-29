package com.darksoldier1404.dpic.commands;

import com.darksoldier1404.dpic.functions.DPICFunction;
import com.darksoldier1404.dppc.builder.command.CommandBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static com.darksoldier1404.dpic.ItemCollection.plugin;


public class DPICCommand {
    CommandBuilder builder = new CommandBuilder(plugin);

    public DPICCommand() {
        builder.addSubCommand("create", "dpic.admin", "/dpic create <collection/reward> <name>", (p, args) -> {
            if (args.length == 3) {
                if (args[1].equalsIgnoreCase("collection")) {
                    DPICFunction.createCollection(p, args[2]);
                    return true;
                } else if (args[1].equalsIgnoreCase("reward")) {
                    DPICFunction.createReward(p, args[2]);
                    return true;
                } else {
                    p.sendMessage(plugin.getPrefix() + "§c올바른 타입이 아닙니다. (collection/reward)");
                    return true;
                }
            }
            return false;
        });

        builder.addSubCommand("maxpage", "dpic.admin", "/dpic maxpage <collection> <maxPage>", (p, args) -> {
            if (args.length == 3) {
                DPICFunction.setMaxPage(p, args[1], args[2]);
                return true;
            }
            return false;
        });

        builder.addSubCommand("items", "dpic.admin", "/dpic items <collection/reward> <name>", (p, args) -> {
            if (args.length == 3) {
                if (args[1].equalsIgnoreCase("collection")) {
                    DPICFunction.editCollectionItems(p, args[2]);
                } else if (args[1].equalsIgnoreCase("reward")) {
                    DPICFunction.editRewardItems(p, args[2]);
                } else {
                    p.sendMessage(plugin.getPrefix() + "§c올바른 타입이 아닙니다. (collection/reward)");
                    return true;
                }
                return true;
            }
            return false;
        });

        builder.addSubCommand("addcmdreward", "dpic.admin", "/dpic addcmdreward <rewardName> <command>", (p, args) -> {
            if (args.length >= 3) {
                String command = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                DPICFunction.addCommandReward(p, args[1], command);
                return true;
            }
            return false;
        });

        builder.addSubCommand("removecmdreward", "dpic.admin", "/dpic removecmdreward <rewardName> <index>", (p, args) -> {
            if (args.length >= 3) {
                DPICFunction.removeCommandReward(p, args[1], args[2]);
                return true;
            }
            return false;
        });

        builder.addSubCommand("setcollectionreward", "dpic.admin", "/dpic setreward <collectionName> <rewardName> <step>", (p, args) -> {
            if (args.length == 4) {
                DPICFunction.setRewardToCollection(p, args[1], args[2], args[3]);
                return true;
            }
            return false;
        });

        builder.addSubCommand("removecollectionreward", "dpic.admin", "/dpic removereward <collectionName> <step>", (p, args) -> {
            if (args.length == 3) {
                DPICFunction.removeRewardFromCollection(p, args[1], args[2]);
                return true;
            }
            return false;
        });

        // total reward set
        builder.addSubCommand("settotalreward", "dpic.admin", "/dpic settotalreward <rewardName> <step>", (p, args) -> {
            if (args.length == 3) {
                DPICFunction.setTotalReward(p, args[1], args[2]);
                return true;
            }
            return false;
        });

        builder.addSubCommand("removetotalreward", "dpic.admin", "/dpic removetotalreward <step>", (p, args) -> {
            if (args.length == 2) {
                DPICFunction.removeTotalReward(p, args[1]);
                return true;
            }
            return false;
        });

        builder.addSubCommand("checkitem", "dpic.admin", "/dpic checkitem", (p, args) -> {
            if (args.length == 1) {
                DPICFunction.openCheckItemSettingGUI(p);
                return true;
            }
            return false;
        });

        builder.addSubCommand("open", "/dpic open <collectionName>", (p, args) -> {
            if (args.length == 2) {
                DPICFunction.openCollection(p, args[1]);
                return true;
            }
            return false;
        });

        builder.addSubCommand("openrewardclaim", "/dpic openrewardclaim (collectionName) - 콜렉션 이름을 지정하지 않으면 전체 보상으로 오픈합니다.", (p, args) -> {
            if (args.length == 1) {
                DPICFunction.openTotalRewardClaimInventory(p);
                return true;
            }
            if (args.length == 2) {
                DPICFunction.openRewardClaimInventory(p, args[1]);
                return true;
            }
            return false;
        });

        for (String c : builder.getSubCommandNames()) {
            builder.addTabCompletion(c, (sender, args) -> {
                if (args.length == 1) {
                    return Arrays.stream(builder.getSubCommandNames().toArray(new String[0]))
                            .filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase()))
                            .collect(Collectors.toList());
                }
                if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("create")) {
                        return Arrays.asList("collection", "reward");
                    } else if (args[0].equalsIgnoreCase("items")) {
                        return Arrays.asList("collection", "reward");
                    } else if (args[0].equalsIgnoreCase("openrewardclaim")) {
                        return DPICFunction.getCollectionNames();
                    } else if (args[0].equalsIgnoreCase("setcollectionreward")) {
                        return DPICFunction.getCollectionNames();
                    } else if (args[0].equalsIgnoreCase("removecollectionreward")) {
                        return DPICFunction.getCollectionNames();
                    } else if (args[0].equalsIgnoreCase("settotalreward")) {
                        return DPICFunction.getRewardNames();
                    } else if (args[0].equalsIgnoreCase("removetotalreward")) {
                        return new ArrayList<>();
                    } else if (args[0].equalsIgnoreCase("open")) {
                        return DPICFunction.getCollectionNames();
                    } else if (args[0].equalsIgnoreCase("maxpage")) {
                        return DPICFunction.getCollectionNames();
                    } else if (args[0].equalsIgnoreCase("addcmdreward") || args[0].equalsIgnoreCase("removecmdreward")) {
                        return DPICFunction.getRewardNames();
                    }
                }
                if (args.length == 3) {
                    if (args[0].equalsIgnoreCase("items")) {
                        if (args[1].equalsIgnoreCase("collection")) {
                            return DPICFunction.getCollectionNames();
                        } else if (args[1].equalsIgnoreCase("reward")) {
                            return DPICFunction.getRewardNames();
                        }
                    } else if (args[0].equalsIgnoreCase("setcollectionreward")) {
                        if (DPICFunction.getCollectionNames().contains(args[1])) {
                            return DPICFunction.getRewardNames();
                        } else {
                            return new ArrayList<>();
                        }
                    } else if (args[0].equalsIgnoreCase("removecollectionreward")) {
                        if (DPICFunction.getCollectionNames().contains(args[1])) {
                            return DPICFunction.getCollectionRewardSteps(args[1]).stream().map(String::valueOf).collect(Collectors.toList());
                        } else {
                            return new ArrayList<>();
                        }
                    }
                }
                return null;
            });
        }
    }

    public CommandBuilder getBuilder() {
        return builder;
    }
}
