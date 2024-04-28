package com.squad.roster;

import com.squad.roster.local.Roster;
import com.squad.roster.local.Squad;
import com.squad.roster.repositories.RosterRepository;
import com.squad.roster.repositories.SquadRepository;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//TODO: for now there's only one discord, so the database is strictly theirs
// implement it so that the bot can be used in multiple servers

@Component
public class Listener extends ListenerAdapter {
    @Autowired
    private final RosterRepository rosterRepository;

    @Autowired
    private final SquadRepository squadRepository;

    public Listener(RosterRepository rosterRepository, SquadRepository squadRepository) {
        this.rosterRepository = rosterRepository;
        this.squadRepository = squadRepository;
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        System.out.println("Bravo SIX, going dark.");
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "roster" -> handleRosterCommand(event);
            case "create-roster" -> handleCreateRosterCommand(event);
            case "create-squad" -> handleSquadCommand(event);
            default -> event.reply("Unknown command").queue();
        }
    }

    private void handleSquadCommand(SlashCommandInteractionEvent event) {
        String roster = event.getOption("roster").getAsString();
        rosterRepository.findByName(roster).ifPresentOrElse(
                r -> {
                    Role role = event.getOption("role").getAsRole();
                    String name = event.getOption("name").getAsString();

                    event.getGuild().getMembersWithRoles(role)
                            .forEach(member -> System.out.println(member.getEffectiveName()));

                    Squad squad = new Squad(r, name);
                    squad.attachRole(role.getIdLong());
                    squadRepository.save(squad);

                    event.reply("Squad created").queue();
                },
                () -> event.reply("Roster not found").queue()
        );
    }

    private void handleRosterCommand(SlashCommandInteractionEvent event) {
        // print out the roster
        StringBuilder sb = new StringBuilder();
        rosterRepository.findAll().forEach(roster -> {
            sb.append("Roster: ")
                    .append(roster.getName()).append("\n");
            roster.getSquads().forEach(squad -> {
                sb.append("Squad: ").append(squad.getName()).append("\n");
                event.getGuild().getMembersWithRoles(event.getGuild().getRoleById(squad.getConnectedRoleId()))
                        .forEach(member -> sb.append(member.getAsMention()).append("\n"));
            });
        });
        event.reply(sb.toString()).queue();
    }


    private void handleCreateRosterCommand(SlashCommandInteractionEvent event) {
        // create a new roster
        TextInput textInput = TextInput.create("roster-name", "Enter the name of the roster", TextInputStyle.SHORT)
                .setPlaceholder("Enter the name of the roster")
                .setMinLength(3)
                .setMaxLength(32)
                .setRequired(true)
                .build();

        Modal modal = Modal.create("create-roster", "Create a new roster")
                .addActionRow(textInput)
                .setTitle("Create a new roster")
                .build();

        event.replyModal(modal)
                .queue();
    }

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        if (event.getModalId().equals("create-roster")) {
            String rosterName = event.getValue("roster-name").getAsString();
            if (!isNullOrEmpty(rosterName)) {
                Roster roster = new Roster(rosterName);
                rosterRepository.save(roster);
                event.reply("Roster created").queue();
            }
        }
        super.onModalInteraction(event);
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (event.getComponentId().equals("squad-name")) {
            event.reply("Button clicked").queue();
        }
        super.onButtonInteraction(event);
    }

    public boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
