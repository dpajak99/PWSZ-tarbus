package com.tarbus.repositories.jpa;

import com.tarbus.models.FileObject;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilesRepository extends CrudRepository<FileObject, String> {

}
