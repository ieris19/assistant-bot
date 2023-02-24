package com.ieris19.discord;

import com.ieris19.discord.commands.CommandListener;
import com.ieris19.discord.commands.CommandLoader;
import com.ieris19.discord.commands.implementations.Command;
import com.ieris19.discord.util.PropertyUtils;
import com.ieris19.discord.util.Token;
import com.ieris19.lib.files.config.FileProperties;
import com.ieris19.lib.util.log.core.IerisLog;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Main class of the Discord Bot runtime
 */
public class Bot {
	/**
	 * The singleton instance of this class
	 */
	private static Bot instance;

	/**
	 * The Java Discord API instance
	 */
	private JDA botInstance;

	/**
	 * Returns the singleton instance of this class
	 */
	public static Bot getInstance() {
		if (instance == null) {
			instance = new Bot();
		}
		return instance;
	}

	/**
	 * The entry point of the bot
	 */
	public static void start() {
		getInstance();
	}

	/**
	 * Returns the JDA instance
	 */
	public JDA getBot() {
		return getInstance().botInstance;
	}

	/**
	 * Instantiates the bot and starts its runtime
	 */
	private Bot() {
		String token = Token.get();
		JDABuilder builder = JDABuilder.createLight(token);
		IerisLog.getInstance().success("Imported Token");
		configure(builder);
		try {
			botInstance = builder.build();
			IerisLog.getInstance().success("Discord Bot Build completed");
			botInstance.awaitReady();
		} catch (InvalidTokenException e) {
			IerisLog.getInstance().error("Fatal error during build. Shutting down" + Arrays.toString(e.getStackTrace()));
			System.exit(120);
		} catch (InterruptedException e) {
			IerisLog.getInstance().error("Bot thread interrupted while not ready");
			throw new RuntimeException(e);
		}
		guildSetup();
		IerisLog.getInstance().success("Initialization of the bot complete");
	}

	/**
	 * Configures the bot guilds
	 */
	private void guildSetup() {
		try (FileProperties guildProperties = FileProperties.getInstance("guilds")) {
			List<Guild> instanceGuilds = botInstance.getGuilds();

			for (Guild guild : instanceGuilds) {
				IerisLog.getInstance().info("Detected guild: " + guild.getName());
				try {
					String guildKey = PropertyUtils.guildPropertyName(guild);
					guildProperties.createProperty(guildKey, guild.getId());
					IerisLog.getInstance().info("Added guild to list of guilds");
				} catch (IllegalArgumentException exception) {
					IerisLog.getInstance().info("Guild already initialized");
				}
				insertCommands(guild);
				IerisLog.getInstance().success("Joined guild " + guild.getName());
			}
		} catch (IOException e) {
			IerisLog.getInstance().error("There has been an I/O error while initializing the guild properties");
		} finally {
			IerisLog.getInstance().success("Guild initialization finished");
		}
	}

	/**
	 * Sets the bot's configuration
	 *
	 * @param botBuilder the builder to configure
	 */
	private void configure(JDABuilder botBuilder) {
		botBuilder.setActivity(Activity.watching("this server"));
		addListeners(botBuilder);
	}

	/**
	 * Adds the listeners to the bot
	 *
	 * @param botBuilder the builder to add the listeners to
	 */
	private void addListeners(JDABuilder botBuilder) {
		botBuilder.addEventListeners(new CommandListener());
	}

	/**
	 * Inserts the commands into the guild
	 *
	 * @param guild the guild to insert the commands into
	 */
	private void insertCommands(Guild guild) {
		if (guild != null) {
			for (String commandName : CommandLoader.getInstance().getCommandList().keySet()) {
				Command command = CommandLoader.getInstance().getCommandList().get(commandName);
				if (command != null) {
					IerisLog.getInstance().trace("Inserting command /" + commandName + " into guild");
					guild.upsertCommand(command.create()).queue();
				} else {
					IerisLog.getInstance().error("Command " + commandName + " is null");
				}
			}
		}
	}
}