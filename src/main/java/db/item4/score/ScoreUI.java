package db.item4.score;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Map;

import db.util.DBConn;

public class ScoreUI {
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	private ScoreDAO dao = new ScoreDAOImpl();
	
	public void menu() {
		int ch;
		
		System.out.println("성적처리-CallableStatement 예제\n");
		
		while(true) {
			try {
				System.out.print("1.등록 2.수정 3.삭제 4.학번검색 5.이름검색 6.전체리스트 7.과목별평균 8.종료 => ");
				ch = Integer.parseInt(br.readLine());
				
				if(ch == 8) {
					DBConn.close();
					return;
				}
				
				switch(ch) {
				case 1: insert(); break;
				case 2: update(); break;
				case 3: delete(); break;
				case 4: findByHak(); break;
				case 5: findByName(); break;
				case 6: findByAll(); break;
				case 7: average(); break;
				}
			} catch (Exception e) {
			}
		}
		
	}
	
	public void insert() {
		System.out.println("\n[데이터 등록]");
		
		ScoreDTO dto = new ScoreDTO();
		
		try {
			System.out.print("학번 ? ");
			dto.setHak(br.readLine());
			
			System.out.print("이름 ? ");
			dto.setName(br.readLine());
			
			System.out.print("생년월일[YYYY-MM-DD] ? ");
			dto.setBirth(br.readLine());
			
			System.out.print("국어 ? ");
			dto.setKor(Integer.parseInt(br.readLine()));
			
			System.out.print("영어 ? ");
			dto.setEng(Integer.parseInt(br.readLine()));
			
			System.out.print("수학 ? ");
			dto.setMat(Integer.parseInt(br.readLine()));
			
			dao.insertScore(dto);
			
			System.out.println("데이터 등록이 완료 되었습니다. !!!");
			
		} catch (NumberFormatException e) {
			System.out.println("점수는 숫자만 입력 가능합니다.");
		} catch (SQLIntegrityConstraintViolationException e) {
			// SQLIntegrityConstraintViolationException
			// : 기본키중복, NOT NULL등의 제약조건 위반 - 무결성제약 위반
			if(e.getErrorCode() == 1) { // ORA-00001
				System.out.println("에러-등록된 학번입니다.");
			}else if(e.getErrorCode() == 1400) { // NOT NULL 위반
				System.out.println("에러-필수 입력사항을 입력하지 않았습니다.");
			}else {
				System.out.println(e.toString());
			}
			
		} catch (SQLDataException e) {
			// 날짜등의 형식 잘못으로 인한 에러
			if(e.getErrorCode() == 1840 || e.getErrorCode() == 1861) {
				System.out.println("에러-날짜 형식 오류입니다.");
			}else {
				System.out.println(e.toString());
			}
		} catch (SQLException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println();
	}
	
	public void update(){
		System.out.println("\n[데이터 수정]");
		
		ScoreDTO dto = new ScoreDTO();
		
		try {
			System.out.print("수정할 학번 ? ");
			dto.setHak(br.readLine());
			
			System.out.print("이름 ? ");
			dto.setName(br.readLine());
			
			System.out.print("생년월일[YYYY-MM-DD] ? ");
			dto.setBirth(br.readLine());
			
			System.out.print("국어 ? ");
			dto.setKor(Integer.parseInt(br.readLine()));
			
			System.out.print("영어 ? ");
			dto.setEng(Integer.parseInt(br.readLine()));
			
			System.out.print("수학 ? ");
			dto.setMat(Integer.parseInt(br.readLine()));
			
			dao.updateScore(dto);
			
			System.out.println("데이터 수정이 완료 되었습니다. !!!");
			
			
		} catch (NumberFormatException e) {
			System.out.println("점수는 숫자만 입력 가능합니다.");
		} catch (SQLDataException e) {
			if(e.getErrorCode() == 1840 || e.getErrorCode() == 1861) {
				System.out.println("에러-날짜 형식 오류입니다.");
			} else {
				System.out.println(e.toString());
			}
		} catch (SQLException e) {
			if(e.getErrorCode() == 1407) { // UPDATE_NOT NULL 위반
				System.out.println("에러-필수 입력사항을 입력하지 않았습니다.");
			} else if(e.getErrorCode() == 20100) {
				System.out.println("등록된 데이터가 아닙니다 !!!");
			} else {
				System.out.println(e.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println();
	}
	
	public void delete() {
		System.out.println("\n[데이터 삭제]");

		String hak;
		
		try {
			System.out.print("삭제할 학번 ? ");
			hak = br.readLine();
			
			dao.deleteScore(hak);

			System.out.println("데이터를 삭제 했습니다. !!!");
			
			
		} catch (SQLException e) {
			if(e.getErrorCode() == 20100) {
				System.out.println("등록된 데이터가 아닙니다.");
			} else {
				System.out.println(e.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println();
	}
	
	public void findByHak() {
		System.out.println("\n[학번 검색]");
		String hak;
		
		try {
			System.out.print("검색할 학번 ? ");
			hak = br.readLine();
			
			ScoreDTO dto = dao.findById(hak);
			if(dto == null) {
				System.out.println("등록된 학번이 아닙니다.\n");
				return;
			}
			
			title();
			print(dto);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	
	public void findByName() {
		System.out.println("\n[이름 검색]");
		
		String name;
		
		try {
			System.out.print("검색할 이름 ? ");
			name = br.readLine();
			
			List<ScoreDTO> list = dao.listScore(name);
			
			title();
			for(ScoreDTO dto : list) {
				print(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println();
	}
	
	public void findByAll() {
		System.out.println("\n[전체 리스트]");
		
		List<ScoreDTO> list = dao.listScore();
		
		titleAll();
		for(ScoreDTO dto : list) {
			printAll(dto);
		}
		System.out.println();
	}
	
	public void average() {
		System.out.println("\n[과목별 평균점수]");
		
		Map<String, Integer> map = dao.averageScore();
		
		int kor = map.get("kor");
		int eng = map.get("eng");
		int mat = map.get("mat");
		
		System.out.println("국어 : " + kor);
		System.out.println("영어 : " + eng);
		System.out.println("수학 : " + mat);
		System.out.println();
	}

	private void title() {
		System.out.print("학번\t");
		System.out.print("이름\t");
		System.out.print("생년월일\t\t");
		System.out.print("국어\t");
		System.out.print("영어\t");
		System.out.print("수학\t");
		System.out.print("총점\t");
		System.out.println("평균");
		System.out.println("----------------------------------------------------------------------");
	}
	
	private void print(ScoreDTO dto) {
		System.out.print(dto.getHak() + "\t");
		System.out.print(dto.getName() + "\t");
		System.out.print(dto.getBirth() + "\t");
		System.out.print(dto.getKor() + "\t");
		System.out.print(dto.getEng() + "\t");
		System.out.print(dto.getMat() + "\t");
		System.out.print(dto.getTot() + "\t");
		System.out.println(dto.getAve());
	}
	
	
	private void titleAll() {
		System.out.print("학번\t");
		System.out.print("이름\t");
		System.out.print("생년월일\t\t");
		System.out.print("국어\t");
		System.out.print("영어\t");
		System.out.print("수학\t");
		System.out.print("총점\t");
		System.out.print("평균\t");
		System.out.println("순위");
		System.out.println("--------------------------------------------------------------------------------");
	}
	
	private void printAll(ScoreDTO dto) {
		System.out.print(dto.getHak() + "\t");
		System.out.print(dto.getName() + "\t");
		System.out.print(dto.getBirth() + "\t");
		System.out.print(dto.getKor() + "\t");
		System.out.print(dto.getEng() + "\t");
		System.out.print(dto.getMat() + "\t");
		System.out.print(dto.getTot() + "\t");
		System.out.print(dto.getAve() + "\t");
		System.out.println(dto.getRank());
	}
	
	
}
