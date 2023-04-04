package com.example.parser;

import com.example.domain.entity.Hospital;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class ReadLineContext<T> {

	private Parser<Hospital> parser;

	public ReadLineContext(Parser<Hospital> parser) {
		this.parser = parser;
	}

	@Transactional
	public List<Hospital> readByLine(String fileName) throws IOException {

		List<Hospital> hospitals = new ArrayList<>();

		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line;

		while ((line = br.readLine()) != null) {
			try {
				Hospital hospital = parser.parse(line);
				hospitals.add(hospital);
			} catch (Exception e) {
				log.warn("파싱 중 문제가 생겨 이 라인은 넘어갑니다. 파일내용 : {}, Error message: {}", line, e.getMessage());
			}
		}

		br.close();
		return hospitals;
	}


}

