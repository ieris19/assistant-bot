package dev.ieris19.util.logging;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class LogUtils {
	public static String commandLog(SlashCommandInteractionEvent event) {
		return event.getName() + " command has been issued by " + event.getMember() + " in " + event.getGuild() + " at "
					 + event.getMessageChannel();
	}
}
