package com.squad.roster.util;

import com.squad.roster.EventConstants;
import com.squad.roster.model.Roster;
import com.squad.roster.model.Squad;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public abstract class StringUtil {
    public static boolean isNotNullOrEmpty(String string) {
        return string != null && !string.isEmpty();
    }

    public static void showEditRoster(InteractionHook hook, Guild guild, Roster roster) {
        hook.sendMessage("Roster: " + roster.getName())
                .setEphemeral(true)
                .addActionRow(
                        Button.primary(EventConstants.RENAME_ROSTER_BUTTON + roster.getId(), "Change name"),
                        Button.success(EventConstants.CREATE_SQUAD_BUTTON, "Add a squad"),
                        Button.danger(EventConstants.DELETE_ROSTER_BUTTON + roster.getId(), "Delete roster"))
                .queue();

        roster.getSquads().forEach(squad -> showEditSquad(hook, guild, squad));
    }

    public static void showEditSquad(InteractionHook hook, Guild guild, Squad squad) {
        hook.sendMessage(getSquadString(guild, squad))
                .setEphemeral(true)
                .addActionRow(
                        Button.primary(EventConstants.RENAME_SQUAD_BUTTON + squad.getId(), "Change name"),
                        Button.secondary(EventConstants.ATTACH_ROLE_SQUAD_BUTTON + squad.getId(), "Change role"),
                        Button.danger(EventConstants.DELETE_SQUAD_BUTTON + squad.getId(), "Delete squad")
                ).queue();
    }

    public static String getSquadString(Guild guild, Squad squad) {
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

        return sb.toString();
    }

}
