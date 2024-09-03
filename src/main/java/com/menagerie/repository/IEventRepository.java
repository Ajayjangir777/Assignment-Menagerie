package com.menagerie.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.menagerie.entity.Event;

@Repository
public interface IEventRepository extends JpaRepository<Event, Integer> {

	
	List<Event> findByPetId(Integer pet_id , Sort sort);
}
