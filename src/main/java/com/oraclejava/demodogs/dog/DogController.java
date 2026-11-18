package com.oraclejava.demodogs.dog;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dogs")
public class DogController {

	private final DogService service;

	public DogController(DogService service) {
		this.service = service;
	}

	@GetMapping
	public List<DogResponse> list() {
		return service.findAll();
	}

	@GetMapping("/{id}")
	public DogResponse get(@PathVariable Long id) {
		return service.findById(id);
	}

	@PostMapping
	public ResponseEntity<DogResponse> create(@Valid @RequestBody DogRequest request) {
		DogResponse created = service.create(request);
		return ResponseEntity.created(URI.create("/api/dogs/" + created.id())).body(created);
	}

	@PutMapping("/{id}")
	public DogResponse update(@PathVariable Long id, @Valid @RequestBody DogRequest request) {
		return service.update(id, request);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}
