package com.squad.roster.command.button;

import com.squad.roster.model.Roster;
import com.squad.roster.model.Squad;
import com.squad.roster.repositories.RosterRepository;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.squad.roster.EventConstants.*;

public class RenameRosterButton implements ButtonCommand {

    @Autowired
    private  final RosterRepository rosterRepository;

    public RenameRosterButton(RosterRepository rosterRepository) {
        this.rosterRepository = rosterRepository;
    }

    @Override
    public void execute(ButtonInteractionEvent event) {
        String squadId = event.getComponent().getId().replace(RENAME_SQUAD_BUTTON_COMMAND, "");
        Optional<Roster> roster = rosterRepository.findById(squadId);
        roster.ifPresentOrElse(s -> {
            //ask for the new name
            TextInput input = TextInput.create(NAME_INPUT, "New name", TextInputStyle.SHORT)
                    .setMaxLength(32)
                    .build();

            Modal modal = Modal.create(RENAME_SQUAD_MODAL_COMMAND + s.getId(), "Rename roster")
                    .addComponents(ActionRow.of(input))
                    .build();

            event.replyModal(modal).queue();

        }, () -> event.reply("Squad not found").queue());
    }
}
