package com.menagerie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.menagerie.entity.Pet;

@Repository
public interface IPetRepository extends JpaRepository<Pet, Integer> {

	
	List<Pet> findBySpecies(String species);
}
