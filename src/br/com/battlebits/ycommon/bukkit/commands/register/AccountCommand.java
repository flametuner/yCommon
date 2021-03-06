package br.com.battlebits.ycommon.bukkit.commands.register;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import br.com.battlebits.ycommon.bukkit.accounts.BukkitPlayer;
import br.com.battlebits.ycommon.bukkit.commands.BukkitCommandFramework.Command;
import br.com.battlebits.ycommon.bukkit.commands.BukkitCommandFramework.CommandArgs;
import br.com.battlebits.ycommon.bukkit.commands.BukkitCommandFramework.Completer;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.commandmanager.CommandClass;
import br.com.battlebits.ycommon.common.tag.Tag;
import br.com.battlebits.ycommon.common.translate.Translate;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class AccountCommand extends CommandClass {

	@Command(name = "account", aliases = "acc")
	public void account(CommandArgs args) {

	}

	@Command(name = "tag", runAsync = true)
	public void tag(CommandArgs cmdArgs) {
		if (cmdArgs.isPlayer()) {
			Player p = cmdArgs.getPlayer();
			String[] args = cmdArgs.getArgs();
			BukkitPlayer player = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
			String prefix = Translate.getTranslation(player.getLanguage(), "command-tag-prefix") + " ";
			if (args.length == 0) {
				int max = player.getTags().size() * 2;
				TextComponent[] message = new TextComponent[max];
				message[0] = new TextComponent(prefix + Translate.getTranslation(player.getLanguage(), "command-tag-available") + " ");
				int i = max - 1;
				for (Tag t : player.getTags()) {
					if (i < max - 1) {
						message[i] = new TextComponent("�f, ");
						i -= 1;
					}
					TextComponent component = new TextComponent((t == Tag.NORMAL) ? "�7�lNORMAL" : t.getPrefix(player.getLanguage()));
					component.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new TextComponent[] { new TextComponent(Translate.getTranslation(player.getLanguage(), "command-tag-click-select")) }));
					component.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, "/tag " + t.name()));
					message[i] = component;
					i -= 1;
					component = null;
				}
				p.spigot().sendMessage(message);
				message = null;
			} else {
				Tag tag = null;
				try {
					tag = Tag.getTag(args[0].toUpperCase(), player.getLanguage());
				} catch (Exception ex) {
					p.sendMessage(prefix + Translate.getTranslation(player.getLanguage(), "command-tag-not-found"));
					return;
				}
				if (tag != null) {
					if (player.getTags().contains(tag)) {
						if (player.getTag() != tag) {
							if (player.setTag(tag)) {
								p.sendMessage(prefix + Translate.getTranslation(player.getLanguage(), "command-tag-selected").replace("%tag%", ((tag == Tag.NORMAL) ? "�7�lNORMAL" : tag.getPrefix(player.getLanguage()))));
							}
						} else {
							p.sendMessage(prefix + Translate.getTranslation(player.getLanguage(), "command-tag-current"));
						}
					} else {
						p.sendMessage(prefix + Translate.getTranslation(player.getLanguage(), "command-tag-no-access"));
					}
					tag = null;
				} else {
					p.sendMessage(prefix + Translate.getTranslation(player.getLanguage(), "command-tag-not-found"));
				}
			}
			prefix = null;
			player = null;
			args = null;
			p = null;
		} else {
			cmdArgs.getSender().sendMessage("�4�lERRO �fComando disponivel apenas �c�lin-game");
		}
	}

	@Completer(name = "tag")
	public List<String> tagCompleter(CommandArgs cmdArgs) {
		if (cmdArgs.isPlayer()) {
			ArrayList<String> tags = new ArrayList<>();
			String[] args = cmdArgs.getArgs();
			if (args.length == 1) {
				Player p = cmdArgs.getPlayer();
				BukkitPlayer player = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
				String s = args[0].toUpperCase();
				for (Tag t : player.getTags()) {
					if (t.name().startsWith(s)) {
						tags.add(t.name());
					} else if (ChatColor.stripColor(t.getPrefix(player.getLanguage())).startsWith(s)) {
						tags.add(t.name());
					}
				}
				player = null;
				p = null;
			}
			args = null;
			return tags;
		} else {
			return new ArrayList<>();
		}
	}

}
