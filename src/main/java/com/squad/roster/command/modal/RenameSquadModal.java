package com.squad.roster.command.modal;

import com.squad.roster.EventConstants;
import com.squad.roster.repositories.SquadRepository;
import com.squad.roster.util.EventUtil;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;


public class RenameSquadModal implements ModalCommand {

    private final SquadRepository squadRepository;

    public RenameSquadModal(SquadRepository squadRepository) {
        this.squadRepository = squadRepository;
    }

    @Override
    public void execute(ModalInteractionEvent event) {
        String id = event.getModalId().replace(EventConstants.RENAME_SQUAD_MODAL, "");
        String name = event.getValue(EventConstants.NAME_INPUT).getAsString();

        squadRepository.findById(id).ifPresentOrElse(squad -> {
                    squad.setName(name);
                    squadRepository.save(squad);

                    event.editMessage(EventUtil.getSquadString(event.getGuild(), squad))
                            .queue();
                },
                () -> event.reply("Squad not found")
                        .setEphemeral(true)
                        .queue());
    }

    @Override
    public String getId() {
        return EventConstants.RENAME_SQUAD_MODAL;
    }
}