package com.squad.roster.command.interaction;

import com.squad.roster.EventConstants;
import com.squad.roster.repositories.RosterRepository;
import com.squad.roster.util.EventUtil;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

public class SelectRosterView implements StringSelectInteraction {

    private final RosterRepository rosterRepository;

    public SelectRosterView(RosterRepository rosterRepository) {
        this.rosterRepository = rosterRepository;
    }

    @Override
    public void execute(StringSelectInteractionEvent event) {
        String rosterId = event.getValues().getFirst();
        event.reply(EventUtil.getRosterView(rosterRepository.findById(rosterId).orElseThrow(), event.getGuild()))
                .queue();
    }

    @Override
    public String getId() {
        return EventConstants.SELECT_ROSTER_VIEW_MODE;
    }
}
