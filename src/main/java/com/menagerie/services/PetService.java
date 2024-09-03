package com.menagerie.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.menagerie.entity.Event;
import com.menagerie.entity.Pet;
import com.menagerie.repository.IEventRepository;
import com.menagerie.repository.IPetRepository;


@Service
public class PetService {
	
	 private static final Logger logger = LoggerFactory.getLogger(PetService.class);
	@Autowired
	private IPetRepository petRepository;
	
	@Autowired
	private IEventRepository eventRepository;
	
	 public List<Pet> getAllPets(String species) {
	        logger.info("Received request to get all pets with species filter: {}", species);
	        
	        List<Pet> pets;
	        if (species != null && !species.isEmpty()) {
	            logger.info("Fetching pets with species: {}", species);
	            pets = petRepository.findBySpecies(species);
	        } else {
	            logger.info("Fetching all pets");
	            pets = petRepository.findAll();
	        }
	        
	        logger.info("Number of pets retrieved: {}", pets.size());
	        return pets;
	    }
	
	  public Optional<Pet> getPetById(int id) {
	        logger.info("Fetching pet with ID: {}", id);
	        Optional<Pet> pet = petRepository.findById(id);
	        if (pet.isPresent()) {
	            logger.info("Pet found: {}", pet.get());
	        } else {
	            logger.warn("Pet with ID: {} not found", id);
	        }
	        return pet;
	    }
	
	  public List<Event> getPetEvents(Integer petId, String sortKey, String sortOrder) {
	        logger.info("Fetching events for pet with ID: {} with sorting by {} {}", petId, sortKey, sortOrder);
	        
	        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortKey);
	        
	        List<Event> events = eventRepository.findByPetId(petId, sort);
	        
	        logger.info("Found {} events for pet with ID: {}", events.size(), petId);
	        return events;
	    }
	  
	  
	  public Pet addPet(Pet pet)
	  {
		  try {
	            return petRepository.save(pet);
	        } catch (Exception e) {
	            logger.error("Constraint violation while creating pet: {}", e.getMessage());
	            throw new IllegalArgumentException("Constraint violation: " + e.getMessage());
	        }
	  }
	  
	  public Pet updatePet(int id, Pet pet) {
	        try {
	            // Find the existing pet by ID
	            Pet existingPet = petRepository.findById(id)
	                .orElseThrow(() -> new IllegalArgumentException("Pet not found with ID: " + id));

	            // Update the pet fields
	            existingPet.setName(pet.getName());
	            existingPet.setOwner(pet.getOwner());
	            existingPet.setSpecies(pet.getSpecies());
	            existingPet.setSex(pet.getSex());
	            existingPet.setBirth(pet.getBirth());
	            existingPet.setDeath(pet.getDeath());

	            // Save the updated pet
	            return petRepository.save(existingPet);
	        } catch (Exception e) {
	            logger.error("Constraint violation while updating pet: {}", e.getMessage());
	            throw new IllegalArgumentException("Constraint violation: " + e.getMessage());
	        }
	    }
	  
	  public Event addEventToPet(int petId, Event event) {
	        try {
	            Pet pet = petRepository.findById(petId)
	                    .orElseThrow(() -> new IllegalArgumentException("Pet not found with ID: " + petId));
	            
	            
	            event.setPet(pet);

	            return eventRepository.save(event);
	        } catch (Exception e) {
	            logger.error("Constraint violation while adding event: {}", e.getMessage());
	            throw new IllegalArgumentException("Constraint violation: " + e.getMessage());
	        }
	    }
	  
	  public void deletePet(int petId) {
	        try {
	            // Check if the pet exists
	            if (!petRepository.existsById(petId)) {
	                throw new IllegalArgumentException("Pet not found with ID: " + petId);
	            }

	            petRepository.deleteById(petId);
	        } catch (Exception e) {
	            logger.error("Constraint violation while deleting pet: {}", e.getMessage());
	            throw new IllegalArgumentException("Cannot delete pet due to constraints: " + e.getMessage());
	        }
	    }
	
	

}
