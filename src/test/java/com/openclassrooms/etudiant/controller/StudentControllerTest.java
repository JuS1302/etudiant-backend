package com.openclassrooms.etudiant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.etudiant.dto.StudentRequestDTO;
import com.openclassrooms.etudiant.entities.Student;
import com.openclassrooms.etudiant.entities.User;
import com.openclassrooms.etudiant.repository.StudentRepository;
import com.openclassrooms.etudiant.repository.UserRepository;
import com.openclassrooms.etudiant.service.JwtService;
import com.openclassrooms.etudiant.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Tests d'intégration des routes /api/students : de la requête HTTP jusqu'à une vraie base MySQL (Docker).
 * Toutes les routes exigent un token : on connecte un agent avant chaque test.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class StudentControllerTest {

    private static final String URL = "/api/students";
    private static final String FIRST_NAME = "Marie";
    private static final String LAST_NAME = "Curie";
    private static final String EMAIL = "marie@test.fr";

    @Container
    // Version fixée : "mysql:latest" (MySQL 26.x) refuse une option de config ajoutée par Testcontainers.
    static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.4");

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;

    private String bearerToken;

    /**
     * Branche l'application sur la base MySQL de test au lieu de la base habituelle.
     */
    @DynamicPropertySource
    static void configureTestProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> mySQLContainer.getJdbcUrl());
        registry.add("spring.datasource.username", () -> mySQLContainer.getUsername());
        registry.add("spring.datasource.password", () -> mySQLContainer.getPassword());
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
    }

    /**
     * Avant chaque test : inscrit un agent en base et prépare son header "Authorization: Bearer <token>".
     */
    @BeforeEach
    public void beforeEach() {
        User agent = new User();
        agent.setFirstName("John");
        agent.setLastName("Doe");
        agent.setLogin("agent");
        agent.setPassword("password");
        userService.register(agent);
        bearerToken = "Bearer " + jwtService.generateToken(agent);
    }

    /**
     * Après chaque test : vide les tables pour que les tests ne se gênent pas.
     */
    @AfterEach
    public void afterEach() {
        studentRepository.deleteAll();
        userRepository.deleteAll();
    }

    /**
     * Enregistre directement un étudiant en base, pour les tests qui ont besoin d'un étudiant existant.
     */
    private Student saveStudent() {
        Student student = new Student();
        student.setFirstName(FIRST_NAME);
        student.setLastName(LAST_NAME);
        student.setEmail(EMAIL);
        return studentRepository.save(student);
    }

    /**
     * Fabrique le JSON envoyé par le front pour créer ou modifier un étudiant.
     */
    private String buildRequestBody(String lastName) throws Exception {
        StudentRequestDTO studentRequest = new StudentRequestDTO();
        studentRequest.setFirstName(FIRST_NAME);
        studentRequest.setLastName(lastName);
        studentRequest.setEmail(EMAIL);
        return objectMapper.writeValueAsString(studentRequest);
    }

    /**
     * POST /api/students crée un étudiant : réponse 201 avec l'étudiant et son id.
     */
    @Test
    public void createStudentSuccessful() throws Exception {
        // WHEN : on envoie un nouvel étudiant, avec le token
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .header("Authorization", bearerToken)
                        .content(buildRequestBody(LAST_NAME))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                // THEN : 201, et l'étudiant renvoyé a un id et les bonnes valeurs
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value(EMAIL));

        // THEN : l'étudiant est bien en base
        assertThat(studentRepository.findAll()).hasSize(1);
    }

    /**
     * GET /api/students renvoie la liste des étudiants : réponse 200.
     */
    @Test
    public void findAllStudentsSuccessful() throws Exception {
        // GIVEN : un étudiant existe en base
        saveStudent();

        // WHEN / THEN : 200, et la liste contient 1 étudiant
        mockMvc.perform(MockMvcRequestBuilders.get(URL)
                        .header("Authorization", bearerToken))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].firstName").value(FIRST_NAME));
    }

    /**
     * GET /api/students/{id} renvoie le détail d'un étudiant : réponse 200.
     */
    @Test
    public void findStudentByIdSuccessful() throws Exception {
        // GIVEN : un étudiant existe en base
        Student savedStudent = saveStudent();

        // WHEN / THEN : 200, avec les infos de cet étudiant
        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/" + savedStudent.getId())
                        .header("Authorization", bearerToken))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").value(savedStudent.getId()))
                .andExpect(jsonPath("$.lastName").value(LAST_NAME));
    }

    /**
     * PUT /api/students/{id} modifie un étudiant : réponse 200 avec les nouvelles valeurs.
     */
    @Test
    public void updateStudentSuccessful() throws Exception {
        // GIVEN : un étudiant existe en base
        Student savedStudent = saveStudent();

        // WHEN : on change son nom
        mockMvc.perform(MockMvcRequestBuilders.put(URL + "/" + savedStudent.getId())
                        .header("Authorization", bearerToken)
                        .content(buildRequestBody("Sklodowska-Curie"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                // THEN : 200, avec le nouveau nom
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.lastName").value("Sklodowska-Curie"));
    }

    /**
     * DELETE /api/students/{id} supprime un étudiant : réponse 204, et il n'est plus en base.
     */
    @Test
    public void deleteStudentSuccessful() throws Exception {
        // GIVEN : un étudiant existe en base
        Student savedStudent = saveStudent();

        // WHEN / THEN : 204 (pas de contenu)
        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/" + savedStudent.getId())
                        .header("Authorization", bearerToken))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // THEN : la table est vide
        assertThat(studentRepository.findAll()).isEmpty();
    }
}
