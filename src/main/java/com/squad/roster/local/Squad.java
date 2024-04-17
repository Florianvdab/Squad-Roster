package com.squad.roster.local;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Squad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Roster roster;

    @OneToMany
    private List<SquadMember> squadMembers;

    private final String name;
    private final Long connectedRoleId;

    public Squad(String name, Long connectedRoleId) {
        this.name = name;
        this.connectedRoleId = connectedRoleId;
    }

    public void addSquadMember(SquadMember squadMember) {
        squadMembers.add(squadMember);
    }
}
