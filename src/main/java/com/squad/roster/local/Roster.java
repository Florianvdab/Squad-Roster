package com.squad.roster.local;

import jakarta.persistence.*;
import lombok.*;

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
    private String name;

    @OneToMany
    private List<Squad> squads;

    public Roster(String name) {
        this.name = name;
    }

    public void addSquad(Squad squad) {
        squads.add(squad);
    }
}
