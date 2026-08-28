# Zonix

This project is a Minecraft practice plugin setup for legacy Bukkit/Spigot-based servers.
It contains a practice system with duels, parties, events, kits, arenas, bots, and
player statistics.

## Features

- Duels, match handling, queues, teams, spectators, and rematches.
- Party management, party selection menus, and team-based play.
- Configurable kits, kit editing, arena management, spawn locations, and warps.
- Practice events: Lights, One in the Chamber (OITC), Parkour, Red Rover, Sumo,
	TNT Tag, Water Drop, and Wool Mix Up.
- Free-for-all (FFA) mode with kill streak rewards.
- Training fights against bots, including bot movement and combat mechanics.
- PvP classes, including class selection menus and class-related event handling.
- Boards, timers, countdowns, match resets, and player status displays.
- Player profiles, statistics, leaderboards, settings, visibility controls, silent
	mode, inventory viewing, and flight controls.
- Tournament management, coin events, event hosting, event spectating, and event
	status commands.
- Region locking, configurable allowed continents, day/night/sunset controls, and
	server-side gameplay listeners.
- YAML configuration for arenas, kits, locations, and plugin settings.
- MongoDB-backed player data persistence and scheduled data saving.

## Dependencies

When you clone this repository, the required server and plugin JARs are already
placed in this folder so you can build and run it locally without needing to fetch
them elsewhere.

The project uses Maven and is configured for Java 8 source and target compatibility.
This repository includes updates intended to make the code usable with higher Java
versions, but compatibility still depends on the server software and its plugins.

The included dependencies are:

- WindSpigot 2.1.4 (Minecraft 1.8 / `v1_8_R3` server API and NMS).
- Citizens 2.0.25-SNAPSHOT for NPCs and training bots.
- WorldEdit 6.0 and FastAsyncWorldEdit (FAWE) API 21.03.09 for world editing.
- Gson 2.3.1, Lombok 1.18.10, and the MongoDB Java driver 3.12.14.

It is generally easier to use FAWE-reborn instead of the WorldEdit version currently in this folder, since FAWE-reborn is the more compatible and reliable option for modern setups, mainly because of later version of Java not being supported by WorldEdit.

## Notes

- This project is provided under the zero-liability, no-warranty license in
	[LICENSE](LICENSE).
- I do not claim ownership of this code. The original creator or rightsholder could
	not be identified from the available project history. Any original code belongs
	to its original creator or other applicable rightsholder.
- My contribution was limited to updating the code so it could be used with higher
	versions of Java. No ownership claim is made over the original work.
- Third-party dependencies remain subject to their own licenses and terms. Review
	those terms before redistributing a server or plugin bundle.

## Usage

Build the project with Maven and run it in your Minecraft server environment as
needed. Configure MongoDB in `src/main/resources/config.yml`, and configure kits,
arenas, and locations in their corresponding YAML files.

For modern setups, use FAWE-reborn in place of the bundled WorldEdit/FAWE files when
appropriate. The plugin is designed around the included WindSpigot 1.8 server API.
