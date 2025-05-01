package db.member2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import db.util.DBConn;

public class MemberUI {
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private MemberDAO dao = new MemberDAOImpl();
	
	public void menu() {
		int ch;
		
        while(true) {
        	try {
				System.out.print("1.등록 2.수정 3.삭제 4.아이디검색 5.이름검색 6.리스트 7.종료 => ");
				ch = Integer.parseInt(br.readLine());
				
				if(ch == 7) {
					DBConn.close();
					return;
				}
				
				switch(ch) {
				case 1: insert(); break;
				case 2: update(); break;
				case 3: delete(); break;
				case 4: findById(); break;
				case 5: findByName(); break;
				case 6: listAll(); break;
				}
				
			} catch (Exception e) {
			}
        }
		
	}
	
	protected void insert() {
		System.out.println("\n[데이터 등록]");
		
		MemberDTO dto = new MemberDTO();
		
		try {
			System.out.print("아이디 ? ");
			dto.setId(br.readLine());
			
			System.out.print("비밀번호 ? ");
			dto.setPwd(br.readLine());
			
			System.out.print("이름 ? ");
			dto.setName(br.readLine());
			
			System.out.print("생년월일[YYYY-MM-DD] ? ");
			dto.setBirth(br.readLine());
			
			System.out.print("이메일 ? ");
			dto.setEmail(br.readLine());
			
			System.out.print("전화번호 ? ");
			dto.setTel(br.readLine());
			
			dao.insertMember(dto);
			
			System.out.println("회원 등록 성공...");
			
		} catch (SQLIntegrityConstraintViolationException e) {
			if(e.getErrorCode() == 1) { // ORA-00001
				System.out.println("에러-등록된 아이디입니다.");
			}else if(e.getErrorCode() == 1400) { // NOT NULL 위반
				System.out.println("에러-필수 입력사항을 입력하지 않았습니다.");
			}else {
				System.out.println(e.toString());
			}
		} catch (SQLDataException e) {
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
	
	protected void update() {
		System.out.println("\n[데이터 수정]");
		
		MemberDTO dto = new MemberDTO();
		String id;
		
		try {
			System.out.print("수정할 학번 ? ");
			id = br.readLine();
			
			dto = dao.readMember(id);
			if(dto == null) {
				System.out.println("등록된 아이디가 아닙니다.\n");
				return;
			}
			title();
			print(dto);
			
			dto.setId(id);
			
			System.out.print("패스워드 ? ");
			dto.setPwd(br.readLine());
			
			System.out.print("이름 ? ");
			dto.setName(br.readLine());
			
			System.out.print("생년월일[YYYY-MM-DD] ? ");
			dto.setBirth(br.readLine());
			
			System.out.print("이메일 ? ");
			dto.setEmail(br.readLine());
			
			System.out.print("전화번호 ? ");
			dto.setTel(br.readLine());
			
			dao.updateMember(dto);
			
			System.out.println("수정이 완료 되었습니다.\n");
			
		} catch (SQLDataException e) {
			if(e.getErrorCode() == 1840 || e.getErrorCode() == 1861) {
				System.out.println("에러-날짜 형식 오류입니다.");
			} else {
				System.out.println(e.toString());
			}
		} catch (SQLException e) {
			if(e.getErrorCode() == 1407) { // UPDATE_NOT NULL 위반
				System.out.println("에러-필수 입력사항을 입력하지 않았습니다.");
			} else {
				System.out.println(e.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	
	protected void delete() {
		System.out.println("\n[데이터 삭제]");
		
		String id;
		
		try {
			System.out.print("삭제할 아이디 ? ");
			id = br.readLine();
			
			int result = dao.deleteMember(id);
			if(result > 0) {
				System.out.println("데이터를 삭제 했습니다.\n");
			} else {
				System.out.println("등록된 아이디가 아닙니다.\n");
			}
			
		} catch (SQLException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println();
	}
	
	protected void listAll() {
		System.out.println("\n[전체 리스트 출력]");
		
		List<MemberDTO> list = dao.listMember();
		
		title();
		for(MemberDTO dto : list) {
			print(dto);
		}
		System.out.println();
	}
	
	protected void findById() {
		System.out.println("\n[아이디 검색]");
		String id;
		
		try {
			System.out.print("검색할 아이디 ? ");
			id = br.readLine();
			
			MemberDTO dto = dao.readMember(id);
			if(dto == null) {
				System.out.println("등록된 아이디가 아닙니다.\n");
				return;
			}
			
			title();
			print(dto);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println();
	}
	
	protected void findByName() {
		System.out.println("\n[이름 검색]");
		
		String name;
		
		try {
			System.out.print("검색할 이름 ? ");
			name = br.readLine();
			
			List<MemberDTO> list = dao.listMember(name);
			
			title();
			for(MemberDTO dto : list) {
				print(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println();
	}
	
	private void title() {
		System.out.print("아이디\t");
		System.out.print("이름\t");
		System.out.print("패스워드\t");
		System.out.print("생년월일\t\t");
		System.out.print("이메일\t\t");
		System.out.println("전화번호\t");
		System.out.println("----------------------------------------------------------------------");
	}
	
	private void print(MemberDTO dto){
		System.out.print(dto.getId() + "\t");
		System.out.print(dto.getName() + "\t");
		System.out.print(dto.getPwd() + "\t");
		System.out.print(dto.getBirth() + "\t");
		System.out.print(dto.getEmail() + "\t");
		System.out.println(dto.getTel());
	}
}
