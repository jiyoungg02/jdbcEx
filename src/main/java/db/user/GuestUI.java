package db.user;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class GuestUI {
	private BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
	private MemberDAO dao = new MemberDAOImpl();
	private LoginInfo loginInfo = null;
	
	public GuestUI(LoginInfo loginInfo) {
		this.loginInfo = loginInfo;
	}
	
	public void register() {
		System.out.println("\n[회원가입]");
		
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
			
			System.out.println("회원가입이 완료 되었습니다.");

		} catch (SQLIntegrityConstraintViolationException e) {
			// 기본키 중복, NOT NULL등의 제약조건 위반에 의한 예외 발생-무결성 제약 조건 위반
			if(e.getErrorCode()==1) {
				System.out.println("아이디 중복입니다.");
			} else if(e.getErrorCode()==1400){ // INSERT-NOT NULL 위반
				System.out.println("필수 입력사항을 입력하지 않았습니다.");
			} else {
				System.out.println(e.toString());
			}
		} catch (SQLDataException e) {
			// 날짜등의 형식 잘못으로 인한 예외
			if(e.getErrorCode()==1840 || e.getErrorCode()==1861) {
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

	public void login() {
		System.out.println("\n[로그인]");

		String id, pwd;
		
		try {
			System.out.print("아이디 ? ");
			id = br.readLine();
			System.out.print("패스워드 ? ");
			pwd = br.readLine();
			
			MemberDTO dto = dao.findById(id);
			if(dto == null || ! dto.getPwd().equals(pwd)) {
				System.out.println("아이디 또는 패스워드가 일치하지 않습니다.");
				return;
			}
			
			loginInfo.login(dto);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
		
	}
}
