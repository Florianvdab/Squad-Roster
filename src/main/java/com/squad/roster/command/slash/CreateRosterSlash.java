package com.squad.roster.command.slash;

import com.squad.roster.EventConstants;
import com.squad.roster.model.Roster;
import com.squad.roster.repositories.RosterRepository;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Optional;

import static com.squad.roster.util.EventUtil.isNotNullOrEmpty;

public class CreateRosterSlash implements SlashCommand {

    private final RosterRepository rosterRepository;

    public CreateRosterSlash(RosterRepository rosterRepository) {
        this.rosterRepository = rosterRepository;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Optional.ofNullable(event.getOption("name")).ifPresentOrElse(option -> {
            String rosterName = option.getAsString();
            if (isNotNullOrEmpty(rosterName)) {
                Roster roster = new Roster(rosterName, event.getGuild().getId());
                rosterRepository.save(roster);
                event.reply("Roster created").setEphemeral(true).queue();
            } else {
                event.reply("Invalid roster name").setEphemeral(true).queue();
            }
        }, () -> event.reply("Invalid roster name").setEphemeral(true).queue());
    }

    @Override
    public String getId() {
        return EventConstants.CREATE_ROSTER_SLASH;
    }
}
