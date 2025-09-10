package com.plexus.infraestructure.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.plexus.infraestructure.persistance.entity.AssetEntity; 

public interface AssetJpaRepository extends JpaRepository<AssetEntity, String> {
}


