package com.squad.roster.command.interaction;

import com.squad.roster.model.Squad;
import com.squad.roster.repositories.SquadRepository;
import com.squad.roster.util.EventUtil;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;

import static com.squad.roster.EventConstants.ATTACH_ROLE_SQUAD_SELECT;

public class AttachRoleToSquadSelection implements EntitySelectInteraction {

    private final SquadRepository squadRepository;

    public AttachRoleToSquadSelection(SquadRepository squadRepository) {
        this.squadRepository = squadRepository;
    }

    @Override
    public void execute(EntitySelectInteractionEvent event) {
        String squadId = event.getComponentId().replace(ATTACH_ROLE_SQUAD_SELECT, "");
        Squad squad = squadRepository.findById(squadId).orElseThrow();
        String roleId = event.getValues().getFirst().getId();
        squad.attachRole(roleId);
        squadRepository.save(squad);

        event.editMessage(EventUtil.getSquadString(event.getGuild(), squad))
                .setActionRow(EventUtil.getSquadComponents(squad))
                .queue();
    }

    @Override
    public String getId() {
        return ATTACH_ROLE_SQUAD_SELECT;
    }
}