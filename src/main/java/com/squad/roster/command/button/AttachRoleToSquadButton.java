package com.squad.roster.command.button;

import com.squad.roster.EventConstants;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

public class AttachRoleToSquadButton implements ButtonCommand {
    @Override
    public void execute(ButtonInteractionEvent event) {
        event.reply("Please mention the role you would like to attach to the squad")
                .setEphemeral(true)
                .addActionRow(
                        StringSelectMenu.create(EventConstants.CREATE_SQUAD_BUTTON)
                                .build()
                )
                .queue();
    }
}
