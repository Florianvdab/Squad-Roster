package com.squad.roster.command.button;

import com.squad.roster.EventConstants;
import com.squad.roster.repositories.RosterRepository;
import jakarta.transaction.Transactional;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;

public class DeleteRosterButton implements ButtonCommand {

    @Autowired
    private final RosterRepository rosterRepository;

    public DeleteRosterButton(RosterRepository rosterRepository) {
        this.rosterRepository = rosterRepository;
    }

    @Override
    @Transactional
    public void execute(ButtonInteractionEvent event) {
        String id = event.getComponentId().replace(EventConstants.DELETE_ROSTER_BUTTON_COMMAND, "");

        rosterRepository.deleteById(id);
        event.reply("roster deleted").queue();
    }
}
