package com.squad.roster;

import com.squad.roster.repositories.RosterRepository;
import com.squad.roster.repositories.SquadRepository;
import jakarta.annotation.PostConstruct;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;


@SpringBootApplication
@PropertySource("classpath:application.properties")
public class SquadRosterApplication {

    private final Listener listener;

    public SquadRosterApplication(RosterRepository rosterRepository, SquadRepository squadRepository) {
        this.listener = new Listener(squadRepository, rosterRepository);
    }

    public static void main(String[] args) {
        SpringApplication.run(SquadRosterApplication.class, args);
    }

    @PostConstruct
    public void init() {
        System.out.println("Hello world!");
        JDA jda = JDABuilder
                .createDefault(System.getenv("DISCORD_TOKEN"))
                .build();

        jda.addEventListener(listener);

        jda.updateCommands().addCommands(
                Commands.slash(EventConstants.ROSTER_SLASH, "View a roster"),
                Commands.slash(EventConstants.CREATE_ROSTER_SLASH, "Create a roster")
                        .addOption(OptionType.STRING, "name", "Name for the roster (f.e PvP, Base)", true),
                Commands.slash(EventConstants.EDIT_ROSTER_SLASH, "Edit a roster"),
                Commands.slash(EventConstants.CREATE_SQUAD_SLASH, "Create a squad")
                        .addOption(OptionType.STRING, "roster", "the roster that you'd like to connect the squad to", true)
                        .addOption(OptionType.ROLE, "role", "the role you'd like to connect to the squad", true)
                        .addOption(OptionType.STRING, "name", "the name of the squad (f.e Alpha, Bravo, Charlie, ...)", true)
        ).queue();
    }
}