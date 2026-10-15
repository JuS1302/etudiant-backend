package com.openclassrooms.etudiant.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Données envoyées par le front pour créer ou modifier un étudiant.
 */
@Data
public class StudentRequestDTO {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    @Email
    private String email;
}
