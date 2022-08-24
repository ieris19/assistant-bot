package dev.ieris19.commands;

import dev.ieris19.util.LogUtils;
import lib.ieris19.util.log.Log;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static dev.ieris19.util.CommandUtils.errorEmbed;
import static dev.ieris19.util.CommandUtils.getCommandList;

public class CommandListener extends ListenerAdapter {
	@Override public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		switch (event.getCommandPath()) {
			case "report", "report/bug", "report/users" -> getCommandList().get("report").execute(event);
			case "minecraft" -> getCommandList().get("minecraft").execute(event);
			default -> event.replyEmbeds(errorEmbed()).queue();
		}
		Log.getInstance().log(LogUtils.commandLog(event));
	}
}
