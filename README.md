# KitPvPCore
A 1.8.9 KitPvP plugin

This is a backend plugin for a KitPvP server in 1.8.9. It uses the PlaceholdersAPI to convey additional information to users and to interoperate with other plugins.

Some features:

[A bounty system](https://github.com/dec4234/KitPvPCore/blob/main/src/main/java/net/dec4234/commands/BountyCommand.java) players can place bounties on other users using the /bounty command.

[A player stats manager](https://github.com/dec4234/KitPvPCore/blob/main/src/main/java/net/dec4234/files/PlayerStats.java) that manages information for each player such as the amount of kills, deaths, any active bounties, their current combat xp level and their placement on the leaderboard.

[A GUI system](https://github.com/dec4234/KitPvPCore/tree/main/src/main/java/net/dec4234/guis/framework) which allowed for building new GUIs very easily.

[A combat XP booster system](https://github.com/dec4234/KitPvPCore/blob/main/src/main/java/net/dec4234/guis/content/boosters/BoosterHandling.java) which allowed users to get short boosts to the amount of Combat XP that they earned.

[An upgrades system](https://github.com/dec4234/KitPvPCore/blob/main/src/main/java/net/dec4234/guis/content/upgrades/UpgradesRolling.java) which allowed users to get certain effects when they got a kill such as regeneration, speed, etc. This also gives them the appropriate amount of Combat XP.

[A customizable config](https://github.com/dec4234/KitPvPCore/blob/main/src/main/resources/config.yml) which allowed the owner to change kill feed information. Supports PlaceholdersAPI.

Among other things
