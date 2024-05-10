package com.squad.roster.command.slash;

import com.squad.roster.model.Roster;
import com.squad.roster.repositories.RosterRepository;
import com.squad.roster.util.EventUtil;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.squad.roster.EventConstants.SELECT_ROSTER_EDIT_MODE;

@Component
public class EditRosterSlash implements SlashCommand {

    private final RosterRepository rosterRepository;

    public EditRosterSlash(RosterRepository rosterRepository) {
        this.rosterRepository = rosterRepository;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply(true)
                .queue();

        Optional.ofNullable(event.getGuild()).ifPresentOrElse(
                (guild -> {
                    List<Roster> rosters = rosterRepository.findAllByGuildId(guild.getId());

                    if (rosters.isEmpty()) {
                        event.getHook()
                                .sendMessage("No rosters found, please create a roster using the /create-roster command")
                                .queue();
                    }
                    if (rosters.size() == 1) {
                        EventUtil.showEditRoster(event.getHook(), guild, rosters.getFirst());
                    }
                    if (rosters.size() > 1) {
                        event.getHook()
                                .sendMessage("Select a roster to edit")
                                .addActionRow(
                                        StringSelectMenu.create(SELECT_ROSTER_EDIT_MODE)
                                                .setRequiredRange(1, 1)
                                                .addOptions(
                                                        rosters.stream()
                                                                .map(roster -> SelectOption.of(roster.getName(), roster.getId()))
                                                                .toList())
                                                .build())
                                .queue();
                    }
                }),
                () -> event.getHook()
                        .sendMessage("This command can only be used in a server")
                        .queue()
        );
    }
}