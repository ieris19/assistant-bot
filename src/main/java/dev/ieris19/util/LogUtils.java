package dev.ieris19.util;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;

/**
 * A class containing miscellaneous utilities to be used by the logger or related to logging in the bot
 */
public class LogUtils {
	/**
	 * Logs the command interaction event
	 *
	 * @param event the event to be logged
	 */
	public static String commandLog(SlashCommandInteractionEvent event) {
		CommandInfo command = new CommandInfo(event);
		return command.name() + " command has been issued by " + command.member() + " in " +
					 command.guild() + " on channel " + command.channel();
	}

	/**
	 * Contains information about the command interaction event used to instantiate this object
	 */
	private static class CommandInfo {
		/**
		 * The event represented by this object
		 */
		private final CommandInteraction command;

		/**
		 * Constructs a new CommandInfo object
		 *
		 * @param event the event to be represented by this object
		 */
		private CommandInfo(SlashCommandInteractionEvent event) {
			this.command = event;
		}

		/**
		 * Returns the name of the command
		 *
		 * @return the name of the command
		 */
		private String name() {
			return '/' + command.getCommandPath();
		}

		/**
		 * Returns the name of the guild where the command was issued
		 *
		 * @return the name of the guild where the command was issued
		 */
		private String guild() {
			return '"' + command.getGuild().getName() + '"';
		}

		/**
		 * Returns the name of the member who issued the command
		 *
		 * @return the name of the member who issued the command
		 */
		private String member() {
			return '@' + this.command.getMember().getUser().getName();
		}

		/**
		 * Returns the name of the channel where the command was issued
		 *
		 * @return the name of the channel where the command was issued
		 */
		private String channel() {
			return '#' + this.command.getMessageChannel().getName();
		}
	}
}
