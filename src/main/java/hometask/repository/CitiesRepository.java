package hometask.repository;

import hometask.entity.CityEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CitiesRepository extends PagingAndSortingRepository<CityEntity, Long> {

    List<CityEntity> findByNameIgnoreCase(String name);
}
