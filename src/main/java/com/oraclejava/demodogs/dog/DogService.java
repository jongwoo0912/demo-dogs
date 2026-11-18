package com.oraclejava.demodogs.dog;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
public class DogService {

	private final DogRepository repository;

	public DogService(DogRepository repository) {
		this.repository = repository;
	}

	public List<DogResponse> findAll() {
		return repository.findAll().stream().map(DogResponse::from).toList();
	}

	/** 단건 조회 시 조회수를 1 증가시킨다. */
	@Transactional
	public DogResponse findById(Long id) {
		Dog dog = getDog(id);
		dog.increaseReadcount();
		return DogResponse.from(dog);
	}

	@Transactional
	public DogResponse create(DogRequest request) {
		return DogResponse.from(repository.save(new Dog(request)));
	}

	@Transactional
	public DogResponse update(Long id, DogRequest request) {
		Dog dog = getDog(id);
		dog.update(request);
		return DogResponse.from(dog);
	}

	@Transactional
	public void delete(Long id) {
		repository.delete(getDog(id));
	}

	private Dog getDog(Long id) {
		return repository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dog not found: " + id));
	}

}
