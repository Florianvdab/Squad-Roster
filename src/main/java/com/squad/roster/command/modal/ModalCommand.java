package com.squad.roster.command.modal;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

public interface ModalCommand {
    void execute(ModalInteractionEvent event);
    String getId();
}
