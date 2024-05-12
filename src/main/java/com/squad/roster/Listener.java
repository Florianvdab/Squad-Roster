package com.squad.roster;

import com.squad.roster.command.CommandManager;
import com.squad.roster.repositories.RosterRepository;
import com.squad.roster.repositories.SquadRepository;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class Listener extends ListenerAdapter {

    private final CommandManager commandManager;

    public Listener(SquadRepository squadRepository, RosterRepository rosterRepository) {
        this.commandManager = new CommandManager(squadRepository, rosterRepository);

    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        System.out.println("Bravo SIX, going dark.");
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        Optional.ofNullable(event.getGuild()).ifPresentOrElse(
                _ -> commandManager.executeSlashCommand(event),
                () -> event.reply("This bot can only be used in a server")
                        .setEphemeral(true)
                        .queue()
        );
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        commandManager.executeButtonCommand(event);
    }

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        commandManager.executeModalCommand(event);
    }

    @Override
    public void onEntitySelectInteraction(@NotNull EntitySelectInteractionEvent event) {
        commandManager.executeEntitySelectCommand(event);
    }

    @Override
    public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {
        commandManager.executeStringSelectCommand(event);
    }
}