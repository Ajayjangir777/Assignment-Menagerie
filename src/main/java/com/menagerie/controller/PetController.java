package com.menagerie.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.menagerie.entity.Event;
import com.menagerie.entity.Pet;
import com.menagerie.services.PetService;

@RestController
public class PetController {
	
	private static final Logger logger = LoggerFactory.getLogger(PetController.class);

	
	@Autowired
	private PetService petService;
	
	@GetMapping("/pets")
	public ResponseEntity<List<Pet>> getAllPets(@RequestParam(required = false) String species)
	{
		logger.info("Getting data with specific species",species);
		return ResponseEntity.ok(petService.getAllPets(species));
	}
	
	
	@GetMapping("/pets/{id}")
    public ResponseEntity<Map<String, Object>> getPetAndEvents(
            @PathVariable Integer id,
            @RequestParam(required = false, defaultValue = "date") String sortKey,
            @RequestParam(required = false, defaultValue = "DESC") String sortOrder) {

        logger.info("Received request to get pet with ID: {} and its events", id);
        Optional<Pet> pet = petService.getPetById(id);
        if (pet.isPresent()) {
            List<Event> events = petService.getPetEvents(id, sortKey, sortOrder);
            Map<String, Object> response = new HashMap<>();
            response.put("pet", pet.get());
            response.put("events", events);
            logger.info("Returning pet and events for pet with ID: {}", id);
            return ResponseEntity.ok(response);
        } else {
            logger.error("Pet with ID: {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }
	
	 @PostMapping("/pets")
	    public ResponseEntity<Pet> addPet(@RequestBody Pet pet) {
	        logger.info("Received request to add a new pet: {}", pet.getName());
	        try {
	            Pet createdPet = petService.addPet(pet);
	            logger.info("Pet created successfully with ID: {}", createdPet.getId());
	            return ResponseEntity.status(HttpStatus.CREATED).body(createdPet);
	        } catch (IllegalArgumentException e) {
	            logger.error("Error creating pet: {}", e.getMessage());
	            return ResponseEntity.badRequest().body(null);
	        }
	    }
	 
	 @PutMapping("pets/{id}")
	    public ResponseEntity<Pet> updatePet(@PathVariable("id") int id, @RequestBody Pet pet) {
	        logger.info("Received request to update pet with ID: {}", id);
	        try {
	            Pet updatedPet = petService.updatePet(id, pet);
	            logger.info("Pet updated successfully with ID: {}", updatedPet.getId());
	            return ResponseEntity.ok(updatedPet);
	        } catch (IllegalArgumentException e) {
	            logger.error("Error updating pet: {}", e.getMessage());
	            return ResponseEntity.badRequest().body(null);
	        }
	    }
	 
	 
	 @PostMapping("/{id}/events")
	    public ResponseEntity<Event> addEventToPet(@PathVariable("id") int id, @RequestBody Event event) {
	        logger.info("Received request to add an event to pet with ID: {}", id);
	        try {
	            Event createdEvent = petService.addEventToPet(id, event);
	            logger.info("Event created successfully for pet ID: {}", id);
	            return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
	        } catch (IllegalArgumentException e) {
	            logger.error("Error adding event: {}", e.getMessage());
	            return ResponseEntity.badRequest().body(null);
	        }
	    }
	 
	 @DeleteMapping("/pet/{id}")
	 public ResponseEntity<Void> deletePet(@PathVariable("id") int id) {
	        logger.info("Received request to delete pet with ID: {}", id);
	        try {
	            petService.deletePet(id);
	            logger.info("Pet with ID: {} deleted successfully", id);
	            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204 No Content
	        } catch (IllegalArgumentException e) {
	            logger.error("Error deleting pet: {}", e.getMessage());
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
	        }
	    }
	
}
