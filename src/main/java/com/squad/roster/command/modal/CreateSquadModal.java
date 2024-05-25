package com.squad.roster.command.modal;

import com.squad.roster.EventConstants;
import com.squad.roster.model.Squad;
import com.squad.roster.repositories.RosterRepository;
import com.squad.roster.repositories.SquadRepository;
import com.squad.roster.util.EventUtil;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

@RequiredArgsConstructor
public class CreateSquadModal implements ModalCommand {

    private final SquadRepository squadRepository;
    private final RosterRepository rosterRepository;

    @Override
    public void execute(ModalInteractionEvent event) {
        event.deferReply(true).queue();
        String rosterId = event.getModalId().replace(EventConstants.CREATE_SQUAD_MODAL, "");
        String squadName = event.getValue(EventConstants.SQUAD_NAME_INPUT).getAsString();

        Squad s = squadRepository.save(new Squad(rosterRepository.findById(rosterId).orElseThrow(), squadName));

        EventUtil.showEditSquad(event.getHook(), event.getGuild(), s);
    }

    @Override
    public String getId() {
        return EventConstants.CREATE_SQUAD_MODAL;
    }
}
