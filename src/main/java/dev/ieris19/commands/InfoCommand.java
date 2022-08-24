package dev.ieris19.commands;

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

import static dev.ieris19.util.CommandUtils.errorEmbed;

public class InfoCommand implements Command {

	@Override public void execute(SlashCommandInteractionEvent event) {
		MessageEmbed embed = switch (event.getCommandPath()) {
			case "info" -> helpEmbed();
			case "info/guild" -> guildEmbed(event);
			case "info/user" -> userEmbed(event);
			default -> errorEmbed();
		};
		event.replyEmbeds(embed).setEphemeral(true).queue();
	}

	private MessageEmbed helpEmbed() {
		return new EmbedBuilder().setTitle("Ieris19's Assistant Bot").setColor(Color.YELLOW)
				.setDescription("I am a bot designed to carry out a variety of tasks, mostly improving your options as " +
												"to how you can use Discord").build();
	}

	private MessageEmbed guildEmbed(SlashCommandInteractionEvent event) {
		Guild guild = event.getGuild();
		if (guild == null)
			return errorEmbed();
		return new EmbedBuilder().setTitle(guild.getName()).setColor(Color.BLUE).setDescription(guild.getDescription())
				.setAuthor(guild.getOwner().getUser().getName(), guild.getVanityUrl(),
									 guild.getOwner().getAvatarUrl()).setThumbnail(guild.getIconUrl())
				.setImage(guild.getBannerUrl())
				.addField("Guild Created",
									guild.getTimeCreated().atZoneSameInstant(ZoneId.of("Z"))
									.format(DateTimeFormatter.ofPattern("yyyy-MM-HH")), true)
				.addBlankField(true)
				.addField("Total Members", String.valueOf(guild.getMemberCount()), true).build();
	}

	private MessageEmbed userEmbed(SlashCommandInteractionEvent event) {
		try {
			Member member = event.getOptions().get(0).getAsMember();
		} catch (IndexOutOfBoundsException | NullPointerException exception) {
			return errorEmbed("The user was not provided or it is invalid, please, make sure input is correct" +
												" before trying to \"/report bug\"");
		}
		return errorEmbed("This command hasn't been fully implemented yet");
	}

	@Override public CommandData create() {
		CommandDataImpl infoCommand = new CommandDataImpl("info", "Get help about the bot");
		infoCommand.addSubcommands(new SubcommandData("guild", "Get information on the current guild"));
		infoCommand.addSubcommands(new SubcommandData("user", "Get the information for a specific user")
							 .addOption(OptionType.USER, "name","User whose info must be retrieved", true));
		return infoCommand;
	}
}
