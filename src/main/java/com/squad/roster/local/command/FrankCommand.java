package com.squad.roster.local.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class FrankCommand implements Command {

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.reply("UHH FRANK").queue();
    }
}
