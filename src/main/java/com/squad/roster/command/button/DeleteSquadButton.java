package com.squad.roster.command.button;

import com.squad.roster.EventConstants;
import com.squad.roster.repositories.SquadRepository;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;

public class DeleteSquadButton implements ButtonCommand {

    @Autowired
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
