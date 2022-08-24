package dev.ieris19.util;

import dev.ieris19.commands.Command;
import dev.ieris19.commands.InfoCommand;
import dev.ieris19.commands.MinecraftCommand;
import dev.ieris19.commands.ReportCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class CommandUtils {
	private static final HashMap<String, Command> commandList = new HashMap<>();
	public static MessageEmbed errorEmbed(@NotNull String customMessage) {
		if (!customMessage.equals(""))
		return new EmbedBuilder().setColor(Color.RED).setTitle("Something went wrong!")
				.setDescription(customMessage).build();
		else
			return errorEmbed();
	}

	public static MessageEmbed errorEmbed() {
		return new EmbedBuilder().setColor(Color.RED).setTitle("Something went wrong!")
				.setDescription("There has been a problem running this command. Please report this to the developer on " +
												"Github at Ieris19/Assistant-Bot or by running \"/report\"").build();
	}

	static {
		commandList.put("report", new ReportCommand());
		commandList.put("minecraft", new MinecraftCommand());
		commandList.put("info", new InfoCommand());
	}

	public static String publicServerMachineIP() {
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

	public static HashMap<String, Command> getCommandList() {
		return commandList;
	}
}
