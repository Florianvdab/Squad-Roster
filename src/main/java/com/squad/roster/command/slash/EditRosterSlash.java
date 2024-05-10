package com.squad.roster.command.slash;

import com.squad.roster.EventConstants;
import com.squad.roster.model.Roster;
import com.squad.roster.repositories.RosterRepository;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.squad.roster.EventConstants.EDIT_ROSTER_STRING_SELECT;

@Component
public class EditRosterSlash implements SlashCommand {

    private final RosterRepository rosterRepository;

    public EditRosterSlash(RosterRepository rosterRepository) {
        this.rosterRepository = rosterRepository;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply(true)
                .queue();

        Optional.ofNullable(event.getGuild()).ifPresentOrElse(
                (guild -> {
                    List<Roster> rosters = rosterRepository.findAllByGuildId(guild.getId());

                    if (rosters.isEmpty()) {
                        event.getHook()
                                .sendMessage("No rosters found, please create a roster using the /create-roster command")
                                .queue();
                    }
                    if (rosters.size() == 1) {
                        showRoster(event.getHook(), guild, rosters.getFirst());
                    }
                    if (rosters.size() > 1) {
                        event.getHook()
                                .sendMessage("Select a roster to edit")
                                .addActionRow(
                                        StringSelectMenu.create(EDIT_ROSTER_STRING_SELECT)
                                                .setRequiredRange(1, 1)
                                                .addOptions(
                                                        rosters.stream()
                                                                .map(roster -> SelectOption.of(roster.getName(), roster.getId()))
                                                                .toList())
                                                .build()).
                                queue();
                    }
                }),
                () -> event.getHook()
                        .sendMessage("This command can only be used in a server")
                        .queue()
        );
    }

    public void showRoster(InteractionHook hook, Guild guild, Roster roster) {
        hook.sendMessage("Roster: " + roster.getName())
                .setEphemeral(true)
                .addActionRow(
                        Button.primary(EventConstants.RENAME_ROSTER_BUTTON + roster.getId(), "Change name"),
                        Button.danger(EventConstants.DELETE_ROSTER_BUTTON + roster.getId(), "Delete roster"),
                        Button.success(EventConstants.CREATE_SQUAD_BUTTON, "Create squad"))
                .queue();

        roster.getSquads().forEach(squad -> {
            StringBuilder sb = new StringBuilder();

            Role role = guild.getRoleById(squad.getConnectedRoleId());

            sb.append("Squad: ");
            sb.append(squad.getName());
            sb.append(" (");
            sb.append(role.getAsMention());
            sb.append(")");

            sb.append("\n");
            sb.append("Members:");

            guild.getMembersWithRoles(role).forEach((bar) -> {
                sb.append("\n");
                sb.append(bar.getAsMention());
            });

            hook.sendMessage(sb.toString())
                    .setEphemeral(true)
                    .addActionRow(
                            Button.primary(EventConstants.ATTACH_ROLE_SQUAD_BUTTON + squad.getId(), "Change role"),
                            Button.secondary(EventConstants.RENAME_SQUAD_BUTTON + squad.getId(), "Change name"),
                            Button.danger(EventConstants.DELETE_SQUAD_BUTTON + squad.getId(), "Delete squad")
                    ).queue();
        });
    }
}