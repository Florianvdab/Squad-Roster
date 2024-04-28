package com.squad.roster.repositories;

import com.squad.roster.local.Roster;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Component
@Repository
public interface RosterRepository extends CrudRepository<Roster, Long> {
    @NotNull
    @Override
    Optional<Roster> findById(@NotNull Long aLong);

    //make a function that checks if a roster exists
    @NotNull
    Optional<Roster> findByName(@NotNull String name);
}
