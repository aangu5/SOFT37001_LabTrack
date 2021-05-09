package org.ordep.labtrack.data;

import org.ordep.labtrack.model.Sym;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface SymRepository extends PagingAndSortingRepository<Sym, UUID> {
    List<Sym> findByDataIdIn(List<UUID> uuids);
    Sym findByDataId(UUID uuid);
}
