package dev.ieris19.commands;

import dev.ieris19.util.CommandUtils;
import dev.ieris19.util.LogUtils;
import lib.ieris19.util.log.Log;
import lib.ieris19.util.properties.DynamicProperties;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.awt.*;

import static dev.ieris19.util.CommandUtils.errorEmbed;

public class MinecraftCommand implements Command {
	@Override public void execute(SlashCommandInteractionEvent event) {
		DynamicProperties minecraftProperties = new DynamicProperties("minecraft");
		try {
			EmbedBuilder embed = new EmbedBuilder();
			event.replyEmbeds(embed.setTitle("Server Info").setColor(Color.YELLOW).setDescription(
							"The current server is a " + minecraftProperties.getProperty("instance-modpack") + " instance")
														.addField("Map:", "Currently adventuring in " +
																							minecraftProperties.getProperty("map"),
																			true)
														.addField("ServerIP", CommandUtils.publicServerMachineIP() + ":" +
																									minecraftProperties.getProperty("server-port"),
																			true)
														.addField("Difficulty:", minecraftProperties.getProperty("difficulty"), true)
														.addField("Status",
																			"The server is currently: " + minecraftProperties.getProperty("status"),
																			false)
														.build()).queue();
		} catch (RuntimeException e) {
			event.replyEmbeds(errorEmbed()).queue();
			Log.getInstance().error("Error fetching Minecraft Server IP");
			e.printStackTrace();
		}
	}

	@Override public CommandData create() {
		return new CommandDataImpl("minecraft", "Get information about the server");
	}
}
