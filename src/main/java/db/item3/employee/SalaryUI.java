package db.item3.employee;

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
	
	protected void payment() {
		System.out.println("\n급여 등록...");
		
		try {
			SalaryDTO dto = new SalaryDTO();
			
			System.out.print("사원번호 ? ");
			dto.setSabeon(br.readLine());
			
			System.out.print("급여년월[yyyymm] ? ");
			dto.setPayDate(br.readLine());
			
			System.out.print("급여지급일자[yyyymmdd] ? ");
			dto.setPaymentDate(br.readLine());
			
			System.out.print("기본급 ? ");
			dto.setPay(Integer.parseInt(br.readLine()));
			
			System.out.print("수당 ? ");
			dto.setSudang(Integer.parseInt(br.readLine()));

			System.out.print("메모 ? ");
			dto.setMemo(br.readLine());

			int tot = dto.getPay() + dto.getSudang();
			
			int tax = 0;
			if(tot >= 3000000) {
				tax = (int)(tot * 0.03);
			} else if(tot >= 2000000) {
				tax = (int)(tot * 0.02);
			} else {
				tax = 0;
			}
			dto.setTax(tax);
			
			dao.insertSalary(dto);
			
			System.out.println("등록 완료 !!!");
			
		} catch (NumberFormatException e) {
			System.out.println("입력 형식이 일치하지 않습니다.");
		} catch (SQLIntegrityConstraintViolationException e) {
			if(e.getErrorCode() == 2291) { // FK 제약 조건 위반
				System.out.println("등록된 사원이 아닙니다.");
			} else if(e.getErrorCode() == 1400) { // NOT NULL 제약 위반
				System.out.println("필수 사항을 입력하지 않았습니다.");
			} else {
				System.out.println(e.toString());
			}
		} catch (SQLDataException e) {
			// ORA-01438:폭보다 숫자 길이가 큰경우 
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
		
	}
	
	protected void update() {
		System.out.println("\n급여 수정...");
		
		try {
			int salaryNum;
			System.out.print("수정할 급여 번호 ? ");
			salaryNum = Integer.parseInt(br.readLine());
			
			SalaryDTO dto = dao.readSalary(salaryNum);
			if(dto == null) {
				System.out.println("등록된 정보가 아닙니다.\n");
				return;
			}
			
			System.out.println("등록정보 -> ");
			System.out.print(dto.getSabeon()+"\t");
			System.out.print(dto.getName()+"\t");
			System.out.print(dto.getPayDate()+"\t");
			System.out.print(dto.getPaymentDate()+"\t");
			System.out.print(dto.getPay()+"\t");
			System.out.print(dto.getSudang()+"\t");
			System.out.print(dto.getTot()+"\t");
			System.out.print(dto.getTax()+"\t");
			System.out.println(dto.getAfterPay());
			System.out.println("---------------------------------------------------------\n");
			
			System.out.print("수정할 사원번호 ? ");
			dto.setSabeon(br.readLine());
			
			System.out.print("급여년월[yyyymm] ? ");
			dto.setPayDate(br.readLine());
			
			System.out.print("급여지급일자[yyyymmdd] ? ");
			dto.setPaymentDate(br.readLine());
			
			System.out.print("기본급 ? ");
			dto.setPay(Integer.parseInt(br.readLine()));
			
			System.out.print("수당 ? ");
			dto.setSudang(Integer.parseInt(br.readLine()));
			
			System.out.print("메모 ? ");
			dto.setMemo(br.readLine());
			
			int tot = dto.getPay()+dto.getSudang();
			
			int tax = 0;
			if(tot >= 3000000) {
				tax = (int)(tot * 0.03);
			} else if(tot >= 2000000) {
				tax = (int)(tot * 0.02);
			} else {
				tax = 0;
			}
			dto.setTax(tax);
			
			int result = dao.updateSalary(dto);
			
			if(result == 0) {
				System.out.println("등록된 자료가 아닙니다.");
			} else {
				System.out.println("급여 수정 완료 !!!");
			}
		} catch (NumberFormatException e) {
			System.out.println("입력 형식이 일치하지 않습니다.");
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

	protected void delete() {
		System.out.println("\n급여 삭제...");
		
		int salaryNum;
		
		try {
			System.out.print("삭제할 급여 번호 ?");
			salaryNum = Integer.parseInt(br.readLine());
			
			int result = dao.deleteSalary(salaryNum);
			
			if(result == 0) {
				System.out.println("등록된 자료가 아닙니다.");
			} else {
				System.out.println("급여 삭제 완료 !!!");
			}
			
		} catch (NumberFormatException e) {
			System.out.println("입력 형식이 일치하지 않습니다. !!!");
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("급여 삭제 실패 !!!");
		}
		System.out.println();
		
	}

	protected void findBySabeon() {
		System.out.println("\n사번 검색...");
		
		String payDate, sabeon;
		
		try {
			System.out.print("검색할 급여년월[yyyymm] ? ");
			payDate = br.readLine();
			System.out.print("검색할 사번 ? ");
			sabeon = br.readLine();
			
			Map<String, Object> map=new HashMap<>();
			map.put("payDate", payDate);
			map.put("sabeon", sabeon);
			
			List<SalaryDTO> list = dao.listSalary(map);
			
			for(SalaryDTO dto : list) {
				System.out.print(dto.getSalaryNum()+"\t");
				System.out.print(dto.getSabeon()+"\t");
				System.out.print(dto.getName()+"\t");
				System.out.print(dto.getPayDate()+"\t");
				System.out.print(dto.getPaymentDate()+"\t");
				System.out.print(dto.getPay()+"\t");
				System.out.print(dto.getSudang()+"\t");
				System.out.print(dto.getTot()+"\t");
				System.out.print(dto.getTax()+"\t");
				System.out.println(dto.getAfterPay());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
		
	}

	protected void monthList() {
		System.out.println("\n월별 리스트...");
		
		String payDate;
		
		try {
			System.out.print("검색할 급여년월[yyyymm] ? ");
			payDate = br.readLine();
			
			List<SalaryDTO> list = dao.listSalary(payDate);
			
			for(SalaryDTO dto : list) {
				System.out.print(dto.getSalaryNum()+"\t");
				System.out.print(dto.getSabeon()+"\t");
				System.out.print(dto.getName()+"\t");
				System.out.print(dto.getPayDate()+"\t");
				System.out.print(dto.getPaymentDate()+"\t");
				System.out.print(dto.getPay()+"\t");
				System.out.print(dto.getSudang()+"\t");
				System.out.print(dto.getTot()+"\t");
				System.out.print(dto.getTax()+"\t");
				System.out.println(dto.getAfterPay());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
		
	}
	
	protected void listAll() {
		System.out.println("\n급여 리스트...");
	
		List<SalaryDTO> list = dao.listSalary();
		
		for(SalaryDTO dto : list) {
			System.out.print(dto.getSalaryNum()+"\t");
			System.out.print(dto.getSabeon()+"\t");
			System.out.print(dto.getName()+"\t");
			System.out.print(dto.getPayDate()+"\t");
			System.out.print(dto.getPaymentDate()+"\t");
			System.out.print(dto.getPay()+"\t");
			System.out.print(dto.getSudang()+"\t");
			System.out.print(dto.getTot()+"\t");
			System.out.print(dto.getTax()+"\t");
			System.out.println(dto.getAfterPay());
		}
		System.out.println();
	}
	
}
