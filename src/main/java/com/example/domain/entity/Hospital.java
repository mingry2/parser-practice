package com.example.domain.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Hospital {
	private int id; //번호
	private String openServiceName; //개방서비스명
	private int openLocalGovernmentCode; //개방자치단체코드
	private String managementNumber; //관리번호
	private LocalDateTime licenseDate; //인허가일자
	private int businessStatus; //영업상태명
	private int businessStatusCode; //영업상태구분코드
	private String phone; //소재지전화
	private String fullAddress; //소재지전체주소
	private String roadNameAddress; //도로명전체주소
	private String hospitalName; //사업장명
	private String businessTypeName; //업태구분명
	private int healthcareProviderCount; //의료인수
	private int patientRoomCount; //입원실수
	private int totalNumberOfBeds; //병상수
	private float totalAreaSize; //총면적
}

