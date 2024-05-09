package com.squad.roster.repositories;

import com.squad.roster.model.Roster;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RosterRepository extends JpaRepository<Roster, String> {

    @NotNull
    Optional<Roster> findById(@NotNull String string);

    @NotNull
    Optional<Roster> findByName(@NotNull String name);

    List<Roster> findAllByGuildId(String guildId);
}