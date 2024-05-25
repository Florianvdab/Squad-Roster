package com.squad.roster.command.button;

import com.squad.roster.EventConstants;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

public class CreateSquadButton implements ButtonCommand {

    @Override
    public void execute(ButtonInteractionEvent event) {
        String rosterId = event.getComponentId().replace(EventConstants.CREATE_SQUAD_BUTTON, "");
        TextInput input = TextInput.create(EventConstants.SQUAD_NAME_INPUT, "Squad name", TextInputStyle.SHORT)
                .setPlaceholder("Enter the name of the squad")
                .setMaxLength(32)
                .setMinLength(1)
                .build();

        event.replyModal(Modal.create(EventConstants.CREATE_SQUAD_MODAL + rosterId, "Create squad")
                        .addComponents(ActionRow.of(input))
                        .build())
                .queue();
    }

    @Override
    public String getId() {
        return EventConstants.CREATE_SQUAD_BUTTON;
    }
}