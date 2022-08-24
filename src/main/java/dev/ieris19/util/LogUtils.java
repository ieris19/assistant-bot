package dev.ieris19.util;

import lib.ieris19.util.log.Log;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.internal.interactions.InteractionHookImpl;

import java.util.function.Consumer;

public class LogUtils {
	public static String commandLog(SlashCommandInteractionEvent command) {
		return command.getName() + " command has been issued by \"" + command.getMember().getUser().getName() +
					 "\" in \"" + command.getGuild().getName() + "\" at \"" + command.getMessageChannel().getName() + "\"";
	}
}
