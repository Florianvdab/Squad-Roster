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
        TextInput subject = TextInput.create("squad-name", "Enter the Squad name", TextInputStyle.SHORT)
                .setPlaceholder("Alpha, Bravo, Charlie, ...")
                .setMaxLength(100) // or setRequiredRange(10, 100)
                .build();

        Modal modal = Modal.create(EventConstants.CREATE_SQUAD_MODAL_COMMAND, "Modmail")
                .addComponents(ActionRow.of(subject))
                .build();

        event.replyModal(modal).queue();
    }
}
