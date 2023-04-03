# 📌 전국 병/의원 대용량 데이터 가공

## ✅ 프로젝트 빌드
```
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jdbc'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    compileOnly 'org.projectlombok:lombok'
    runtimeOnly 'com.mysql:mysql-connector-j'
    annotationProcessor 'org.projectlombok:lombok'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

## ✅ 한 줄씩 Insert

### 1. 데이터 가공
* 파일에서 필요한 데이터만 뽑아 형식에 맞게 가공하여 Hospital 객체 생성

```java
@Component
public class HospitalParser implements Parser<Hospital>{

	@Override
	public Hospital parse(String line) {
        //"," 기준으로 나눠서 한줄씩 담는다.
		String[] row = line.split("\",\""); 

		Hospital hospital = new Hospital();

        //파일 첫번째 row에 " 가 하나더 붙어있음(주의!)
		hospital.setId(Integer.parseInt(row[0].replace("\"","")));
		hospital.setOpenServiceName(row[1]);
		hospital.setOpenLocalGovernmentCode(Integer.parseInt(row[3]));
		hospital.setManagementNumber(row[4]);

        //date 는 . 으로 연결되어있기 때문에 나눠주는 작업 추가
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
````

### 2. 가공된 데이터 `jdbc template` 사용하여 db insert

```java
@Component
public class HospitalDto {
	private final JdbcTemplate jdbcTemplate;


	public HospitalDto(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	//insert
	public void add(Hospital hospital) {
		String sql ="INSERT INTO `parser_db`.`hospital` \n" +
				"(`id`, `open_service_name`, `open_local_government_code`, `management_number`, `license_date`, " +
				"`business_status`, `business_status_code`, `phone`, `full_address`, `road_name_address`, `hospital_name`, " +
				"`business_type_name`, `healthcare_provider_count`, `patient_room_count`, `total_number_of_beds`, `total_area_size`) \n" +
				"VALUES (?,?,?," +
				"?,?,?," +
				"?,?,?," +
				"?,?,?," +
				"?,?,?," +
				"?);";

		this.jdbcTemplate.update(sql,
				hospital.getId(),
				hospital.getOpenServiceName(),
				hospital.getOpenLocalGovernmentCode(),
				hospital.getManagementNumber(),
				hospital.getLicenseDate(),
				hospital.getBusinessStatus(),
				hospital.getBusinessStatusCode(),
				hospital.getPhone(),
				hospital.getFullAddress(),
				hospital.getRoadNameAddress(),
				hospital.getHospitalName(),
				hospital.getBusinessTypeName(),
				hospital.getHealthcareProviderCount(),
				hospital.getPatientRoomCount(),
				hospital.getTotalNumberOfBeds(),
				hospital.getTotalAreaSize()
		);
	}
}
```

### 3. Test 코드 작성하여 insert 진행
> 한 줄씩 insert
```java
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
    System.out.println(cnt);

    //파싱 후 list에 담기
    List<Hospital> hospitals = hospitalReadLineContext.readByLine(fileName);

    //종료시간
    long endTime = System.currentTimeMillis();

    //소요된 시간
    long totalTime = (endTime - startTime) / 1000;
    System.out.println("Total Time: " + totalTime + "초");

    assertTrue(hospitals.size() > 1000);
    assertTrue(hospitals.size() > 10000);
}
```
* 총 111,919건 insert

![](img/카운트.png)

* 총 걸린 시간 약 8분

![](img/총시간.png)    

## ✅ JDBC Batch 사용하여 일괄 Insert