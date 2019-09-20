package com.physicomtech.kit.physis_wallswitch_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.physicomtech.kit.physislibrary.PHYSIsMQTTActivity;

public class ControlActivity extends PHYSIsMQTTActivity {

    private final String SERIAL_NUMBER = "XXXXXXXXXXXX";            // PHYSIs Maker Kit 시리얼번호

    public static final String PUB_TOPIC = "TURNON";                // Publish Topic
    public static final String TURN_ON = "1";                       // Publish Message
    public static final String TURN_OFF = "2";

    Button btnConnect, btnDisconnect, btnSwitchOn, btnSwitchOff;    // 액티비티 위젯
    ProgressBar pgbConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        initWidget();                   // 위젯 생성 및 초기화 함수 호출
        setEventListener();             // 이벤트 리스너 설정 함수 호출
    }


    /*
        # 위젯 생성 및 초기화
     */
    private void initWidget() {
        btnConnect = findViewById(R.id.btn_connect);                // 버튼 생성
        btnDisconnect = findViewById(R.id.btn_disconnect);
        btnSwitchOn = findViewById(R.id.btn_switch_on);
        btnSwitchOff = findViewById(R.id.btn_switch_off);
        pgbConnect = findViewById(R.id.pgb_connect);                // 프로그래스 생성
    }

    /*
        # 뷰 (버튼) 이벤트 리스너 설정
     */
    private void setEventListener() {
        btnConnect.setOnClickListener(new View.OnClickListener() {                  // 연결 버튼
            @Override
            public void onClick(View v) {           // 버튼 클릭 시 호출
                btnConnect.setEnabled(false);               // 연결 버튼 비활성화 설정
                pgbConnect.setVisibility(View.VISIBLE);     // 연결 프로그래스 가시화 설정
                connectMQTT();                              // MQTT 연결 시도
            }
        });

        btnDisconnect.setOnClickListener(new View.OnClickListener() {               // 연결 종료 버튼
            @Override
            public void onClick(View v) {
                disconnectMQTT();                               // MQTT 연결 종료
                setConnectedResult(false);                      // 연결 종료에 따른 상태 설정
            }
        });

        btnSwitchOn.setOnClickListener(new View.OnClickListener() {                  // 스위치 On 버튼
            @Override
            public void onClick(View v) {
                publish(SERIAL_NUMBER, PUB_TOPIC, TURN_ON);     // Switch On 메시지 Publish
            }
        });

        btnSwitchOff.setOnClickListener(new View.OnClickListener() {                 // 스위치 Off 버튼
            @Override
            public void onClick(View v) {
                publish(SERIAL_NUMBER, PUB_TOPIC, TURN_OFF);    // Switch Off 메시지 Publish
            }
        });
    }

    /*
        # MQTT 연결 결과 수신
        - MQTT Broker 연결에 따른 결과를 전달받을 때 호출
        - 인자로 연결 성공 여부를 전달
     */
    @Override
    protected void onMQTTConnectedStatus(boolean result) {
        super.onMQTTConnectedStatus(result);
        setConnectedResult(result);                             // MQTT 연결 결과 처리 함수 호출
    }

    /*
       # MQTT 연결 결과 처리
    */
    private void setConnectedResult(boolean result){
        pgbConnect.setVisibility(View.INVISIBLE);               // 연결 프로그래스 비가시화 설정

        String toastMsg;                                        // 연결 결과에 따른 Toast 메시지 출력
        if(result){
            toastMsg = "MQTT Broker와 연결되었습니다.";
        }else{
            toastMsg = "MQTT Broker와 연결 종료/실패하였습니다.";
        }
        Toast.makeText(getApplicationContext(), toastMsg, Toast.LENGTH_SHORT).show();

        btnConnect.setEnabled(!result);                          // 연결 버튼 활성화 상태 설정
        btnDisconnect.setEnabled(result);
    }
}