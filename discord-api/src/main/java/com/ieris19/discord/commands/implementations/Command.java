package com.ieris19.discord.commands.implementations;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

/**
 * The command interface represents an object that will listen to a
 * {@link net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction Slash Command} and act accordingly.
 */
public interface Command {
	/**
	 * Execute the command
	 *
	 * @param event the command information in the form of event data
	 */
	void execute(SlashCommandInteractionEvent event);
	/**
	 * Get the data needed to register the command to the Discord API.<br> This method should create everything the
	 * command needs to be registered, including the name, description, options, etc.
	 *
	 * @return the command data as needed to be registered
	 */
	CommandData create();
}
