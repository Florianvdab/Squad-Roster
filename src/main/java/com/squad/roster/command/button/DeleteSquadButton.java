package com.squad.roster.command.button;

import com.squad.roster.EventConstants;
import com.squad.roster.repositories.SquadRepository;
import jakarta.transaction.Transactional;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;

import java.time.Duration;
import java.util.ArrayList;

public class DeleteSquadButton implements ButtonCommand {

    private final SquadRepository squadRepository;

    public DeleteSquadButton(SquadRepository squadRepository) {
        this.squadRepository = squadRepository;
    }

    @Override
    @Transactional
    public void execute(ButtonInteractionEvent event) {
        String id = event.getComponentId().replace(EventConstants.DELETE_SQUAD_BUTTON, "");
        squadRepository.deleteById(id);
        event.editMessage("Squad deleted")
                .setComponents(new ArrayList<>())
                .delay(Duration.ofSeconds(3))
                .flatMap(InteractionHook::deleteOriginal)
                .queue();
    }

    @Override
    public String getId() {
        return EventConstants.DELETE_SQUAD_BUTTON;
    }
}