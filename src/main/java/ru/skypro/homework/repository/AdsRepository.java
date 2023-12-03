package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.AdsModel;

import java.util.List;

/*** Ad repository / Репозиторий объявлений ***/
@Repository
public interface AdsRepository extends JpaRepository <AdsModel, Integer> {
    @Query(value = "select * from ads order by id", nativeQuery = true)
    List<AdsModel> findAllAds();
    List<AdsModel> findAllByUserModelId(Integer userModelId);

    void deleteAllById(Integer adsId);
    List<AdsModel> findByTitleContainingIgnoreCaseOrderByTitle(String title);

}
