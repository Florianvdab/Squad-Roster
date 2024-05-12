package com.squad.roster.command.slash;

import com.squad.roster.model.Roster;
import com.squad.roster.repositories.RosterRepository;
import com.squad.roster.util.EventUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.util.List;
import java.util.Objects;

import static com.squad.roster.EventConstants.ROSTER_SLASH;
import static com.squad.roster.EventConstants.SELECT_ROSTER_VIEW_MODE;

public class ShowRosterSlash implements SlashCommand {

    private final RosterRepository rosterRepository;

    public ShowRosterSlash(RosterRepository rosterRepository) {
        this.rosterRepository = rosterRepository;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply(true)
                .queue();

        Guild guild = Objects.requireNonNull(event.getGuild());
        List<Roster> rosters = rosterRepository.findAllByGuildId(guild.getId());

        if (rosters.isEmpty()) {
            event.getHook()
                    .sendMessage("No rosters found, please create a roster using the /create-roster command")
                    .queue();
        }
        if (rosters.size() == 1) {
            EventUtil.getBaseView(rosters.getFirst(), event.getHook());
        }
        if (rosters.size() > 1) {
            event.getHook()
                    .sendMessage("Select a roster to depict")
                    .addActionRow(
                            StringSelectMenu.create(SELECT_ROSTER_VIEW_MODE)
                                    .setRequiredRange(1, 1)
                                    .addOptions(
                                            rosters.stream()
                                                    .map(roster -> SelectOption.of(roster.getName(), roster.getId()))
                                                    .toList())
                                    .build())
                    .queue();
        }

    }

    @Override
    public String getId() {
        return ROSTER_SLASH;
    }
}
