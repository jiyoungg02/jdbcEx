package db.user;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import db.util.DBConn;

public class MainUI {
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	private LoginInfo login = new LoginInfo();
	private GuestUI guestUI = new GuestUI(login);
	private MemberUI memberUI = new MemberUI(login);
	private ProductUI productUI = new ProductUI(login);
	private ProductManageUI manageUI = new ProductManageUI(login);
	
	public void menu() {
		while(true) {
			MemberDTO loginMember = login.loginMember();
			
			if(loginMember == null) {
				menuGuest();
			} else if(loginMember.getId().equals("admin")){
				menuAdmin();
			} else {
				menuUser();
			}
		}
	}
	
	private void menuGuest() {
		int ch;
		
		do {
			ch = 0;
			try {
				System.out.print("1.로그인 2.회원가입 3.종료 => ");
				ch = Integer.parseInt(br.readLine());
			} catch (Exception e) {
			}
		} while(ch < 1 || ch > 3);
		
		if(ch == 3) {
			DBConn.close();
			System.exit(0);
		}
		
		switch(ch) {
		case 1: guestUI.login(); break;
		case 2: guestUI.register(); break;
		}
	}
	
	private void menuUser() {
		int ch;
		
		try {
			MemberDTO loginMember = login.loginMember();
			
			System.out.println("\n["+loginMember.getName()+"] 님");
			
			do {
				System.out.print("1.상품검색 2.상품구매 3.정보수정 4.로그아웃 5.회원탈퇴 => ");
				ch = Integer.parseInt(br.readLine());
			} while(ch < 1 || ch > 5);
			
			switch(ch) {
			case 1: productUI.productSearch(); break;
			case 2: productUI.productBuy(); break;
			case 3: memberUI.update(); break;
			case 4: login.logout(); System.out.println(); break;
			case 5: memberUI.delete(); break;
			}
		} catch (Exception e) {
		}
	}

	private void menuAdmin() {
		int ch;
		
		try {
			System.out.println("\n[관리자] 님");
			
			do {
				System.out.print("1.상품등록 2.상품수정 3.상품검색 4.상품리스트 5.회원검색 6.회원리스트 7.로그아웃 => ");
				ch = Integer.parseInt(br.readLine());
			} while(ch<1 || ch>7);
			
			switch(ch) {
			case 1: manageUI.productAdd(); break;
			case 2: manageUI.productUpdate(); break;
			case 3: manageUI.productSearch(); break;
			case 4: manageUI.productList(); break;
			case 5: memberUI.findByName(); break;
			case 6: memberUI.listAll(); break;
			case 7: login.logout(); System.out.println(); break;
			}
		} catch (Exception e) {
		}		
	}
}
