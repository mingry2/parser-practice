package com.example.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.domain.dto.HospitalDto;
import com.example.domain.entity.Hospital;
import com.example.service.HospitalService;
import java.io.IOException;
import java.time.LocalDateTime;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class HospitalParserTest {

	String line1 = "\"1\",\"의원\",\"01_01_02_P\",\"3620000\",\"PHMA119993620020041100004\",\"19990612\",\"\",\"01\",\"영업/정상\",\"13\",\"영업중\",\"\",\"\",\"\",\"\",\"062-515-2875\",\"\",\"500881\",\"광주광역시 북구 풍향동 565번지 4호 3층\",\"광주광역시 북구 동문대로 24, 3층 (풍향동)\",\"61205\",\"효치과의원\",\"20211115113642\",\"U\",\"2021-11-17 02:40:00.0\",\"치과의원\",\"192630.735112\",\"185314.617632\",\"치과의원\",\"1\",\"0\",\"0\",\"52.29\",\"401\",\"치과\",\"\",\"\",\"\",\"0\",\"0\",\"\",\"\",\"0\",\"\",";

	@Autowired
	ReadLineContext<Hospital> hospitalReadLineContext;

	@Autowired
	HospitalDto hospitalDto;

	@Autowired
	HospitalService hospitalService;

	@Test
	void deleteAndCount() {
		hospitalDto.deleteAll();
		int cnt = hospitalDto.getCount();

		System.out.println(cnt);
	}

//	@Test
	void addAndCount() {
		HospitalParser hp = new HospitalParser();

		hospitalDto.deleteAll();
		int cnt = hospitalDto.getCount();
		System.out.println(cnt);

		Hospital hospital = hp.parse(line1);

//		hospitalDto.add(hospital);

		Hospital selected = hospitalDto.findById(hospital.getId());

		assertEquals(selected.getId(), hospital.getId());
		assertEquals(selected.getOpenServiceName(), hospital.getOpenServiceName());
		assertEquals(selected.getHospitalName(), hospital.getHospitalName());
		assertEquals(selected.getLicenseDate(), hospital.getLicenseDate());
		assertEquals(selected.getTotalAreaSize(), hospital.getTotalAreaSize());
	}

	@Test
	@DisplayName("전국 병/의원 데이터 DB Insert")
	void addDb() throws IOException {

		//시작시간
		long startTime = System.currentTimeMillis();

		hospitalDto.deleteAll();
		assertEquals(0, hospitalDto.getCount());

		//대용량 데이터 파일
		String fileName = "D:\\workspace\\전국_병의원_정보.csv";

		//DB에 Insert 된 정보 row 수
		int cnt = this.hospitalService.insertLargeVolumeHospitalData(fileName);
		log.debug("Count: {}", cnt);

		int resultCount = hospitalDto.getCount();

		//파싱 후 list에 담기
		List<Hospital> hospitals = hospitalReadLineContext.readByLine(fileName);

		//종료시간
		long endTime = System.currentTimeMillis();

		//소요된 시간
		long totalTime = (endTime - startTime) / 1000;
		System.out.println("Total Time: " + totalTime + "초");
		System.out.println("Count: " + resultCount + "개");

		assertTrue(hospitals.size() > 1000);
		assertTrue(hospitals.size() > 10000);
	}

//	@Test
	@DisplayName(".csv 1줄 hospital로 잘 만드는지 테스트")
	void convertToHospital() {
		HospitalParser hp = new HospitalParser();
		Hospital hospital = hp.parse(line1);

		assertEquals(1, hospital.getId());
		assertEquals("의원", hospital.getOpenServiceName());
		assertEquals(3620000, hospital.getOpenLocalGovernmentCode());
		assertEquals("PHMA119993620020041100004", hospital.getManagementNumber());
		assertEquals(LocalDateTime.of(1999, 6, 12, 0, 0, 0), hospital.getLicenseDate()); //19990612
		assertEquals(1, hospital.getBusinessStatus());
		assertEquals(13, hospital.getBusinessStatusCode());
		assertEquals("062-515-2875", hospital.getPhone());
		assertEquals("광주광역시 북구 풍향동 565번지 4호 3층", hospital.getFullAddress());
		assertEquals("광주광역시 북구 동문대로 24, 3층 (풍향동)", hospital.getRoadNameAddress());
		assertEquals("효치과의원", hospital.getHospitalName());
		assertEquals("치과의원", hospital.getBusinessTypeName());
		assertEquals(1, hospital.getHealthcareProviderCount());
		assertEquals(0, hospital.getPatientRoomCount());
		assertEquals(0, hospital.getTotalNumberOfBeds());
		assertEquals(52.29f, hospital.getTotalAreaSize());

	}

}