package com.micro.repository;

import com.micro.model.AudioFileDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AudioFileRepository extends CrudRepository<AudioFileDetails, Long> {

}