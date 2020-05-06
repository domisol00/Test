package welcome_example;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class RootController implements Initializable{

	@FXML private PasswordField txtPassword;
	//콤보박스에 리스트 내용이 String타입이므로 제네릭 선언
	private long inputStart = 0;
	private long inputEnd = 0;
	private long intervalStart = 0;
	private long intervalEnd = 0;
	private long totalInputTime = 0;
	private ArrayList<Integer> inputList = new ArrayList<>();
	private ArrayList<Integer> intervalList = new ArrayList<>();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		txtPassword.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(!event.getCode().equals(KeyCode.TAB) && !event.getCode().equals(KeyCode.ENTER)
						&& !event.getCode().equals(KeyCode.BACK_SPACE) && !event.getCode().equals(KeyCode.DELETE)) {
					inputStart = System.currentTimeMillis();
					intervalEnd = System.currentTimeMillis();
					long intervalTime = intervalEnd - intervalStart;
					if(intervalStart != 0) {
						//System.out.println("인터벌 시간 : " + intervalTime);
						intervalList.add((int) intervalTime);
					}
				}
				if(event.getCode().equals(KeyCode.ENTER)) {
					intervalStart = 0;
					btnLogin(new ActionEvent());
				}
			}
		});
		
		txtPassword.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(!event.getCode().equals(KeyCode.TAB) && !event.getCode().equals(KeyCode.ENTER)
						&& !event.getCode().equals(KeyCode.BACK_SPACE) && !event.getCode().equals(KeyCode.DELETE)) {
					inputEnd = System.currentTimeMillis();
					intervalStart = System.currentTimeMillis();					
					long inputTime = inputEnd - inputStart;
					//System.out.println("키보드 입력 시간 : " + (inputEnd - inputStart));
					inputList.add((int) inputTime);
				}
				
				if(event.getCode().equals(KeyCode.BACK_SPACE) || event.getCode().equals(KeyCode.DELETE)) {
					intervalStart = 0;
				}
			}
		});
		
		
	}
	
	//내용 출력
	public void btnLogin(ActionEvent event) {
		double high = 0.68;
		double normal = 0.85;
		double lower = 1.04;

		double[] inputAvg = new double[] {140,149,106,62,25,79,97,59,49,98,129};
		double[] inputStdev = new double[] {25,14,17,32,6,41,12,31,28,13,26};
		int count=0;	//불일치 할 시 카운트 증가
		try {
			
			for(int i=0; i<inputList.size(); i++) {
				float z = (float) Math.abs((inputList.get(i) - inputAvg[i]) / inputStdev[i]);
				System.out.println(z);
				if(!(z < lower)) {
					System.out.println((i+1) + "번째 값이 다릅니다");
					count++;				
				}
			}
			
			for(int i=0; i<inputList.size(); i++) {
				totalInputTime += inputList.get(i);
			}
			
			for(int i=0; i<intervalList.size(); i++) {
				totalInputTime += intervalList.get(i);
			}
			
			if(!((float)Math.abs((totalInputTime - 3217) / 369) < lower)) {
				System.out.println("총입력시간이 다릅니다.");
				count++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("고려하는 총 카운트 수 : " + (inputAvg.length + 1));
		
		//허가 가능한 카운트수 (이 수보다 크면 불일치, 작거나 같으면 일치)
		int permitCount = (int) Math.round(((inputAvg.length + 1) * 0.5));	
		
		if(count > permitCount) {
			System.out.println("불일치하는 카운트 : " + count);
			System.out.println("불일치 허용 최대 카운트 : " + permitCount);
			System.out.println("======사용자 불일치=========");
		} else {
			System.out.println("불일치하는 카운트 : " + count);
			System.out.println("불일치 허용 최대 카운트 : " + permitCount);
			System.out.println("=======사용자 일치==========");
		}
		
		System.out.println("키보드 누르는 시간");
		for(int a : inputList) {
			System.out.println(a);
		}
		
//		System.out.println("키보드 인터벌 시간");
//		for(int a : intervalList) {
//			System.out.println(a);
//		}
		
		
		System.out.println(totalInputTime);
		totalInputTime = 0;
		inputList = new ArrayList<>();
		intervalList = new ArrayList<>();
		txtPassword.setText("");
		System.out.println();
	}
	
	//어플리케이션 종료
	public void btnCancel(ActionEvent event) {
		Platform.exit();
	}
	
}
