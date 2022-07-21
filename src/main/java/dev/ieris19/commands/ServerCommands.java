package dev.ieris19.commands;

import dev.ieris19.util.logging.LogUtils;
import lib.ieris19.util.log.Log;
import lib.ieris19.util.properties.DynamicProperties;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class ServerCommands extends ListenerAdapter {
	MessageEmbed errorEmbed = new EmbedBuilder().setColor(Color.RED).setTitle("Error")
			.setDescription("There has been a problem running this command. Please report this to the developer on Github at"
											+ "Ieris19/Assistant-Bot or by running \"/report\"").build();

	@Override public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		switch (event.getName()) {
			case "report" -> {
					MessageEmbed reportEmbed = new EmbedBuilder().setTitle("Report Errors",
																													 "https://github.com/Ieris19/assistant-bot")
							.setColor(Color.RED).setDescription("You can deal with any issue in multiple ways on Github")
							.addField(new MessageEmbed.Field("Pull Request", "Dust off your coding skills and solve it yourself,"
																						 + " then create a pull request so that I can implement your code", false))
							.addField(new MessageEmbed.Field("Create an Issue", "Create an issue on GitHub and let my creator deal "
																						 + "with it. You can also request features here", false))
							.build();
					event.replyEmbeds(reportEmbed).queue();
			}
			case "minecraft" -> {
				DynamicProperties minecraftProperties = new DynamicProperties("minecraft");
				try {
					EmbedBuilder embed = new EmbedBuilder();
					event.replyEmbeds(embed
					.setTitle("Server Info")
					.setColor(Color.YELLOW)
					.setDescription("The current server is a " + minecraftProperties.getProperty("instance-modpack") + " instance")
					.addField("Map:", "Currently adventuring in " + minecraftProperties.getProperty("map"), true)
					.addField("ServerIP", publicServerMachineIP() + ":" + minecraftProperties.getProperty("server-port"), true)
					.addField("Difficulty:", minecraftProperties.getProperty("difficulty"), true)
					.addField("Status", "The server is currently: " + minecraftProperties.getProperty("status"), false)
					.build()).queue();
				} catch (RuntimeException e) {
					event.replyEmbeds(errorEmbed).queue();
					Log.getInstance().error("Error fetching Minecraft Server IP");
					e.printStackTrace();
				}
				Log.getInstance().log(LogUtils.commandLog(event));
			}
			default -> event.replyEmbeds(errorEmbed).queue();
		}
	}

	public String publicServerMachineIP() {
		String publicAddress;
		try {
			URL websiteAddress = new URL("https://checkip.amazonaws.com");
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(websiteAddress.openStream()))) {
				publicAddress = reader.readLine().trim();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
		return publicAddress;
	}
}
