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
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

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
        rosterRepository.findByName(roster).ifPresentOrElse(r -> {
            Role role = event.getOption("role").getAsRole();
            String name = event.getOption("name").getAsString();


            event.getGuild().getMembersWithRoles(role).forEach(member -> System.out.println(member.getEffectiveName()));

            Squad squad = new Squad(r, name);
            squad.attachRole(role.getIdLong());
            squadRepository.save(squad);

            event.reply("Squad created").queue();
        }, () -> event.reply("Roster not found").queue());
    }

    private void handleRosterCommand(SlashCommandInteractionEvent event) {
        // print out the roster

        event.deferReply().queue();

        rosterRepository.findAll().forEach(roster -> {

            event.getHook().sendMessage("Roster: " + roster.getName()).queue();

            roster.getSquads().forEach(squad -> {
                StringBuilder sb = new StringBuilder();

                Role role = event.getGuild().getRoleById(squad.getConnectedRoleId());

                sb.append("Squad: ");
                sb.append(squad.getName());
                sb.append(" (");
                sb.append(role.getAsMention());
                sb.append(")");

                sb.append("\n");
                sb.append("Members:");

                event.getGuild().getMembersWithRoles(role).forEach((bar) -> {
                    sb.append("\n");
                    sb.append(bar.getAsMention());
                });

                event.getHook().sendMessage(sb.toString())
                        .addActionRow(
                                Button.primary("id-button-1", "Change role"),
                                Button.secondary("rename-button-" + squad.getId(), "Change name"),
                                Button.danger("id-button-3", "Delete squad")
                        ).queue();
            });
        });
    }


    private void handleCreateRosterCommand(SlashCommandInteractionEvent event) {
        // create a new roster
        Optional.ofNullable(event.getOption("name")).ifPresentOrElse(option -> {
            String rosterName = option.getAsString();
            if (!isNullOrEmpty(rosterName)) {
                Roster roster = new Roster(rosterName, event.getGuild().getId());
                rosterRepository.save(roster);
                event.reply("Roster created").queue();
            } else {
                event.reply("Invalid roster name").queue();
            }
        }, () -> event.reply("Invalid roster name").queue());
    }

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        if (event.getModalId().equals("create-roster")) {
            String rosterName = event.getValue("roster-name").getAsString();
            if (!isNullOrEmpty(rosterName)) {
                Roster roster = new Roster(rosterName, event.getGuild().getId());
                rosterRepository.save(roster);
                event.reply("Roster created").queue();
            }
        }
        if (event.getModalId().startsWith("rename-modal-")) {
            String squadId = event.getModalId().replace("rename-modal-", "");
            Optional<Squad> squad = squadRepository.findById(Long.parseLong(squadId));
            squad.ifPresentOrElse(s -> {
                String newName = event.getValue("new-name").getAsString();
                if (!isNullOrEmpty(newName)) {
                    s.setName(newName);
                    squadRepository.save(s);
                    event.reply("Squad renamed").queue();
                } else {
                    event.reply("Invalid name").queue();
                }
            }, () -> event.reply("Squad not found").queue());
        }
        super.onModalInteraction(event);
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (event.getComponent().getId().startsWith("rename-button-")) {
            String squadId = event.getComponent().getId().replace("rename-button-", "");
            Optional<Squad> squad = squadRepository.findById(Long.parseLong(squadId));
            squad.ifPresentOrElse(s -> {
                //ask for the new name

                TextInput input = TextInput.create("new-name", "New name", TextInputStyle.SHORT)
                        .setMaxLength(32)
                        .build();

                Modal modal = Modal.create("rename-modal-" + s.getId(), "Rename squad")
                        .addComponents(ActionRow.of(input))
                        .build();

                event.replyModal(modal).queue();

            }, () -> event.reply("Squad not found").queue());
        }
    }

    public boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
