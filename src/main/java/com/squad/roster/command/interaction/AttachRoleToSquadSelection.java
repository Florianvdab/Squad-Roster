package com.squad.roster.command.interaction;

import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;

public class AttachRoleToSquadSelection implements EntitySelectInteraction {
    @Override
    public void execute(EntitySelectInteractionEvent event) {
        event.reply("Role selected " + event.getMentions().getRoles()).queue();
    }
}