package sample.application.ball;

import android.app.Activity;
import android.os.Bundle;
import java.util.List;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.PowerManager;
import android.view.KeyEvent;
import android.widget.TextView;
import android.content.Context;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class BallActivity extends Activity implements SensorEventListener{
	
	public static float acceler_y, acceler_x;	//加速度センターからのデータ
	private SensorManager sensorManager;
	PowerManager.WakeLock wl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
        setContentView(new BoardView(this));
        
        //センサーオブジェクト作成
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
    }
    
    @Override
	protected void onResume() {
		super.onResume();
		//加速度センサーリスナー登録
		List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
		if(sensors.size() > 0){
			Sensor sensor = sensors.get(0);
			sensorManager.registerListener(this, sensor,1);
		}
		
		PowerManager pm=(PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK+
		    PowerManager.ON_AFTER_RELEASE, "My Tag");
		wl.acquire();
	}
    
    @Override
	protected void onStop() {
		super.onStop();
		//加速度センサーリスナー解除
		sensorManager.unregisterListener(this);
		if(wl.isHeld()) wl.release();
	}


	@Override
	public void onSensorChanged(SensorEvent event) {
		//ランドスケープ固定のためxyを入れ替える
		acceler_y = event.values[0];	//x軸
		acceler_x = event.values[1];	//y軸
//		((TextView)findViewById(R.id.textView1)).setText(acceler_x+" , "+acceler_y);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			AlertDialog.Builder ab = new AlertDialog.Builder(this);
			ab.setMessage(R.string.message_exit);
			ab.setPositiveButton(R.string.label_yes,
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			ab.setNegativeButton(R.string.label_no,
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				}
			});
			ab.show();
			return true;

		}
		return super.onKeyDown(keyCode, event);
	}
}
