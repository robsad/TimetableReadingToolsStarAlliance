package app.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AirportsDataRepository extends CrudRepository<AirportsData, Long> {
	List<AirportsData> findByCityCode(@Param("cityCode") String cityCode);
}
