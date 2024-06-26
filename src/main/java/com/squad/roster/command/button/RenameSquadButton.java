package com.squad.roster.command.button;

import com.squad.roster.repositories.SquadRepository;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import static com.squad.roster.EventConstants.*;

public class RenameSquadButton implements ButtonCommand {

    private final SquadRepository squadRepository;

    public RenameSquadButton(SquadRepository squadRepository) {
        this.squadRepository = squadRepository;
    }

    @Override
    public void execute(ButtonInteractionEvent event) {
        String squadId = event.getComponentId().replace(RENAME_SQUAD_BUTTON, "");

        squadRepository.findById(squadId).ifPresentOrElse(squad -> {
            TextInput input = TextInput.create(NAME_INPUT, "New name", TextInputStyle.SHORT)
                    .setMaxLength(32)
                    .build();

            Modal modal = Modal.create(RENAME_SQUAD_MODAL + squad.getId(), "Rename squad")
                    .addComponents(ActionRow.of(input))
                    .build();

            event.replyModal(modal).queue();

        }, () -> event.reply("Squad not found").queue());
    }

    @Override
    public String getId() {
        return RENAME_SQUAD_BUTTON;
    }
}
