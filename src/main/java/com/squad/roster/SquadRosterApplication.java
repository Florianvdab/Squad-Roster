package com.squad.roster;

import jakarta.annotation.PostConstruct;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SquadRosterApplication {

    private final Listener listener;

    @Autowired
    public SquadRosterApplication(Listener listener) {
        this.listener = listener;
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
                Commands.slash("roster", "View the roster"),
                Commands.slash("create-roster", "Create a new roster"),
                Commands.slash("create-squad", "Create a new squad")
                        .addOptions(
                                new OptionData(OptionType.STRING, "roster", "The roster to create the squad in", true),
                                new OptionData(OptionType.ROLE, "role", "The role to assign to the squad", true),
                                new OptionData(OptionType.STRING, "name", "The name of the squad", true)
                        )
        ).queue();
    }
}