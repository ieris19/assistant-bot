package com.ieris19.discord;

import com.ieris19.discord.util.Token;
import com.ieris19.lib.util.log.core.IerisLog;
import com.ieris19.lib.files.config.FileProperties;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.Command;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.List;

/**
 * Executable class that will reset the commands registered in all guilds
 */
public class CommandDeleter {

	/**
	 * Main method of the application
	 *
	 * @param args command line arguments that will be ignored
	 *
	 * @throws LoginException if the token is invalid or Discord refuses connection
	 * @throws InterruptedException if the thread is interrupted while waiting for the bot to be ready
	 * @throws IOException if the config file cannot be read/written to
	 */
	public static void main(String[] args) throws LoginException, InterruptedException, IOException {
		IerisLog.getInstance("IerisBotCleaner");
		String token = Token.get();
		IerisLog.getInstance().success("Imported Token");
		JDABuilder builder = JDABuilder.createLight(token);
		IerisLog.getInstance().success("Bot Initialized");
		JDA botInstance = builder.build();
		botInstance.awaitReady();
		try (FileProperties guildsProperties = FileProperties.getInstance("guilds")) {
			for (Guild guild : botInstance.getGuilds()) {
				if (guild != null) {
					IerisLog.getInstance().warning("Disabling guild " + guild.getName());
					resetCommands(guild);
					guild.leave().queue();
					guildsProperties.deleteProperty(guild.getName());
					IerisLog.getInstance().success("Successfully disabled guild");
				}
			}
		}
	}

	/**
	 * Resets the commands registered in the specified guild
	 *
	 * @param guild the guild to reset the commands in
	 */
	private static void resetCommands(Guild guild) {
		List<Command> commands = guild.retrieveCommands().complete();
		if (commands.isEmpty())
			return;
		for (Command command : commands) {
			IerisLog.getInstance().trace("Removing command: " + '/' + command.getName());
			guild.deleteCommandById(command.getId()).complete();
		}
	}
}
