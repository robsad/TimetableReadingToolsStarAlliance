package app.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AirConnectionRepository extends
		CrudRepository<AirConnection, Long> {
	List<AirConnection> findByDestination(
			@Param("destination") String destination);
}
