package com.squad.roster.command.button;

import com.squad.roster.EventConstants;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu;

import static com.squad.roster.EventConstants.ATTACH_ROLE_SQUAD_SELECT;

public class AttachRoleToSquadButton implements ButtonCommand {
    @Override
    public void execute(ButtonInteractionEvent event) {
        String squadId = event.getComponentId().replace(EventConstants.ATTACH_ROLE_SQUAD_BUTTON, "");
        event.editMessage("Please mention the role you would like to attach to the squad")
                .setActionRow(
                        EntitySelectMenu.create(ATTACH_ROLE_SQUAD_SELECT + squadId, EntitySelectMenu.SelectTarget.ROLE)
                                .setRequiredRange(1, 1)
                                .build())
                .queue();
    }

    @Override
    public String getId() {
        return EventConstants.ATTACH_ROLE_SQUAD_BUTTON;
    }
}