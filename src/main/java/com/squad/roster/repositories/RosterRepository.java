package com.squad.roster.repositories;

import com.squad.roster.model.Roster;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RosterRepository extends CrudRepository<Roster, String> {
    @NotNull
    @Override
    Optional<Roster> findById(@NotNull String string);

    //make a function that checks if a roster exists
    Optional<Roster> findByName(@NotNull String name);
}