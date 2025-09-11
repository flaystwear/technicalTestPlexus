package com.plexus.infraestructure.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.plexus.infraestructure.persistance.entity.AssetEntity; 

@Repository
public interface AssetRepository extends JpaRepository<AssetEntity, String>, JpaSpecificationExecutor<AssetEntity> {
}


