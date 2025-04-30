package db.item3.score;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import db.util.DBConn;

public class ScoreUI {
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	private ScoreDAO dao = new ScoreDAOImpl();
	
	public void menu() {
		int ch;
		
		System.out.println("성적처리-PreparedStatement 예제\n");
		
		while(true) {
			try {
				System.out.print("1.등록 2.수정 3.삭제 4.학번검색 5.이름검색 6.전체리스트 7.종료 => ");
				ch = Integer.parseInt(br.readLine());
				
				if(ch == 7) {
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
	}
	
	public void delete() {
		System.out.println("\n[데이터 삭제]");
	}
	
	public void findByHak() {
		System.out.println("\n[학번 검색]");
	}
	
	public void findByName() {
		System.out.println("\n[이름 검색]");
	}
	
	public void findByAll() {
		System.out.println("\n[전체 리스트]");
		
		List<ScoreDTO> list = dao.listScore();
		
		title();
		for(ScoreDTO dto : list) {
			print(dto);
		}
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
	
	
}
