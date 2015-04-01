package deshant.accel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

	private SensorManager senM;
	private Sensor aSensor;

	private long lastUpdate = 0;
	private double last[] = new double[3];
	Boolean init = false;
	double accl[] = new double[3];

	// private static final int SHAKE_THRESHOLD = 600;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		senM = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		aSensor = senM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		senM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		senM.registerListener(this, aSensor, SensorManager.SENSOR_DELAY_NORMAL);
		last = null;

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

		double relative[] = new double[3];
		// long diffTime;
		String disp = "";

		// ensure last[] has valid value for calculations
		if (init == false) {
			last = accl;
			init = true;
			System.out.println("initialized\n");
			return;

		}

		long curTime = System.currentTimeMillis();

		if ((curTime - lastUpdate) > 1000) {

			// diffTime = (curTime - lastUpdate);
			lastUpdate = curTime;
			for (int i = 0; i < 3; i++) {
				/*
				 * double temp = (double)(accl[i]*10 - (last[i])*10);
				 * NumberFormat nf = NumberFormat.getInstance();
				 * nf.setMinimumFractionDigits(9); temp /= 10;
				 * System.out.println(nf.format(temp));
				 */
				disp += String.valueOf(accl[i]) + " \n";
			}

			last = accl;
			show(disp);
		} else
			return;
	}

	@SuppressLint("NewApi")
	void show(String disp) {

		RelativeLayout layout = new RelativeLayout(this);
		TextView text = new TextView(this);
		RelativeLayout.LayoutParams for_text;
		text.setId(2);
		text.setTextSize(15);
		text.setGravity(Gravity.CENTER);
		text.setY(100);

		// System.out.println(disp);
		text.setText(disp);

		// set layout
		for_text = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		for_text.addRule(RelativeLayout.CENTER_HORIZONTAL, text.getId());
		for_text.setMargins(0, 20, 0, 20);
		layout.addView(text, for_text);
		setContentView(layout);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}
}
