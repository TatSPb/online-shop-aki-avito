package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.ImageModel;

/*** Image repository / Репозиторий изображений ***/
@Repository
public interface ImageRepository extends JpaRepository<ImageModel,Integer> {


    void deleteImageModelsByAdsModel_Id(Integer id);
}
