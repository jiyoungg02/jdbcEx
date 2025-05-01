package db.employee;

import java.util.InputMismatchException;
import java.util.Scanner;

import db.util.DBConn;

public class MainUI {
	private Scanner sc = new Scanner(System.in);
	
	private EmployeeUI emp = new EmployeeUI();
	private SalaryUI sal = new SalaryUI(emp);
	
	public void menu() {
		System.out.println(">> 인사관리 프로그램 <<");
		int ch;
		
		while(true) {
			System.out.println("\n[Main]");
			try {
				System.out.print("1.사원관리 2.급여관리 3.종료 => ");
				ch = sc.nextInt();
				
				if(ch == 3) {
					DBConn.close();
					break;
				}
				
				switch(ch) {
				case 1: emp.employeeManage(); break;
				case 2: sal.salaryManage(); break;
				}
			} catch (InputMismatchException e) {
				sc.nextLine();
			}
		}

	}
}
