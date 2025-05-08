package db.user;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

public class MemberUI {
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private MemberDAO dao = new MemberDAOImpl();
	private LoginInfo loginInfo = null;

	public MemberUI(LoginInfo loginInfo) {
		this.loginInfo = loginInfo;
	}

	public void update() {
		System.out.println("\n[정보수정]");

		try {
			MemberDTO dto = loginInfo.loginMember();
			
			System.out.print("패스워드 ? ");
			dto.setPwd(br.readLine());

			System.out.print("생년월일 ? ");
			dto.setBirth(br.readLine());

			System.out.print("이메일 ? ");
			dto.setEmail(br.readLine());

			System.out.print("전화번호 ? ");
			dto.setTel(br.readLine());

			dao.updateMember(dto);

			System.out.println("회원정보가 수정되었습니다.");
		} catch (SQLException e) {
			if (e.getErrorCode() == 1407) { // UPDATE - NOT NULL 위반
				System.out.println("에러-필수 입력사항을 입력하지 않았습니다.");
			} else if(e.getErrorCode()==1840 || e.getErrorCode()==1861) {
				System.out.println("날짜 입력 형식 오류입니다.");
			} else {
				System.out.println(e.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	public void delete() {
		System.out.println("\n[회원탈퇴]");
		char ch;

		try {
			System.out.print("회원을 탈퇴 하시겠습니까[Y/N] ? ");
			ch = br.readLine().charAt(0);

			if (ch == 'Y' || ch == 'y') {
				dao.deleteMember(loginInfo.loginMember().getId());
				loginInfo.logout();
			}

		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("회원 탈퇴 실패...");
		}
		System.out.println();
		
	}

	public void findById() {
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

			System.out.println("아이디\t이름\t생년월일\t\t이메일\t\t전화번호");
			System.out.println("---------------------------------------------------------------");

			System.out.print(dto.getId() + "\t");
			System.out.print(dto.getName() + "\t");
			// System.out.print(dto.getPwd() + "\t");
			System.out.print(dto.getBirth() + "\t");
			System.out.print(dto.getEmail() + "\t");
			System.out.println(dto.getTel());
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

			List<MemberDTO> list = dao.listMember(name);
			if (list.size() == 0) {
				System.out.println("등록된 정보가 없습니다.\n");
				return;
			}

			System.out.println("아이디\t이름\t생년월일\t\t이메일\t\t전화번호");
			System.out.println("---------------------------------------------------------------");
			for (MemberDTO dto : list) {
				System.out.print(dto.getId() + "\t");
				System.out.print(dto.getName() + "\t");
				// System.out.print(dto.getPwd() + "\t");
				System.out.print(dto.getBirth() + "\t");
				System.out.print(dto.getEmail() + "\t");
				System.out.println(dto.getTel());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	public void listAll() {
		System.out.println("\n[전체 리스트]");

		List<MemberDTO> list = dao.listMember();

		System.out.println("아이디\t이름\t생년월일\t\t이메일\t\t전화번호");
		System.out.println("---------------------------------------------------------------");
		for (MemberDTO dto : list) {
			System.out.print(dto.getId() + "\t");
			System.out.print(dto.getName() + "\t");
			// System.out.print(dto.getPwd() + "\t");
			System.out.print(dto.getBirth() + "\t");
			System.out.print(dto.getEmail() + "\t");
			System.out.print(dto.getTel() + "\n");
		}
		System.out.println();
	}
}
