package com.ieris19.discord.commands.implementations;

import com.ieris19.discord.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.awt.*;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static com.ieris19.discord.util.CommandUtils.errorEmbed;

/**
 * This class is used to handle the /info command<br>
 * A command that will return information about multiple aspects of the bot, server, users, etc.
 */
public class InfoCommand implements Command {

	/**
	 * Instantiates a new Info command.
	 */
	public InfoCommand() {
	}

	/**
	 * Execute the command
	 * @param event the command information in the form of event data
	 */
	@Override public void execute(SlashCommandInteractionEvent event) {
		MessageEmbed embed = switch (event.getCommandPath()) {
			case "info/bot" -> botEmbed();
			case "info/guild" -> guildEmbed(event);
			case "info/user" -> userEmbed(event);
			default -> errorEmbed();
		};
		event.replyEmbeds(embed).setEphemeral(true).queue();
	}

	/**
	 * Construct an embed with info about the bot and its purpose
	 * @return the embed that will be sent to the user
	 */
	private MessageEmbed botEmbed() {
		return new EmbedBuilder().setTitle("Ieris19's Assistant Bot").setColor(Color.YELLOW)
				.setThumbnail(Bot.getInstance().getBot().getSelfUser().getAvatarUrl())
				.setDescription("I am a bot designed to carry out a variety of tasks, mostly adding QoL options to " +
												"improve your Discord experience").build();
	}

	/**
	 * Construct an embed with info about the current guild
	 * @param event the command information in the form of event data
	 * @return the embed that will be sent to the user
	 */
	private MessageEmbed guildEmbed(SlashCommandInteractionEvent event) {
		Guild guild = event.getGuild();
		if (guild == null)
			return errorEmbed();
		Member owner = guild.retrieveOwner().complete();
		return new EmbedBuilder().setTitle(guild.getName()).setColor(Color.BLUE).setDescription(guild.getDescription())
				.setAuthor(owner.getUser().getName(), null, owner.getUser().getAvatarUrl())
				.setThumbnail(guild.getIconUrl())
				.setImage(guild.getBannerUrl())
				.addField("Guild Created",
									guild.getTimeCreated().atZoneSameInstant(ZoneId.of("Z"))
									.format(DateTimeFormatter.ofPattern("yyyy-MM-HH")), true)
				.addBlankField(true)
				.addField("Total Members", String.valueOf(guild.getMemberCount()), true).build();
	}

	/**
	 * Construct an embed with info about the user that invoked the command
	 * @param event the command information in the form of event data
	 * @return the embed that will be sent to the user
	 */
	private MessageEmbed userEmbed(SlashCommandInteractionEvent event) {
		MessageEmbed userEmbed;
		try {
			Member member = event.getOptions().get(0).getAsMember();
			userEmbed = new EmbedBuilder().setTitle(member.getUser().getName()).setColor(Color.GREEN)
					.setThumbnail(member.getUser().getAvatarUrl())
					.addField("User Joined Server",
										member.getTimeJoined().atZoneSameInstant(ZoneId.of("Z"))
										.format(DateTimeFormatter.ofPattern("yyyy-MM-HH")), true)
					.addBlankField(true)
					.addField("Boosting Server",
										member.isBoosting() ? "Boosting Server" : "Not Boosting Server", true).build();
		} catch (IndexOutOfBoundsException | NullPointerException exception) {
			return errorEmbed("The user was not provided or it is invalid, please, make sure input is correct" +
												" before trying to \"/report bug\". \nMake sure that the user is part of this guild too");
		}
		return userEmbed;
	}

	/**
	 * Get the data needed to register the command to the Discord API.<br>
	 * This method creates the following command and options: <br>
	 * <ul>
	 *   <li>info</li>
	 *   <li>info/guild</li>
	 *   <li>info/user</li>
	 * </ul>
	 *
	 * @return the command data as needed to be registered
	 */
	@Override public CommandData create() {
		CommandDataImpl infoCommand = new CommandDataImpl("info", "Get information from the bot");
		infoCommand.addSubcommands(new SubcommandData("guild", "Get information on the current guild"));
		infoCommand.addSubcommands(new SubcommandData("user", "Get the information for a specific user")
							 .addOption(OptionType.USER, "name","User whose info must be retrieved", true));
		infoCommand.addSubcommands(new SubcommandData("bot", "Get information on the bot"));
		return infoCommand;
	}
}
