package com.squad.roster.command.modal;

import com.squad.roster.EventConstants;
import com.squad.roster.model.Squad;
import com.squad.roster.repositories.SquadRepository;
import com.squad.roster.util.StringUtil;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;


public class RenameSquadModal implements ModalCommand {

    @Autowired
    private SquadRepository squadRepository;

    @Override
    public void execute(ModalInteractionEvent event) {
        String squadId = event.getModalId().replace("rename-modal-", "");
        Optional<Squad> squad = squadRepository.findById(Long.parseLong(squadId));
        squad.ifPresentOrElse(s -> {
                    String newName = event.getValue(EventConstants.RENAME_INPUT).getAsString();
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