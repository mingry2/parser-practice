package com.example.controller;

import com.example.domain.dto.HospitalDto;
import com.example.domain.entity.Hospital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/hospital")
public class HospitalController {
	private final HospitalDto hospitalDto;

	@Autowired
	public HospitalController(HospitalDto hospitalDto) {
		this.hospitalDto = hospitalDto;
	}

	@GetMapping("/{id}")
	public ResponseEntity<Hospital> get(@PathVariable Integer id) {
		Hospital hospital = hospitalDto.findById(id);
		Optional<Hospital> opt = Optional.of(hospital);

		if (!opt.isEmpty()) {
			return ResponseEntity.ok().body(hospital);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Hospital());

		}

	}

}
