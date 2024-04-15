package com.squad.roster;

import com.squad.roster.local.Roster;
import com.squad.roster.local.command.FrankCommand;
import com.squad.roster.repositories.RosterRepository;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Listener extends ListenerAdapter {
    @Autowired
    private final RosterRepository rosterRepository;

    public Listener(RosterRepository rosterRepository) {
        this.rosterRepository = rosterRepository;
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        System.out.println("Bravo SIX, going dark.");
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "frank" -> new FrankCommand().execute(event);
            case "create" -> {
                rosterRepository.save(new Roster( "some name"));
                event.reply("Shauwwnienetje has been created").queue();
            }
            default -> event.reply("Unknown command").queue();
        }
    }
}
