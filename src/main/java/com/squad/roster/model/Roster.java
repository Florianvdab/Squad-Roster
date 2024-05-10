package com.squad.roster.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Roster {
    @Id
    private String id = UUID.randomUUID().toString();

    private String guildId;
    private String name;

    @OneToMany(mappedBy = "roster", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Squad> squads;

    public Roster(String name, String guildId) {
        this.guildId = guildId;
        this.name = name;
    }
}
