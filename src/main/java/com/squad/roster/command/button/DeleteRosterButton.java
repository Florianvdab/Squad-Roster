package com.squad.roster.command.button;

import com.squad.roster.EventConstants;
import com.squad.roster.repositories.RosterRepository;
import jakarta.transaction.Transactional;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;

import java.time.Duration;
import java.util.ArrayList;

public class DeleteRosterButton implements ButtonCommand {

    private final RosterRepository rosterRepository;

    public DeleteRosterButton(RosterRepository rosterRepository) {
        this.rosterRepository = rosterRepository;
    }

    @Override
    @Transactional
    public void execute(ButtonInteractionEvent event) {
        String id = event.getComponentId().replace(EventConstants.DELETE_ROSTER_BUTTON, "");
        rosterRepository.deleteById(id);
        event.editMessage("""
                        Roster deleted, BEWARE: Some squad edit messages might still be visible.
                        """)
                .setComponents(new ArrayList<>())
                .delay(Duration.ofSeconds(6))
                .flatMap(InteractionHook::deleteOriginal)
                .queue();
    }

    @Override
    public String getId() {
        return EventConstants.DELETE_ROSTER_BUTTON;
    }
}
