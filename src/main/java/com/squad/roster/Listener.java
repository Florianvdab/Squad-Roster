package com.squad.roster;

import com.squad.roster.local.Roster;
import com.squad.roster.local.command.FrankCommand;
import com.squad.roster.repositories.RosterRepository;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
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
            default -> event.reply("Unknown command").queue();
        }
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

    public boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
