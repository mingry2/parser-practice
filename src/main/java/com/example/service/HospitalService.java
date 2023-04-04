package com.example.service;

import com.example.parser.ReadLineContext;
import com.example.domain.dto.HospitalDto;
import com.example.domain.entity.Hospital;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@Slf4j
public class HospitalService {

	private final ReadLineContext<Hospital> readLineContext;

	private final HospitalDto hospitalDto;

	public HospitalService(ReadLineContext<Hospital> readLineContext, HospitalDto hospitalDto) {
		this.readLineContext = readLineContext;
		this.hospitalDto = hospitalDto;
	}

//	@Transactional
	public int insertLargeVolumeHospitalData(String fileName) {
		log.trace("Insert large volume hospital data.");

		int cnt = 0;
		try {
			List<Hospital> hospitals = readLineContext.readByLine(fileName);
			log.debug("Hospitals size: {}", hospitals.size());
			try {
				this.hospitalDto.add(hospitals);
				cnt++;
			} catch (Exception e) {
				log.error("Exception: {}", e);
				throw new RuntimeException(e);
			}

		} catch (IOException e) {
			log.error("IOException: {}", e);
			throw new RuntimeException(e);
		}
		return cnt;

	}

}
