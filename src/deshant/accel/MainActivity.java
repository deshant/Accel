package deshant.accel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

	private SensorManager senM;
	private Sensor aSensor;
	
	private long lastUpdate = 0;
	Boolean init = false;
	double accl[] = new double[3];
	long fall_duration = 0, end_time = 0;
	Boolean fall = false;
	String disp = "WELCOME\n\n";

	// private static final int SHAKE_THRESHOLD = 600;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		senM = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		aSensor = senM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		senM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		senM.registerListener(this, aSensor, SensorManager.SENSOR_DELAY_NORMAL);
		show(disp);
	}

	@Override
	protected void onPause() {
		super.onPause();
		senM.unregisterListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		senM.registerListener(this, aSensor, SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {

		accl[0] = (double) (sensorEvent.values[0]);
		accl[1] = (double) (sensorEvent.values[1]);
		accl[2] = (double) (sensorEvent.values[2]);

		// long diffTime;
		double hit_speed = 0;
		long curTime = System.nanoTime();

		if ((curTime - lastUpdate) > 10) {
			// diffTime = (curTime - lastUpdate);
			lastUpdate = curTime;

			for (int i = 0; i < 3; i++) {
				/*
				 * double temp = (double)(accl[i] - (last[i])); NumberFormat nf
				 * = NumberFormat.getInstance(); nf.setMinimumFractionDigits(9);
				 * temp /= 10; System.out.println(nf.format(temp));
				 */
				// disp += String.valueOf(accl[i]) + " \n";
			}
			double accl_vector = (Math.sqrt(Math.pow(accl[0], 2)
					+ Math.pow(accl[1], 2) + Math.pow(accl[2], 2)));

			if (accl_vector < 4.5) {
				// phone is in free fall
				fall = true;
				System.out.println(accl_vector);
				end_time = curTime;

				// start of fall
				if (init == false) {
					fall_duration = curTime;
					init = true;
				}
			}
			/*
			 * sudden bump/stop else if (accl_vector > 12) { fall = false; }
			 */
			else
				fall = false;

			// check for end of fall
			if (fall == false && init == true) {
				fall = init = false;
				fall_duration = (end_time - fall_duration) / 1000000; // convert
																		// to ms
				// filter falls
				if (fall_duration > 80) {
					System.out.println(fall_duration + "ms");

					hit_speed = (9.8 * fall_duration) / 1000;
					System.out.println(hit_speed);
					disp += String.valueOf("SPEED OF IMPACT\n" + hit_speed + " m/s\n\n");
					disp += String.valueOf("DURATION OF FREE FALL\n"
							+ fall_duration + "ms\n\n");
					fall_duration = 0;
				}
			}
			show(disp);
		}
	}

	@SuppressLint("NewApi")
	void show(String value) {

		//m_Scroll.removeAllViews();
		TextView text = (TextView) findViewById(R.id.data);
		text.setText(value);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}
}
