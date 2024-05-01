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
@AllArgsConstructor
@NoArgsConstructor
public class Roster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String guildId;
    private String name;

    @OneToMany(mappedBy = "roster", fetch = FetchType.EAGER)
    private List<Squad> squads;

    public Roster(String name, String guildId) {
        this.guildId = guildId;
        this.name = name;
    }

    public void addSquad(Squad squad) {
        squads.add(squad);
    }
}
