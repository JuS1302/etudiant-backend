package com.openclassrooms.etudiant.service;

import com.openclassrooms.etudiant.entities.Student;
import com.openclassrooms.etudiant.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests unitaires du StudentService : le repository est remplacé par un mock (aucune base de données).
 */
@ExtendWith(SpringExtension.class)
public class StudentServiceTest {
    private static final Long ID = 1L;
    private static final String FIRST_NAME = "Marie";
    private static final String LAST_NAME = "Curie";
    private static final String EMAIL = "marie@test.fr";

    @Mock
    private StudentRepository studentRepository;
    @InjectMocks
    private StudentService studentService;

    /**
     * Fabrique un étudiant de test avec les valeurs données.
     */
    private Student buildStudent(Long id, String firstName, String lastName, String email) {
        Student student = new Student();
        student.setId(id);
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setEmail(email);
        return student;
    }

    /**
     * findAll() renvoie tous les étudiants fournis par le repository.
     */
    @Test
    public void test_find_all_students() {
        // GIVEN : le repository contient 2 étudiants
        Student marie = buildStudent(1L, FIRST_NAME, LAST_NAME, EMAIL);
        Student pierre = buildStudent(2L, "Pierre", "Curie", "pierre@test.fr");
        when(studentRepository.findAll()).thenReturn(List.of(marie, pierre));

        // WHEN
        List<Student> foundStudents = studentService.findAll();

        // THEN : on récupère bien ces 2 étudiants
        assertThat(foundStudents).containsExactly(marie, pierre);
    }

    /**
     * findById() renvoie l'étudiant trouvé par le repository.
     */
    @Test
    public void test_find_student_by_id() {
        // GIVEN : le repository trouve l'étudiant n°1
        Student marie = buildStudent(ID, FIRST_NAME, LAST_NAME, EMAIL);
        when(studentRepository.findById(ID)).thenReturn(Optional.of(marie));

        // WHEN
        Student foundStudent = studentService.findById(ID);

        // THEN
        assertThat(foundStudent).isEqualTo(marie);
    }

    /**
     * create() enregistre un étudiant dont l'email n'est pas encore utilisé.
     */
    @Test
    public void test_create_student() {
        // GIVEN : un nouvel étudiant, dont l'email n'existe pas encore en base
        Student marie = buildStudent(null, FIRST_NAME, LAST_NAME, EMAIL);
        when(studentRepository.existsByEmail(EMAIL)).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenReturn(marie);

        // WHEN
        Student studentCreated = studentService.create(marie);

        // THEN : save() a été appelé avec cet étudiant, et le service renvoie l'étudiant enregistré
        ArgumentCaptor<Student> studentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(studentCaptor.capture());
        assertThat(studentCaptor.getValue()).isEqualTo(marie);
        assertThat(studentCreated).isEqualTo(marie);
    }

    /**
     * update() recopie les nouvelles valeurs dans l'étudiant existant, puis l'enregistre.
     */
    @Test
    public void test_update_student() {
        // GIVEN : l'étudiant n°1 existe, et on veut changer son nom (même email)
        Student existingStudent = buildStudent(ID, FIRST_NAME, LAST_NAME, EMAIL);
        Student studentWithNewName = buildStudent(null, FIRST_NAME, "Sklodowska-Curie", EMAIL);
        when(studentRepository.findById(ID)).thenReturn(Optional.of(existingStudent));
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // WHEN
        Student updatedStudent = studentService.update(ID, studentWithNewName);

        // THEN : l'étudiant enregistré garde son id et a le nouveau nom
        assertThat(updatedStudent.getId()).isEqualTo(ID);
        assertThat(updatedStudent.getLastName()).isEqualTo("Sklodowska-Curie");
        assertThat(updatedStudent.getEmail()).isEqualTo(EMAIL);
    }

    /**
     * delete() supprime l'étudiant trouvé par son id.
     */
    @Test
    public void test_delete_student() {
        // GIVEN : l'étudiant n°1 existe
        Student marie = buildStudent(ID, FIRST_NAME, LAST_NAME, EMAIL);
        when(studentRepository.findById(ID)).thenReturn(Optional.of(marie));

        // WHEN
        studentService.delete(ID);

        // THEN : le repository a reçu l'ordre de supprimer cet étudiant
        verify(studentRepository).delete(marie);
    }
}
