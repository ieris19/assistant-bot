package com.ieris19.discord.commands;

import com.ieris19.discord.util.CommandUtils;
import com.ieris19.discord.util.LogUtils;
import com.ieris19.discord.commands.implementations.Command;
import com.ieris19.lib.util.log.Log;
import com.ieris19.lib.util.log.core.IerisLog;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static com.ieris19.discord.util.CommandUtils.errorEmbed;

/**
 * The command listener class is a forwarder that will listen to all slash commands and forward them to the correct
 * class according to the command path
 */
public class CommandListener extends ListenerAdapter {
	/**
	 * Action that will occur when a slash command is sent to the bot. This method should receive said event and forward
	 * it to the appropriate class
	 *
	 * @param event the Slash Command event that has been received from the DiscordAPI
	 */
	@Override public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		Command command = CommandLoader.getInstance().getCommandList().get(event.getFullCommandName().split(" ")[0]);
		if (command != null) {
			command.execute(event);
		} else {
			event.replyEmbeds(CommandUtils.errorEmbed("The command received by the bot cannot be found or the bot" +
																								" had trouble parsing it")).queue();
		}
		IerisLog.getInstance().trace(LogUtils.commandLog(event));
	}
}
