package ru.otus.crm.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import ru.otus.crm.model.Client;

import java.util.List;
import java.util.Optional;


public interface ClientRepository extends CrudRepository<Client, String> {

    Optional<Client> findByName(String name);

    @Override
    @Query(value = """
            select c.id           as client_id,
                   c.name         as client_name,
                   p.id           as phone_id,
                   p.number       as phone_number,
                   a.street       as address_street
            from client c
                     left outer join phone p
                                     on c.id = p.client_id
                     left outer join address a
                                     on a.client = c.id
            order by c.id
                                                          """,
            resultSetExtractorClass = ClientResultSetExtractor.class)
    List<Client> findAll();
}
