package com.squad.roster.command.interaction;

import com.squad.roster.EventConstants;
import com.squad.roster.repositories.RosterRepository;
import com.squad.roster.util.EventUtil;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

public class SelectRosterEdit implements StringSelectInteraction{

    private final RosterRepository rosterRepository;

    public SelectRosterEdit(RosterRepository rosterRepository) {
        this.rosterRepository = rosterRepository;
    }

    @Override
    public void execute(StringSelectInteractionEvent event) {
        event.deferReply(true)
                .queue();

        String rosterId = event.getValues().getFirst();
        EventUtil.showEditRoster(
                event.getHook(),
                event.getGuild(),
                rosterRepository.findById(rosterId).orElseThrow());
    }

    @Override
    public String getId() {
        return EventConstants.SELECT_ROSTER_EDIT_MODE;
    }
}
