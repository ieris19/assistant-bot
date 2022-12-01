package com.ieris19.discord.commands.implementations;

import com.ieris19.discord.util.CommandUtils;
import com.ieris19.lib.util.log.Log;
import com.ieris19.lib.util.properties.DynamicProperties;
import com.ieris19.lib.util.properties.FileProperties;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.awt.*;
import java.io.IOException;
import java.util.Set;

/**
 * This class is used to handle the /minecraft command<br>
 * A command that will return information about the Minecraft server
 */
public class MinecraftCommand implements Command {
	/**
	 * Instantiates a new Minecraft command.
	 */
	public MinecraftCommand() {
	}
	/**
	 * Execute the command
	 * @param event the command information in the form of event data
	 */
	@Override public void execute(SlashCommandInteractionEvent event) {
		MessageEmbed embed = switch (event.getCommandPath()) {
			case "minecraft" -> serverFetch(event);
			default -> CommandUtils.errorEmbed();
		};
		event.replyEmbeds(embed).queue();
	}

	/**
	 * Fetch the server information and provide it in the form of an embed message
	 * @param event the command information in the form of event data
	 * @return the embed message containing the server information
	 */
	private MessageEmbed serverFetch(SlashCommandInteractionEvent event) {
		try (DynamicProperties minecraftProperties = new DynamicProperties("minecraft")) {
			EmbedBuilder embed = new EmbedBuilder();
			embed.setTitle("Server Info").setColor(Color.YELLOW).setDescription(
							"The current server is a " + minecraftProperties.getProperty("instance") + " instance")
														.addField("Map:", "Currently adventuring in " +
																							minecraftProperties.getProperty("map"),
																			true)
														.addField("ServerIP", CommandUtils.publicServerMachineIP() + ":" +
																									minecraftProperties.getProperty("server-port"),
																			true)
														.addField("Difficulty:", minecraftProperties.getProperty("difficulty"), true)
														.addField("Status",
																			"The server is currently: " + minecraftProperties.getProperty("status"),
																			false);
			return embed.build();
		} catch (IOException e) {
			event.replyEmbeds(CommandUtils.errorEmbed()).queue();
			Log.getInstance().error("Error fetching Minecraft Server IP");
			e.printStackTrace();
		}
		return CommandUtils.errorEmbed();
	}

	/**
	 * Get the data needed to register the command to the Discord API.<br>
 	 * This method creates the following command and options: <br>
 	 * <ul>
 	 *   <li>minecraft</li>
 	 * </ul>
 	 *
	 * @return the command data as needed to be registered
	 */
	@Override public CommandData create() {
		try (FileProperties minecraft = FileProperties.getInstance("minecraft")) {
			Set<Object> keySet = minecraft.getProperties().keySet();
			if (keySet.isEmpty())
				throw new IllegalStateException("Minecraft properties file is empty");
			for (Object setKey : keySet) {
				String key = (String) setKey;
				if (minecraft.getProperty(key) == null) {
					throw new IllegalStateException("A property is missing from the minecraft.properties file");
				}
			}
		} catch (IOException | IllegalStateException | IllegalArgumentException e) {
			Log.getInstance().error("Problems encountered with: minecraft.properties " + e.getMessage());
			Log.getInstance().info("Recreating minecraft.properties file, please fill in the missing values");
			try (FileProperties minecraft = FileProperties.getInstance("minecraft")) {
				minecraft.getProperties().keySet().forEach(key -> minecraft.deleteProperty((String) key));
				minecraft.createProperty("server-port", "25565");
				minecraft.createProperty("status", "OFFLINE");
				minecraft.createProperty("instance", "");
				minecraft.createProperty("map", "");
				minecraft.createProperty("difficulty", "");
			} catch (IOException ioException) {
				ioException.printStackTrace();
				Log.getInstance().error("Error creating minecraft.properties file");
			}
		}
		return new CommandDataImpl("minecraft", "Get information about the server");
	}
}
