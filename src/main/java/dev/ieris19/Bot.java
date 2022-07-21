package dev.ieris19;

import dev.ieris19.commands.ServerCommands;
import dev.ieris19.util.Token;
import lib.ieris19.util.log.Log;
import lib.ieris19.util.properties.FileProperties;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import javax.security.auth.login.LoginException;
import java.util.Arrays;

public class Bot {
	private static final Bot instance;

	private JDA botInstance;

	static {
		instance = new Bot(Token.get());
	}

	public static Bot getInstance() {
		return instance;
	}

	private Bot(String token) {
		JDABuilder builder = JDABuilder.createDefault(token);
		Log.getInstance().success("Imported Token");
		configure(builder);
		try {
			botInstance = builder.build();
			Log.getInstance().success("Build initialized");
		} catch (LoginException e) {
			Log.getInstance().error("Fatal error during build. Shutting down" + Arrays.toString(e.getStackTrace()));
			System.exit(120);
		}
		try {
			botInstance.awaitReady();
		} catch (InterruptedException e) {
			Log.getInstance().error("Bot thread interrupted while not ready");
			throw new RuntimeException(e);
		}
		createCommands();
		Log.getInstance().success("Initialization of the bot complete");
	}

	private void configure(JDABuilder botBuilder) {
		botBuilder.setActivity(Activity.watching("this server"));
		addListeners(botBuilder);
	}

	private void addListeners(JDABuilder botBuilder) {
		botBuilder.addEventListeners(new ServerCommands());
	}

	private void createCommands() {
		CommandDataImpl reportCommand = new CommandDataImpl("report", "Get information about reporting errors");
		CommandDataImpl minecraftServerIPCommand = new CommandDataImpl("minecraft",
																																	 "Get current information about the server");
		Guild devGuild = botInstance.getGuildById(FileProperties.getInstance("sensitive").getProperty("dev-guild"));
		Guild solarWard = botInstance.getGuildById(FileProperties.getInstance("sensitive").getProperty("solarward"));
		if (devGuild != null) {
			devGuild.upsertCommand(reportCommand).queue();
			devGuild.upsertCommand(minecraftServerIPCommand).queue();
		}
		if (solarWard != null) {
			solarWard.upsertCommand(reportCommand).queue();
			solarWard.upsertCommand(minecraftServerIPCommand).queue();
		}
	}
}
