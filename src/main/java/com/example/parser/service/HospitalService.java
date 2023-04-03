package com.example.parser.service;

import com.example.parser.domain.dto.HospitalDto;
import com.example.parser.domain.entity.Hospital;
import com.example.parser.parser.ReadLineContext;
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

	@Transactional
	public int insertLargeVolumeHospitalData(String fileName) {
		log.trace("Insert large volume hospital data.");

		int cnt = 0;
		try {

			List<Hospital> hospitals = readLineContext.readByLine(fileName);

			for (Hospital hospital : hospitals) {
				log.debug("Hospital: {}", hospital);
				try {

					this.hospitalDto.add(hospital);
					cnt++;

				} catch (Exception e) {

					log.error("id:%d 레코드에 문제가 있습니다.",hospital.getId());
					throw new RuntimeException(e);

				}
			}

		} catch (IOException e) {

			throw new RuntimeException(e);

		}
		return cnt;
	}

}
