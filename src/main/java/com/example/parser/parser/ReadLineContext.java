package com.example.parser.parser;

import com.example.parser.domain.entity.Hospital;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadLineContext<T> {

	private Parser<Hospital> parser;

	public ReadLineContext(Parser<Hospital> parser) {
		this.parser = parser;
	}

	public List<Hospital> readByLine(String fileName) throws IOException {

		List<Hospital> hospitals = new ArrayList<>();

		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line;

		while ((line = br.readLine()) != null) {

			try {

				Hospital hospital = parser.parse(line);
				hospitals.add(hospital);

			} catch (Exception e) {

				System.out.printf("파싱 중 문제가 생겨 이 라인은 넘어갑니다. 파일내용 : %s\n", line);

			}
		}

		br.close();
		return hospitals;
	}


}

