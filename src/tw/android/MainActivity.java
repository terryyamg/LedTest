package tw.android;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private NotificationManager notificationManager;
	private Notification notification;
	private boolean indeterminate = true; // true-開啟, false-關閉
	private int msInterval = 850; // 閃爍間隔時間(毫秒)

	private Button btStart, btStop;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notification = new Notification();
		
		/* 啟用通知的led燈 */
		notification.flags = Notification.FLAG_SHOW_LIGHTS;
		/*notification閃亮間隔時間 */
		notification.ledOnMS=100; 
		notification.ledOffMS=100;

		btStart = (Button) findViewById(R.id.btStart);
		btStop = (Button) findViewById(R.id.btStop);

		/* 開始 */
		btStart.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				indeterminate = true;// 設為開啟
				new TwoColorBlink().execute(); // 執行LED燈閃爍

				btStart.setEnabled(false); // 開始按鈕-不可按
				btStop.setEnabled(true); // 關閉按鈕-可按

			}
		});

		/* 停止 */
		btStop.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				indeterminate = false;// 設為關閉
				notificationManager.cancel(0); // 關閉notificationManager

				btStart.setEnabled(true); // 開始按鈕-可按
				btStop.setEnabled(false); // 關閉按鈕-不可按
			}
		});

	}

	@Override
	protected void onPause() {
		indeterminate = false; // 設為關閉
		notificationManager.cancel(0); // 關閉notificationManager
		super.onPause();
	}

	private class TwoColorBlink extends AsyncTask<Void, Void, Void> {

		private int counter = 0; // 初始化次數

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {

				synchronized (this) { //同一時間只有一個Thread執行
					
					notification.ledARGB = 0xFFFF0000;// 設定LED燈開始顏色

					while (indeterminate) { //重複閃爍

						notificationManager.notify(0, notification);//開啟notificationManager

						this.wait(msInterval); //閃爍間隔時間

						notificationManager.cancel(0); //關閉notificationManager

						if (counter++ % 2 == 0) {
							notification.ledARGB = 0xFF00FF00; //切換綠色
						} else {
							notification.ledARGB = 0xFF0000FF; //切換藍色
						}
						
					}

				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			

		}

	}

}