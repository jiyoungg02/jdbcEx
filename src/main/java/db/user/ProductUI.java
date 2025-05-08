package db.user;

public class ProductUI {
	private LoginInfo loginInfo = null;

	public ProductUI(LoginInfo loginInfo) {
		this.loginInfo = loginInfo;
	}
	
	public void productSearch() {
		System.out.println("\n[상품검색]");
	}
	
	public void productBuy() {
		System.out.println("\n[상품구매]");
		
		MemberDTO loginMember = loginInfo.loginMember();
		System.out.println("구매유저 : " + loginMember.getName() +"\n");
		
	}
}
