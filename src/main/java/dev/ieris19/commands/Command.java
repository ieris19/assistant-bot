package dev.ieris19.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public interface Command {
	void execute(SlashCommandInteractionEvent event);
	CommandData create();
}
