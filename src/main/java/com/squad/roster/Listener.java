package com.squad.roster;

import com.squad.roster.command.button.*;
import com.squad.roster.command.interaction.AttachRoleToSquadSelection;
import com.squad.roster.command.modal.CreateSquadModal;
import com.squad.roster.command.modal.RenameRosterModal;
import com.squad.roster.command.modal.RenameSquadModal;
import com.squad.roster.command.slash.CreateRosterSlash;
import com.squad.roster.command.slash.CreateSquadSlash;
import com.squad.roster.command.slash.EditRosterSlash;
import com.squad.roster.command.slash.ShowRosterSlash;
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

import static com.squad.roster.EventConstants.*;

@Component
public class Listener extends ListenerAdapter {

    private final SquadRepository squadRepository;

    private final RosterRepository rosterRepository;

    public Listener(SquadRepository squadRepository, RosterRepository rosterRepository) {
        this.squadRepository = squadRepository;
        this.rosterRepository = rosterRepository;
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        System.out.println("Bravo SIX, going dark.");
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case ROSTER_SLASH_COMMAND -> new ShowRosterSlash().execute(event);
            case CREATE_ROSTER_SLASH_COMMAND -> new CreateRosterSlash(rosterRepository).execute(event);
            case EDIT_ROSTER_SLASH_COMMAND -> new EditRosterSlash(rosterRepository).execute(event);
            case CREATE_SQUAD_SLASH_COMMAND -> new CreateSquadSlash(rosterRepository, squadRepository).execute(event);
            default -> event.reply("Unknown command").queue();
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponentId().startsWith(RENAME_ROSTER_BUTTON_COMMAND)) {
            new RenameRosterButton(rosterRepository).execute(event);
        } else if (event.getComponentId().startsWith(DELETE_ROSTER_BUTTON_COMMAND)) {
            new DeleteRosterButton(rosterRepository).execute(event);
        } else if (event.getComponentId().startsWith(CREATE_SQUAD_BUTTON_COMMAND)) {
            new CreateSquadButton().execute(event);
        } else if (event.getComponentId().startsWith(DELETE_SQUAD_BUTTON_COMMAND)) {
            new DeleteSquadButton(squadRepository).execute(event);
        } else if (event.getComponentId().startsWith(RENAME_SQUAD_BUTTON_COMMAND)) {
            new RenameSquadButton(squadRepository).execute(event);
        } else if (event.getComponentId().startsWith(ATTACH_ROLE_SQUAD_BUTTON_COMMAND)) {
            new AttachRoleToSquadButton().execute(event);
        }
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (event.getModalId().startsWith(RENAME_ROSTER_MODAL_COMMAND)) {
            new RenameRosterModal(rosterRepository).execute(event);
        } else if (event.getModalId().startsWith(RENAME_SQUAD_MODAL_COMMAND)) {
            new RenameSquadModal(squadRepository).execute(event);
        } else if (event.getModalId().startsWith(CREATE_SQUAD_MODAL_COMMAND)) {
            new CreateSquadModal().execute(event);
        }
    }

    @Override
    public void onEntitySelectInteraction(EntitySelectInteractionEvent event) {
        if (event.getComponentId().startsWith(ATTACH_ROLE_SQUAD_BUTTON_COMMAND)) {
            new AttachRoleToSquadSelection().execute(event);
        }
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        event.deferReply().queue();
        if (event.getComponentId().startsWith("test-id")) {

            String rosterId = event.getValues().getFirst();
            new EditRosterSlash(rosterRepository).showRoster(
                    event.getHook(),
                    event.getGuild(),
                    rosterRepository.findById(rosterId).orElseThrow());
        }
    }
}
