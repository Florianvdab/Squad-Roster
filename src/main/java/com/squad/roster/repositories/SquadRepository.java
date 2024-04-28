package com.squad.roster.repositories;

import com.squad.roster.local.Squad;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public interface SquadRepository extends CrudRepository<Squad, Long> {


}
