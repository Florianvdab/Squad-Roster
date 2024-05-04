package com.squad.roster.command.modal;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

public class CreateSquadModal implements ModalCommand {
    @Override
    public void execute(ModalInteractionEvent event) {
        event.reply("Create Squad Modal").queue();
    }
}
