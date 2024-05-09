package com.squad.roster.command.modal;

import com.squad.roster.EventConstants;
import com.squad.roster.model.Squad;
import com.squad.roster.repositories.SquadRepository;
import com.squad.roster.util.StringUtil;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

import java.util.Optional;


public class RenameSquadModal implements ModalCommand {

    private final SquadRepository squadRepository;

    public RenameSquadModal(SquadRepository squadRepository) {
        this.squadRepository = squadRepository;
    }


    @Override
    public void execute(ModalInteractionEvent event) {
        String squadId = event.getModalId().replace("rename-modal-", "");
        Optional<Squad> squad = squadRepository.findById(squadId);
        squad.ifPresentOrElse(s -> {
                    String newName = event.getValue(EventConstants.NAME_INPUT).getAsString();
                    if (!StringUtil.isNotNullOrEmpty(newName)) {
                        s.setName(newName);
                        squadRepository.save(s);
                        event.reply("Squad renamed").queue();
                    } else {
                        event.reply("Invalid name").queue();
                    }
                },
                () -> event.reply("Squad not found").queue());
    }
}