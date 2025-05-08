package db.user;

public class ProductManageUI {
	private LoginInfo loginInfo = null;

	public ProductManageUI(LoginInfo loginInfo) {
		this.loginInfo = loginInfo;
	}
	
	public void productAdd() {
		System.out.println("\n[상품등록]");
		
		MemberDTO loginMember = loginInfo.loginMember();
		System.out.println("등록유저 : " + loginMember.getName() +"\n");
	}

	public void productUpdate() {
		System.out.println("\n[상품수정]");
	}
	
	public void productSearch() {
		System.out.println("\n[상품검색]");
	}
	
	public void productList() {
		System.out.println("\n[상품리스트]");
	}
}
