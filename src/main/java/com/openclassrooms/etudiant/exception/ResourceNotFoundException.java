package com.openclassrooms.etudiant.exception;

/**
 * Levée quand une ressource demandée (ex : un étudiant) n'existe pas. Renvoie une 404.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
