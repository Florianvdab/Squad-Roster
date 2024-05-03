package com.squad.roster.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class SquadMember {
    @Id
    private Long discordId;

    private String name;
}
