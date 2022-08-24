package dev.ieris19;

import dev.ieris19.commands.Command;
import dev.ieris19.commands.CommandListener;
import dev.ieris19.util.CommandUtils;
import dev.ieris19.util.Token;
import lib.ieris19.util.log.Log;
import lib.ieris19.util.properties.FileProperties;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;

import javax.security.auth.login.LoginException;
import java.util.Arrays;
import java.util.List;

public class Bot {
	private static Bot instance;

	private JDA botInstance;

	static {
		instance = new Bot();
	}

	public static Bot getInstance() {
		if (instance == null) {
			instance = new Bot();
		}
		return instance;
	}

	private Bot() {
		String token = Token.get();
		JDABuilder builder = JDABuilder.createDefault(token);
		Log.getInstance().success("Imported Token");
		configure(builder);
		try {
			botInstance = builder.build();
			Log.getInstance().success("Build initialized");
			botInstance.awaitReady();
		} catch (LoginException e) {
			Log.getInstance().error("Fatal error during build. Shutting down" + Arrays.toString(e.getStackTrace()));
			System.exit(120);
		} catch (InterruptedException e) {
			Log.getInstance().error("Bot thread interrupted while not ready");
			throw new RuntimeException(e);
		}
		guildSetup();
		Log.getInstance().success("Initialization of the bot complete");
	}

	private void guildSetup() {
		List<Guild> instanceGuilds = botInstance.getGuilds();
		Guild[] disabledGuilds = {
				botInstance.getGuildById(FileProperties.getInstance("guilds").getProperty("solar-ward"))
		};
		for (Guild guild : disabledGuilds) {
			if (guild != null) {
				Log.getInstance().warning("Disabling guild " + guild.getName());
				resetCommands(guild);
				instanceGuilds.remove(guild);
				Log.getInstance().warning("Successfully disabled guild");
			}
		}
		for (Guild guild : instanceGuilds) {
			Log.getInstance().info("Detected guild: " + guild.getName());
			insertCommands(guild);
			Log.getInstance().success("Joined guild " + guild.getName());
		};
	}

	private void configure(JDABuilder botBuilder) {
		botBuilder.setActivity(Activity.watching("this server"));
		addListeners(botBuilder);
	}

	private void addListeners(JDABuilder botBuilder) {
		botBuilder.addEventListeners(new CommandListener());
	}

	private void insertCommands(Guild guild) {
		if (guild != null) {
			for (String commandName : CommandUtils.getCommandList().keySet()) {
				Command command = CommandUtils.getCommandList().get(commandName);
				if (command != null) {
					guild.upsertCommand(command.create()).queue();
				} else {
					Log.getInstance().error("Command " + commandName + " is null");
				}
			}
		}
	}

	private void resetCommands(Guild guild) {
		List<net.dv8tion.jda.api.interactions.commands.Command> commands = guild.retrieveCommands().complete();
		if (commands.isEmpty())
			return;
		for (net.dv8tion.jda.api.interactions.commands.Command command : commands) {
			botInstance.deleteCommandById(command.getId()).complete();
		}
	}
}