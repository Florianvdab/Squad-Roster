package com.squad.roster.command.interaction;

import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

public interface StringSelectInteraction{
    void execute(StringSelectInteractionEvent event);
    String getId();
}
