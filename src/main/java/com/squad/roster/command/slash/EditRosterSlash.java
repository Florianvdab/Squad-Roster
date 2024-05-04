package com.squad.roster.command.slash;

import com.squad.roster.EventConstants;
import com.squad.roster.repositories.RosterRepository;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.stereotype.Component;

@Component
public class EditRosterSlash implements SlashCommand {


    private final RosterRepository rosterRepository;

    public EditRosterSlash(RosterRepository rosterRepository) {
        this.rosterRepository = rosterRepository;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply().queue();

        rosterRepository.findAll().forEach(roster -> {

            event.getHook().sendMessage("Roster: " + roster.getName())
                    .addActionRow(
                            Button.primary(EventConstants.RENAME_ROSTER_BUTTON_COMMAND + roster.getId(), "Change name"),
                            Button.danger(EventConstants.DELETE_ROSTER_BUTTON_COMMAND + roster.getId(), "Delete roster"),
                            Button.success(EventConstants.CREATE_SQUAD_BUTTON_COMMAND, "Create squad"))
                    .queue();

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
                                Button.primary(EventConstants.ATTACH_ROLE_SQUAD_BUTTON_COMMAND + squad.getId(), "Change role"),
                                Button.secondary(EventConstants.RENAME_SQUAD_BUTTON_COMMAND + squad.getId(), "Change name"),
                                Button.danger(EventConstants.DELETE_SQUAD_BUTTON_COMMAND + squad.getId(), "Delete squad")
                        ).queue();
            });
        });
    }
}