package com.micro.repository;

import com.micro.model.SongMetaData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongMetaDataRepository extends CrudRepository<SongMetaData, Long> {
}