package com.squad.roster.util;

import com.squad.roster.EventConstants;
import com.squad.roster.model.Roster;
import com.squad.roster.model.Squad;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.SequencedCollection;

public abstract class EventUtil {
    public static boolean isNotNullOrEmpty(String string) {
        return string != null && !string.isEmpty();
    }

    public static void showEditRoster(InteractionHook hook, Guild guild, Roster roster) {
        hook.sendMessage("Roster: " + roster.getName())
                .setEphemeral(true)
                .addActionRow(getRosterComponents(roster))
                .queue();

        roster.getSquads().forEach(squad -> showEditSquad(hook, guild, squad));
    }

    public static void showEditSquad(InteractionHook hook, Guild guild, Squad squad) {
        hook.sendMessage(getSquadString(guild, squad))
                .setEphemeral(true)
                .setActionRow(getSquadComponents(squad))
                .queue();
    }

    public static String getSquadString(Guild guild, Squad squad) {
        StringBuilder sb = new StringBuilder();

        sb.append("Squad: ");
        sb.append(squad.getName());
        sb.append(" (");
        if (squad.getConnectedRoleId() != null) {
            Role role = guild.getRoleById(squad.getConnectedRoleId());
            sb.append(role.getAsMention());
            sb.append(")");

            sb.append("\n");
            sb.append("Members:");

            guild.getMembersWithRoles(role).forEach((bar) -> {
                sb.append("\n");
                sb.append(bar.getAsMention());
            });
        } else {
            sb.append("Role not found)");
        }

        return sb.toString();
    }

    public static SequencedCollection<ItemComponent> getRosterComponents(Roster roster) {
        List<ItemComponent> components = new ArrayList<>();
        components.add(Button.primary(EventConstants.RENAME_ROSTER_BUTTON + roster.getId(), "Change name"));
        components.add(Button.success(EventConstants.CREATE_SQUAD_BUTTON + roster.getId(), "Add a squad"));
        components.add(Button.danger(EventConstants.DELETE_ROSTER_BUTTON + roster.getId(), "Delete roster"));
        return components;
    }

    public static SequencedCollection<ItemComponent> getSquadComponents(Squad squad) {
        List<ItemComponent> components = new ArrayList<>();
        components.add(Button.primary(EventConstants.RENAME_SQUAD_BUTTON + squad.getId(), "Change name"));
        components.add(Button.secondary(EventConstants.ATTACH_ROLE_SQUAD_BUTTON + squad.getId(), "Change role"));
        components.add(Button.danger(EventConstants.DELETE_SQUAD_BUTTON + squad.getId(), "Delete squad"));
        return components;
    }

    public static void getBaseView(Roster first, InteractionHook hook) {
        StringBuilder sb = new StringBuilder();
        sb.append("Roster: ");
        sb.append(first.getName());
        sb.append("\n");
        sb.append("Squads:");
        first.getSquads().forEach((squad) -> {
            sb.append("\n");
            sb.append(squad.getName());
        });
        hook.sendMessage(sb.toString())
                .queue();
    }
}