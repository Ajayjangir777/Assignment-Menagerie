package com.menagerie.entity;

import java.time.LocalDate;
import java.util.List;

import javax.annotation.Generated;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String name;
	
	private String owner;
	
	private String species;
	
	@Enumerated(EnumType.STRING)
	private Sex sex;
	
	private LocalDate birth;
	
	private LocalDate death;
	
	@OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonBackReference
	private List<Event> events;
}
