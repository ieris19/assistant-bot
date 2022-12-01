package com.ieris19.discord.commands.implementations;

import com.ieris19.discord.util.CommandUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.awt.*;

/**
 * This class is used to create the /report command.<br>
 * This command is used to report a bug or a user.
 */
public class ReportCommand implements Command {
	/**
	 * Instantiates a new Report command.
	 */
	public ReportCommand() {
	}
	/**
	 * Execute the command.
	 * @param event the command information in the form of event data
	 */
	@Override public void execute(SlashCommandInteractionEvent event) {
		MessageEmbed reply = switch (event.getCommandPath()) {
			case "report/bug" -> bugEmbed();
			case "report/user" -> userEmbed();
			case "report" -> infoEmbed();
			default -> CommandUtils.errorEmbed();
		};
		event.replyEmbeds(reply).setEphemeral(true).queue();
	}

	/**
	 * Construct an embed with info about how to report user abuse
	 *
	 * @return the embed that will be sent to the user
	 */
	private MessageEmbed userEmbed() {
		return new EmbedBuilder().setColor(Color.PINK)
				.setTitle("User Report")
				.setDescription("Please report any user abuse to the developer on Github at Ieris19/Assistant-Bot").build();
	}

	/**
	 * Construct an embed with info about how to use the report command
	 *
	 * @return the embed that will be sent to the user
	 */
	public MessageEmbed bugEmbed() {
		return new EmbedBuilder().setTitle("Report Errors", "https://github.com/Ieris19/assistant-bot")
				.setColor(Color.RED)
				.setDescription("You can deal with any issue in multiple ways on Github")
				.addField(new MessageEmbed.Field("Pull Request",
																	 				"Dust off your coding skills and solve it yourself, then create a pull " +
																								"request so that I can implement your code", false))
				.addField(new MessageEmbed.Field("Create an Issue",
																				 "Create an issue on GitHub and let my creator deal with it. " +
																							 "You can also request features here",false)).build();
	}

	/**
	 * Construct an embed with info about how to use the report command
	 *
	 * @return the embed that will be sent to the user
	 */
	private MessageEmbed infoEmbed() {
		return new EmbedBuilder().setColor(Color.ORANGE)
				.setTitle("Report Command")
				.setDescription("Please choose one of the subcommands in order to specify the nature of the report")
				.addField(new MessageEmbed.Field("/report bug",
																				 "Use this command to get information on how to report a bug", true))
				.addField(new MessageEmbed.Field("/report user",
																				 "Use this command to let the admins know about user abuse", true))
				.build();
	}

	/**
	 * Get the data needed to register the command to the Discord API.<br>
	 * This method creates the following command and options: <br>
	 * <ul>
	 *   <li>report/bug</li>
	 *   <li>report/user</li>
	 * </ul>
	 *
	 * @return the command data as needed to be registered
	 */
	public CommandData create() {
		CommandDataImpl reportCommand = new CommandDataImpl("report", "Communicate with the administrators");
		reportCommand.addSubcommands(new SubcommandData("bug", "Report a bug"));
		reportCommand.addSubcommands(new SubcommandData("user", "Report a user")
								 .addOption(OptionType.USER, "user", "The user to report", true));
		return reportCommand;
	}
}
