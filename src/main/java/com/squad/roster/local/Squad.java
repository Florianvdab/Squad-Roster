package com.squad.roster.local;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Squad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "roster_id")
    private Roster roster;

    @OneToMany
    private List<SquadMember> squadMembers;

    private String name;
    private Long connectedRoleId;

    public Squad(Roster roster, String name) {
        this.roster = roster;
        this.name = name;
    }

    public void attachRole(Long roleId) {
        this.connectedRoleId = roleId;
    }

    public void addSquadMember(SquadMember squadMember) {
        squadMembers.add(squadMember);
    }
}
