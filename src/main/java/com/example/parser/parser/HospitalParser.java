package com.example.parser.parser;

import com.example.parser.domain.entity.Hospital;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class HospitalParser implements Parser<Hospital>{

	@Override
	public Hospital parse(String line) {
		String[] row = line.split("\",\""); //"," 기준으로 나눠서 한줄씩 담는다.

		Hospital hospital = new Hospital();

		hospital.setId(Integer.parseInt(row[0].replace("\"",""))); //"1"," < 첫번째 row는 앞에 "가 하나 더 붙어있음
		hospital.setOpenServiceName(row[1]);
		hospital.setOpenLocalGovernmentCode(Integer.parseInt(row[3]));
		hospital.setManagementNumber(row[4]);

		//LicenseDate 는 .으로 구분되어있기 때문에 나눠주는 작업 추가
		int year = Integer.parseInt(row[5].substring(0, 4));
		int month = Integer.parseInt(row[5].substring(4, 6));
		int day = Integer.parseInt(row[5].substring(6, 8));

		hospital.setLicenseDate(LocalDateTime.of(year, month, day, 0, 0, 0));
		hospital.setBusinessStatus(Integer.parseInt(row[7]));
		hospital.setBusinessStatusCode(Integer.parseInt(row[9]));
		hospital.setPhone(row[15]);
		hospital.setFullAddress(row[18]);
		hospital.setRoadNameAddress(row[19]);
		hospital.setHospitalName(row[21]);
		hospital.setBusinessTypeName(row[25]);
		hospital.setHealthcareProviderCount(Integer.parseInt(row[29]));
		hospital.setPatientRoomCount(Integer.parseInt(row[30]));
		hospital.setTotalNumberOfBeds(Integer.parseInt(row[31]));
		hospital.setTotalAreaSize(Float.parseFloat(row[32]));

		return hospital;

	}

}

