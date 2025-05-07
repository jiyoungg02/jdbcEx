package db.item4.member;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import db.util.DBConn;

public class MemberUI {
	private MemberDAO dao = new MemberDAOImpl();
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	public void menu() {
		int ch;
		
		System.out.println("회원 - CallableStatement 예제...");
		
		while(true) {
			try {
				System.out.print("1.회원가입 2.수정 3.탈퇴 4.아이디검색 5.이름검색 6.리스트 7.종료 => ");
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
		System.out.println("\n[회원 가입]");

		try {
			MemberDTO dto = new MemberDTO();

			System.out.print("아이디 ? ");
			dto.setId(br.readLine());

			System.out.print("패스워드 ? ");
			dto.setPwd(br.readLine());

			System.out.print("이름 ? ");
			dto.setName(br.readLine());

			System.out.print("생년월일 ? ");
			dto.setBirth(br.readLine());

			System.out.print("이메일 ? ");
			dto.setEmail(br.readLine());

			System.out.print("전화번호 ? ");
			dto.setTel(br.readLine());

			dao.insertMember(dto);
			System.out.println("회원 등록 성공 !!!");

		} catch (SQLIntegrityConstraintViolationException e) {
			// 기본키 중복, NOT NULL등의 제약조건 위반에 의한 예외 발생-무결성 제약 조건 위반
			if(e.getErrorCode()==1) {
				System.out.println("아이디 중복입니다.");
			} else if(e.getErrorCode() == 1400){ // INSERT-NOT NULL 위반
				System.out.println("필수 입력사항을 입력하지 않았습니다.");
			} else {
				System.out.println(e.toString());
			}
		} catch (SQLDataException e) {
			// 날짜등의 형식 잘못으로 인한 예외
			if(e.getErrorCode() == 1840 || e.getErrorCode() == 1861) {
				System.out.println("날짜 입력 형식 오류입니다.");
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
		System.out.println("\n[회원 정보 수정]");

		try {
			MemberDTO dto = new MemberDTO();

			System.out.print("수정할 아이디 ? ");
			dto.setId(br.readLine());

			System.out.print("패스워드 ? ");
			dto.setPwd(br.readLine());
			
			System.out.print("이름 ? ");
			dto.setName(br.readLine());

			System.out.print("생년월일 ? ");
			dto.setBirth(br.readLine());

			System.out.print("이메일 ? ");
			dto.setEmail(br.readLine());

			System.out.print("전화번호 ? ");
			dto.setTel(br.readLine());

			dao.updateMember(dto);

			System.out.println("회원 정보 수정 성공 !!!");
			
		} catch (SQLDataException e) {
			// 날짜등의 형식 잘못으로 인한 예외
			if (e.getErrorCode() == 1840 || e.getErrorCode() == 1861) {
				System.out.println("에러-날짜 입력 형식 오류입니다.");
			} else {
				System.out.println(e.toString());
			}
		} catch (SQLException e) {
			if (e.getErrorCode() == 1407) { // UPDATE - NOT NULL 위반
				System.out.println("에러-필수 입력사항을 입력하지 않았습니다.");
			} else if(e.getErrorCode() == 20100){
					System.out.println("등록된 아이디가 아닙니다.");
			} else {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println();
	}

	protected void delete() {
		System.out.println("\n[회원 탈퇴]");

		try {
			String id;
			System.out.print("탈퇴할 아이디 ? ");
			id = br.readLine();

			dao.deleteMember(id);
		
			System.out.println("회원 탈퇴 성공 !!!");
			
		} catch (SQLException e) {
			if(e.getErrorCode() == 20100){
				System.out.println("등록된 아이디가 아닙니다.");
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println();
	}

	protected void findById() {
		System.out.println("\n[아이디 검색]");

		try {
			String id;
			System.out.print("검색할 아이디 ? ");
			id = br.readLine();

			MemberDTO dto = dao.findById(id);
			if (dto == null) {
				System.out.println("등록된 정보가 아닙니다.\n");
				return;
			}

			System.out.print(dto.getId() + "\t");
			System.out.print(dto.getName() + "\t");
			System.out.print(dto.getPwd() + "\t");
			System.out.print(dto.getBirth() + "\t");
			System.out.print(dto.getEmail() + "\t");
			System.out.println(dto.getTel());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println();
	}

	protected void findByName() {
		System.out.println("\n[이름 검색]");

		try {
			String name;
			System.out.print("검색할 이름 ? ");
			name = br.readLine();

			List<MemberDTO> list = dao.listMember(name);
			
			if(list.size() == 0) {
				System.out.println("등록된 자료가 없습니다.\n");
				return;
			}
			
			for (MemberDTO dto : list) {
				System.out.print(dto.getId() + "\t");
				System.out.print(dto.getName() + "\t");
				System.out.print(dto.getPwd() + "\t");
				System.out.print(dto.getBirth() + "\t");
				System.out.print(dto.getEmail() + "\t");
				System.out.println(dto.getTel());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println();
	}

	protected void listAll() {
		System.out.println("\n[전체 리스트]");

		List<MemberDTO> list = dao.listMember();
		for (MemberDTO dto : list) {
			System.out.print(dto.getId() + "\t");
			System.out.print(dto.getName() + "\t");
			System.out.print(dto.getPwd() + "\t");
			System.out.print(dto.getBirth() + "\t");
			System.out.print(dto.getEmail() + "\t");
			System.out.println(dto.getTel());
		}
		
		System.out.println();
	}
}
