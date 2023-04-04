package com.example.domain.dto;

import com.example.domain.entity.Hospital;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class HospitalDto {
	private final JdbcTemplate jdbcTemplate;

	public HospitalDto(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	//insert
	public void add(List<Hospital> hospitals) {
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

		//batchUpdate(query, 저장할 데이터의 collection, batch 처리할 크기, ParameterizedPreparedStatementSetter)
		jdbcTemplate.batchUpdate(sql, hospitals, 100, (ps, hospital) -> {
			ps.setInt(1, hospital.getId());
			ps.setString(2, hospital.getOpenServiceName());
			ps.setInt(3, hospital.getOpenLocalGovernmentCode());
			ps.setString(4, hospital.getManagementNumber());
			ps.setString(5, hospital.getLicenseDate().toString());
			ps.setInt(6, hospital.getBusinessStatus());
			ps.setInt(7, hospital.getBusinessStatusCode());
			ps.setString(8, hospital.getPhone());
			ps.setString(9, hospital.getFullAddress());
			ps.setString(10, hospital.getRoadNameAddress());
			ps.setString(11, hospital.getHospitalName());
			ps.setString(12, hospital.getBusinessTypeName());
			ps.setInt(13, hospital.getHealthcareProviderCount());
			ps.setInt(14, hospital.getPatientRoomCount());
			ps.setInt(15, hospital.getTotalNumberOfBeds());
			ps.setFloat(16, hospital.getTotalAreaSize());
		});
	}

	RowMapper<Hospital> rowMapper = (rs, rowNum) -> {
		Hospital hospital = new Hospital();

		hospital.setId(rs.getInt("id"));
		hospital.setOpenServiceName(rs.getString("open_service_name"));
		hospital.setOpenLocalGovernmentCode(rs.getInt("open_local_government_code"));
		hospital.setManagementNumber(rs.getString("management_number"));
		hospital.setLicenseDate(rs.getTimestamp("license_date").toLocalDateTime());
		hospital.setBusinessStatus(rs.getInt("business_status"));
		hospital.setBusinessStatusCode(rs.getInt("business_status_code"));
		hospital.setPhone(rs.getString("phone"));
		hospital.setFullAddress(rs.getString("full_address"));
		hospital.setRoadNameAddress(rs.getString("road_name_address"));
		hospital.setHospitalName(rs.getString("hospital_name"));
		hospital.setBusinessTypeName(rs.getString("business_type_name"));
		hospital.setHealthcareProviderCount(rs.getInt("healthcare_provider_count"));
		hospital.setPatientRoomCount(rs.getInt("patient_room_count"));
		hospital.setTotalNumberOfBeds(rs.getInt("total_number_of_beds"));
		hospital.setTotalAreaSize(rs.getFloat("total_area_size"));

		return hospital;
	};

	//find
	public Hospital findById(int id){
		return this.jdbcTemplate.queryForObject("SELECT * FROM hospital WHERE id = ?", rowMapper, id);
	}

	//count
	public int getCount() {
		String sql = "SELECT COUNT(id) FROM hospital;";
		return this.jdbcTemplate.queryForObject(sql, Integer.class);
	}

	//delete
	public void deleteAll() {
		this.jdbcTemplate.update("DELETE FROM hospital;");
	}

}

