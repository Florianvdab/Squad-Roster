package com.squad.roster.command.slash;

import com.squad.roster.repositories.RosterRepository;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EditRosterSlash implements SlashCommand {

    @Autowired
    private RosterRepository rosterRepository;

    public EditRosterSlash() {
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
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
}