package com.openclassrooms.etudiant.service;

import com.openclassrooms.etudiant.entities.Student;
import com.openclassrooms.etudiant.exception.ResourceNotFoundException;
import com.openclassrooms.etudiant.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;

    /**
     * Enregistre un nouvel étudiant, si son email n'est pas déjà utilisé.
     */
    public Student create(Student student) {
        Assert.notNull(student, "Student must not be null");
        log.info("Creating new student");
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new IllegalArgumentException("Student with email " + student.getEmail() + " already exists");
        }
        return studentRepository.save(student);
    }

    /**
     * Renvoie la liste de tous les étudiants.
     */
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    /**
     * Renvoie un étudiant par son id, ou lève une 404 s'il n'existe pas.
     */
    public Student findById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student with id " + id + " not found"));
    }

    /**
     * Modifie un étudiant existant avec les nouvelles valeurs reçues.
     */
    public Student update(Long id, Student newValues) {
        Assert.notNull(newValues, "Student must not be null");
        Student student = findById(id);
        boolean emailChanged = !student.getEmail().equals(newValues.getEmail());
        if (emailChanged && studentRepository.existsByEmail(newValues.getEmail())) {
            throw new IllegalArgumentException("Student with email " + newValues.getEmail() + " already exists");
        }
        student.setFirstName(newValues.getFirstName());
        student.setLastName(newValues.getLastName());
        student.setEmail(newValues.getEmail());
        return studentRepository.save(student);
    }

    /**
     * Supprime un étudiant, ou lève une 404 s'il n'existe pas.
     */
    public void delete(Long id) {
        Student student = findById(id);
        studentRepository.delete(student);
    }
}
