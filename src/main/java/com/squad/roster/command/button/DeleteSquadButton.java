package com.squad.roster.command.button;

import com.squad.roster.EventConstants;
import com.squad.roster.repositories.SquadRepository;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class DeleteSquadButton implements ButtonCommand {

    private final SquadRepository squadRepository;

    public DeleteSquadButton(SquadRepository squadRepository) {
        this.squadRepository = squadRepository;
    }

    @Override
    public void execute(ButtonInteractionEvent event) {
        String id = event.getComponentId().replace(EventConstants.DELETE_SQUAD_BUTTON_COMMAND, "");
        squadRepository.deleteById(id);
        event.reply("squad deleted").queue();
    }
}
