package db.employee;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SalaryUI {
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private SalaryDAO dao = new SalaryDAO();
	private EmployeeUI emp = null;
	
	public SalaryUI(EmployeeUI emp) {
		this.emp = emp;
	}
	
	public void salaryManage() {
		int ch = 0;
		
		while(true) {
			System.out.println("\n[급여관리]");
			
			try {
				System.out.print("1.등록 2.수정 3.삭제 4.월별급여리스트 5.사번검색 6.전체급여리스트 7.사원리스트 8.메인 => ");
				ch = Integer.parseInt(br.readLine());
				
				if(ch == 8) return;
				
				switch(ch) {
				case 1: payment(); break;
				case 2: update(); break;
				case 3: delete(); break;
				case 4: monthList(); break;
				case 5: findBySabeon(); break;
				case 6: listAll(); break;
				case 7: emp.listAll(); break;
				}
			} catch (Exception e) {
			}
		}
	}
	
	public void payment() {
		System.out.println("\n급여 등록...");
		SalaryDTO dto = new SalaryDTO();
		
		try {
			System.out.print("급여번호 ? ");
			dto.setSalaryNum(Integer.parseInt(br.readLine()));
			
			System.out.print("사번 ? ");
			dto.setSabeon(br.readLine());
			
			System.out.print("급여년월[YYYYMM] ? ");
			dto.setPayDate(br.readLine());
			
			System.out.print("급여지급년월일[YYYYMMDD] ? ");
			dto.setPaymentDate(br.readLine());
			
			System.out.print("기본급 ? ");
			dto.setPay(Integer.parseInt(br.readLine()));
			
			System.out.print("수당 ? ");
			dto.setSudang(Integer.parseInt(br.readLine()));
			
			System.out.print("세금 ? ");
			dto.setTax(Integer.parseInt(br.readLine()));
			
			System.out.print("메모 ? ");
			dto.setMemo(br.readLine());
			
			dao.insertSalary(dto);
			
			System.out.println("급여가 등록되었습니다.\n");
			
		} catch (NumberFormatException e) {
			System.out.println("급여는 숫자만 입력 가능합니다.");
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
	
	public void update() {
		System.out.println("\n급여 수정...");

	}

	public void delete() {
		System.out.println("\n급여 삭제...");
		
	}

	public void findBySabeon() {
		System.out.println("\n사번 검색...");
		
	}

	public void monthList() {
		System.out.println("\n월별 리스트...");
		
	}
	
	public void listAll() {
		System.out.println("\n급여 리스트...");
	
	}

}
