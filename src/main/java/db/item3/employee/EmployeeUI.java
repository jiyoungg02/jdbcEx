package db.item3.employee;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class EmployeeUI {
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private EmployeeDAO dao = new EmployeeDAO();
	
	public void employeeManage() {
		int ch = 0;
		
		while(true) {
			System.out.println("\n[사원관리]");
			
			try {
				System.out.print("1.사원등록 2.정보수정 3.사번검색 4.이름검색 5.사원리스트 6.메인 => ");
				ch = Integer.parseInt(br.readLine());
				
				if(ch == 6) return;
				
				switch(ch) {
				case 1: insert(); break;
				case 2: update(); break;
				case 3: findBySabeon(); break;
				case 4: findByName(); break;
				case 5: listAll(); break;
				}
			} catch (Exception e) {
			}
		}
	}
	
	protected void insert() {
		System.out.println("\n사원 등록...");
		
		EmployeeDTO dto = new EmployeeDTO();
		
		try {
			System.out.print("사원번호 ? ");
			dto.setSabeon(br.readLine());
			
			System.out.print("이름 ? ");
			dto.setName(br.readLine());
			
			System.out.print("생년월일[yyyymmdd] ? ");
			dto.setBirth(br.readLine());
			
			System.out.print("전화번호 ? ");
			dto.setTel(br.readLine());
			
			dao.insertEmployee(dto);

			System.out.println("사원 등록 완료 !!!");
		} catch (SQLIntegrityConstraintViolationException e) {
			if(e.getErrorCode() == 1) {
				System.out.println("사원번호 중복 입니다.");
			} else if(e.getErrorCode() == 1400) { // INSERT-NOT NULL 제약 위반
				System.out.println("필수 사항을 입력하지 않았습니다.");
			} else {
				System.out.println(e.toString());
			}
		} catch (SQLDataException e) {
			if(e.getErrorCode() == 1840 || e.getErrorCode() == 1861) {
				System.out.println("날짜입력 형식 오류입니다.");
			} else {
				System.out.println(e.toString());
			}
		} catch (SQLException e) {
			 e.printStackTrace();
		} catch (Exception e) {
			 e.printStackTrace();
		}
		System.out.println();
		
	}
	
	protected void update() {
		System.out.println("\n사원 정보 수정...");
		
		String sabeon;
		
		try {
			System.out.print("수정할 사번 ? ");
			sabeon = br.readLine();
			
			EmployeeDTO dto = dao.readEmployee(sabeon);
			if(dto == null) {
				System.out.println("등록된 사원이 아닙니다. !!!");
				return;
			}
			
			System.out.println("등록정보 -> ");
			System.out.print(dto.getName()+"\t");
			System.out.print(dto.getBirth()+"\t");
			System.out.println(dto.getTel());
			System.out.println("-------------------------------\n");
			
			System.out.print("수정할 이름 ? ");
			dto.setName(br.readLine());
			
			System.out.print("생년월일[yyyymmdd] ? ");
			dto.setBirth(br.readLine());
			
			System.out.print("전화번호 ? ");
			dto.setTel(br.readLine());
			
			int result = dao.updateEmployee(dto);
			
			if(result == 0) {
				System.out.println("등록된 자료가 아닙니다.");
			} else { 
				System.out.println("정보 수정 완료 !!!");
			}
		} catch (SQLDataException e) {
			if(e.getErrorCode() == 1840 || e.getErrorCode() == 1861) {
				System.out.println("날짜입력 형식 오류입니다.");
			} else {
				System.out.println(e.toString());
			}
		} catch (SQLException e) {
			// ORA-12899 : 문자열 입력 값보다 열폭이 큰경우
			if(e.getErrorCode() == 1407) { // UPDATE-NOT NULL 제약 위반
				System.out.println("필수 사항을 입력하지 않았습니다.");
			} else  {
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
		
	}
	
	protected void findBySabeon() {
		System.out.println("\n사번 검색...");
		
		String sabeon;
		
		try {
			System.out.print("검색할 사번 ? ");
			sabeon = br.readLine();
			
			EmployeeDTO dto = dao.readEmployee(sabeon);
			if(dto == null) {
				System.out.println("등록된 정보가 없습니다.\n");
				return;
			}
			
			System.out.print(dto.getSabeon()+"\t");
			System.out.print(dto.getName()+"\t");
			System.out.print(dto.getBirth()+"\t");
			System.out.println(dto.getTel());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
		
	}

	protected void findByName() {
		System.out.println("\n이름 검색...");
		
		String name;
		
		try {
			System.out.print("검색할 이름 ? ");
			name = br.readLine();
			
			List<EmployeeDTO> list = dao.listEmployee(name);
			if(list.size() == 0) {
				System.out.println("등록된 정보가 없습니다.\n");
				return;
			}
			
			for(EmployeeDTO dto : list) {
				System.out.print(dto.getSabeon()+"\t");
				System.out.print(dto.getName()+"\t");
				System.out.print(dto.getBirth()+"\t");
				System.out.println(dto.getTel());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
		
	}
	
	protected void listAll() {
		System.out.println("\n사원 리스트...");
		List<EmployeeDTO> list = dao.listEmployee();
		
		for(EmployeeDTO dto : list) {
			System.out.print(dto.getSabeon()+"\t");
			System.out.print(dto.getName()+"\t");
			System.out.print(dto.getBirth()+"\t");
			System.out.println(dto.getTel());
		}
		System.out.println();
		
	}
}
