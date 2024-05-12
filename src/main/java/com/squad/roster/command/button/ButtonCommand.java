package com.squad.roster.command.button;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public interface ButtonCommand {
    void execute(ButtonInteractionEvent event);
    String getId();
}
