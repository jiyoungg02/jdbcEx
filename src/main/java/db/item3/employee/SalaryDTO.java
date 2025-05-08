package db.item3.employee;

public class SalaryDTO {
	private int salaryNum;
	private String sabeon;
	private String name;
	private String payDate;     // 지급년월
	private String paymentDate; // 지급일자
	private int pay;
	private int sudang;
	private int tot;
	private int tax;
	private int afterPay;
	private String memo;
	
	public int getSalaryNum() {
		return salaryNum;
	}
	public void setSalaryNum(int salaryNum) {
		this.salaryNum = salaryNum;
	}
	public String getSabeon() {
		return sabeon;
	}
	public void setSabeon(String sabeon) {
		this.sabeon = sabeon;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public String getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}
	public int getPay() {
		return pay;
	}
	public void setPay(int pay) {
		this.pay = pay;
	}
	public int getSudang() {
		return sudang;
	}
	public void setSudang(int sudang) {
		this.sudang = sudang;
	}
	public int getTot() {
		return tot;
	}
	public void setTot(int tot) {
		this.tot = tot;
	}
	public int getTax() {
		return tax;
	}
	public void setTax(int tax) {
		this.tax = tax;
	}
	public int getAfterPay() {
		return afterPay;
	}
	public void setAfterPay(int afterPay) {
		this.afterPay = afterPay;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
}
