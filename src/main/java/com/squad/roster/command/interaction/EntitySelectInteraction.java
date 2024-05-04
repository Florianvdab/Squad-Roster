package com.squad.roster.command.interaction;

import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;

public interface EntitySelectInteraction {
    void execute(EntitySelectInteractionEvent event);
}
