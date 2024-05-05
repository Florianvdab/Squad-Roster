package com.squad.roster.command.slash;

import com.squad.roster.model.Squad;
import com.squad.roster.repositories.RosterRepository;
import com.squad.roster.repositories.SquadRepository;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;

public class CreateSquadSlash implements SlashCommand {

    @Autowired
    private final RosterRepository rosterRepository;

    @Autowired
    private final SquadRepository squadRepository;

    public CreateSquadSlash(RosterRepository rosterRepository, SquadRepository squadRepository) {
        this.squadRepository = squadRepository;
        this.rosterRepository = rosterRepository;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Role role = event.getOption("role").getAsRole();
        String name = event.getOption("name").getAsString();
        String roster = event.getOption("roster").getAsString();

        rosterRepository.findByName(roster).ifPresentOrElse(
                r -> {
                    Squad squad = new Squad(r, name);
                    squad.attachRole(role.getId());
                    squadRepository.save(squad);

                    event.reply("squad saved, success").queue();
                },
                () -> event.reply("faulty roster").queue()
        );
    }
}
