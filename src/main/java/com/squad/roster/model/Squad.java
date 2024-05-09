package com.squad.roster.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Squad {
    @Id
    private final String id = UUID.randomUUID().toString();

    @ManyToOne
    @JoinColumn(name = "roster_id")
    private Roster roster;

    private String name;
    private String connectedRoleId;

    public Squad(Roster roster, String name) {
        this.roster = roster;
        this.name = name;
    }

    public void attachRole(String roleId) {
        this.connectedRoleId = roleId;
    }
}
