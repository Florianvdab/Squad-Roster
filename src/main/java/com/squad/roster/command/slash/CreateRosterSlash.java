package com.squad.roster.command.slash;

import com.squad.roster.model.Roster;
import com.squad.roster.repositories.RosterRepository;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.squad.roster.util.StringUtil.isNotNullOrEmpty;

public class CreateRosterSlash implements SlashCommand {

    @Autowired
    private RosterRepository rosterRepository;

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Optional.ofNullable(event.getOption("name")).ifPresentOrElse(option -> {
            String rosterName = option.getAsString();
            if (isNotNullOrEmpty(rosterName)) {
                Roster roster = new Roster(rosterName, event.getGuild().getId());
                rosterRepository.save(roster);
                event.reply("Roster created").queue();
            } else {
                event.reply("Invalid roster name").queue();
            }
        }, () -> event.reply("Invalid roster name").queue());
    }
}
