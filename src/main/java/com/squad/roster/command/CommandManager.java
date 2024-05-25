package com.squad.roster.command;

import com.squad.roster.command.button.*;
import com.squad.roster.command.interaction.*;
import com.squad.roster.command.modal.CreateSquadModal;
import com.squad.roster.command.modal.ModalCommand;
import com.squad.roster.command.modal.RenameRosterModal;
import com.squad.roster.command.modal.RenameSquadModal;
import com.squad.roster.command.slash.*;
import com.squad.roster.repositories.RosterRepository;
import com.squad.roster.repositories.SquadRepository;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    private final List<SlashCommand> slashCommands = new ArrayList<>();
    private final List<ButtonCommand> buttonCommands = new ArrayList<>();
    private final List<ModalCommand> modalCommands = new ArrayList<>();
    private final List<EntitySelectInteraction> entitySelectInteractions = new ArrayList<>();
    private final List<StringSelectInteraction> stringSelectInteractions = new ArrayList<>();

    public CommandManager(SquadRepository squadRepository, RosterRepository rosterRepository) {
        slashCommands.add(new ShowRosterSlash(rosterRepository));
        slashCommands.add(new CreateRosterSlash(rosterRepository));
        slashCommands.add(new EditRosterSlash(rosterRepository));
        slashCommands.add(new CreateSquadSlash(rosterRepository, squadRepository));

        buttonCommands.add(new RenameRosterButton(rosterRepository));
        buttonCommands.add(new DeleteRosterButton(rosterRepository));
        buttonCommands.add(new CreateSquadButton());
        buttonCommands.add(new DeleteSquadButton(squadRepository));
        buttonCommands.add(new RenameSquadButton(squadRepository));
        buttonCommands.add(new AttachRoleToSquadButton());

        modalCommands.add(new RenameRosterModal(rosterRepository));
        modalCommands.add(new RenameSquadModal(squadRepository));
        modalCommands.add(new CreateSquadModal(squadRepository, rosterRepository));

        stringSelectInteractions.add(new SelectRosterView(rosterRepository));
        stringSelectInteractions.add(new SelectRosterEdit(rosterRepository));

        entitySelectInteractions.add(new AttachRoleToSquadSelection(squadRepository));
    }

    public void executeSlashCommand(SlashCommandInteractionEvent event) {
        slashCommands.stream()
                .filter(slashCommand -> event.getName().equals(slashCommand.getId()))
                .findFirst()
                .ifPresentOrElse(
                        command -> command.execute(event),
                        () -> System.out.println("Unknown command")
                );
    }

    public void executeButtonCommand(ButtonInteractionEvent event) {
        buttonCommands.stream()
                .filter(buttonCommand -> event.getComponentId().startsWith(buttonCommand.getId()))
                .findFirst()
                .ifPresentOrElse(
                        command -> command.execute(event),
                        () -> System.out.println("Unknown button")
                );
    }

    public void executeModalCommand(ModalInteractionEvent event) {
        modalCommands.stream()
                .filter(modalCommand -> event.getModalId().startsWith(modalCommand.getId()))
                .findFirst()
                .ifPresentOrElse(
                        command -> command.execute(event),
                        () -> System.out.println("Unknown modal")
                );
    }

    public void executeEntitySelectCommand(EntitySelectInteractionEvent event) {
        entitySelectInteractions.stream()
                .filter(entitySelectInteraction -> event.getComponentId().startsWith(entitySelectInteraction.getId()))
                .findFirst()
                .ifPresentOrElse(
                        command -> command.execute(event),
                        () -> System.out.println("Unknown entity select")
                );
    }

    public void executeStringSelectCommand(StringSelectInteractionEvent event) {
        stringSelectInteractions.stream()
                .filter(stringSelectInteraction -> event.getComponentId().startsWith(stringSelectInteraction.getId()))
                .findFirst()
                .ifPresentOrElse(
                        command -> command.execute(event),
                        () -> System.out.println("Unknown string select")
                );
    }
}