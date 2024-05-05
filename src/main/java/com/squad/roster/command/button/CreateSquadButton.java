package com.squad.roster.command.button;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class CreateSquadButton implements ButtonCommand {

    @Override
    public void execute(ButtonInteractionEvent event) {
        event.reply("To create a squad, make use of the `/create-squad` command")
                .setEphemeral(true)
                .queue();
    }
}