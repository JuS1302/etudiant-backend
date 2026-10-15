package com.openclassrooms.etudiant.mapper;

import com.openclassrooms.etudiant.dto.StudentRequestDTO;
import com.openclassrooms.etudiant.dto.StudentResponseDTO;
import com.openclassrooms.etudiant.entities.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface StudentDtoMapper {

    /**
     * Transforme le DTO reçu du front en entité à enregistrer.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created_at", ignore = true)
    @Mapping(target = "updated_at", ignore = true)
    Student toEntity(StudentRequestDTO studentRequestDTO);

    /**
     * Transforme l'entité lue en base en DTO renvoyé au front.
     */
    @Mapping(target = "createdAt", source = "created_at")
    @Mapping(target = "updatedAt", source = "updated_at")
    StudentResponseDTO toDto(Student student);

    /**
     * Transforme une liste d'entités en liste de DTO.
     */
    List<StudentResponseDTO> toDtoList(List<Student> students);
}
