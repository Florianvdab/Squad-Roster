package com.squad.roster.command.modal;

import com.squad.roster.EventConstants;
import com.squad.roster.repositories.RosterRepository;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

import static com.squad.roster.EventConstants.NAME_INPUT;

public class RenameRosterModal implements ModalCommand {

    private final RosterRepository rosterRepository;

    public RenameRosterModal(RosterRepository rosterRepository) {
        this.rosterRepository = rosterRepository;
    }

    @Override
    public void execute(ModalInteractionEvent event) {
        String id = event.getModalId().replace(EventConstants.RENAME_ROSTER_BUTTON_COMMAND, "");
        String name = event.getValue(NAME_INPUT).getAsString();

        rosterRepository.findById(id).ifPresentOrElse(roster -> {
                    roster.setName(name);
                    rosterRepository.save(roster);
                    event.reply("name updated").queue();
                },
                () -> event.reply("something unexpected happened").queue());
    }
}
