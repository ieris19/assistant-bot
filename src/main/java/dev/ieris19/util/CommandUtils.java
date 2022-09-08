package dev.ieris19.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * A class containing miscellaneous utilities to be used by commands or related to commands
 */
public class CommandUtils {
	/**
	 * Constructs an error embed with a custom message for the user. The title will always be "Something went wrong!"
	 *
	 * @param customMessage the description of the embed
	 *
	 * @return an embed message telling the user about the error
	 */
	public static MessageEmbed errorEmbed(@NotNull String customMessage) {
		if (!customMessage.trim().equals(""))
			return new EmbedBuilder().setColor(Color.RED).setTitle("Something went wrong!").setDescription(customMessage)
					.build();
		else
			return errorEmbed();
	}

	/**
	 * Creates a generic error embed telling the user that the command run into issues and that they should report to the
	 * developer
	 *
	 * @return a generic error embed message
	 */
	public static MessageEmbed errorEmbed() {
		return errorEmbed("There has been a problem running this command. Please report this to the developer on " +
											"Github at Ieris19/Assistant-Bot or by running \"/report\"");
	}

	/**
	 * Fetches the public IP address by querying <a href="https://checkip.amazonaws.com">AWS</a>
	 *
	 * @return the public IP address of the machine hosting the bot
	 */
	public static String publicServerMachineIP() throws IOException {
		String publicAddress;
		URL websiteAddress = new URL("https://checkip.amazonaws.com");
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(websiteAddress.openStream()))) {
			publicAddress = reader.readLine().trim();
			return publicAddress;
		}
	}
}
