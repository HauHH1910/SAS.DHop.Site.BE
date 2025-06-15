package com.sas.dhop.site.repository;

import com.sas.dhop.site.model.Area;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AreaRepository
    extends JpaRepository<Area, Integer>, JpaSpecificationExecutor<Area> {

  Optional<Area> findByCity(String city);

  Optional<Area> findByWard(String ward);

  Optional<Area> findByDistrict(String district);

  @Query(
      "SELECT a FROM Area a WHERE LOWER(a.city) = LOWER(:city) AND LOWER(a.district) = LOWER(:district) AND LOWER(a.ward) = LOWER(:ward)")
  Optional<Area> findByLocation(
      @Param("city") String city, @Param("district") String district, @Param("ward") String ward);

  @Query(
      "SELECT COUNT(a) > 0 FROM Area a WHERE LOWER(a.city) = LOWER(:city) AND LOWER(a.district) = LOWER(:district) AND LOWER(a.ward) = LOWER(:ward)")
  boolean existsByLocation(
      @Param("city") String city, @Param("district") String district, @Param("ward") String ward);

  //    @Query("SELECT a FROM Area a WHERE a.status.statusName = :statusName")
  //    List<Area> findAreaByStatus(@Param("statusName") AreaStatus areaStatusName);

  @Query("SELECT a FROM Area a WHERE a.status.statusName = :status")
  List<Area> findAreaByStatus(@Param("status") String status);

  @Query("SELECT a FROM Area  a WHERE a.id = :id AND a.status.statusName = :status")
  Area findAreaByIdAndStatus(@Param("id") Integer id, @Param("status") String status);
}
