package com.squad.roster.command.button;

import com.squad.roster.repositories.RosterRepository;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import static com.squad.roster.EventConstants.*;

public class RenameRosterButton implements ButtonCommand {

    private final RosterRepository rosterRepository;

    public RenameRosterButton(RosterRepository rosterRepository) {
        this.rosterRepository = rosterRepository;
    }

    @Override
    public void execute(ButtonInteractionEvent event) {
        String rosterId = event.getComponentId().replace(RENAME_ROSTER_BUTTON, "");

        rosterRepository.findById(rosterId).ifPresentOrElse(
                roster -> {
                    TextInput input = TextInput.create(NAME_INPUT, "New name", TextInputStyle.SHORT)
                            .setMaxLength(32)
                            .build();

                    Modal modal = Modal.create(RENAME_ROSTER_MODAL + roster.getId(), "Rename roster")
                            .addComponents(ActionRow.of(input))
                            .build();

                    event.replyModal(modal).queue();
                },
                () -> event.reply("Roster not found").queue());
    }
}
