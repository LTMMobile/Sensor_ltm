package com.ltm.sensor;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

public class Sensor_ltmActivity extends Activity  {
	private SensorManager _mSensorManager;
	private Sensor _mAccelerometer;
	private Sensor _mOrientation;
	private Sensor _mProximity;
	private Sensor _mLight;
	
	private AccelerometerEvent _accelero_event = new AccelerometerEvent();
	private OrientationEvent _orientation_event = new OrientationEvent();
	private ProximityEvent _proximity_event = new ProximityEvent();
	private LightEvent _light_event = new LightEvent();
	
	private TextView mTimestamp;
	private TextView mAccuracy;
	private TextView mSensorX; 
	private TextView mSensorY; 
	private TextView mSensorZ; 
	private SeekBar _seek_x, _seek_y, _seek_z;
	private SeekBar _seek_or_x, _seek_or_y, _seek_or_z;
	
	@SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		// 1.
		_mSensorManager = (SensorManager)getSystemService( SENSOR_SERVICE );

		// 2.
        _mAccelerometer = _mSensorManager.getDefaultSensor( Sensor.TYPE_ACCELEROMETER );
        //_mPressure = _mSensorManager.getDefaultSensor( Sensor.TYPE_PRESSURE );
        //mMagneticField = mSensorManager.getDefaultSensor( Sensor.TYPE_MAGNETIC_FIELD );
        _mOrientation = _mSensorManager.getDefaultSensor( Sensor.TYPE_ORIENTATION );
        _mProximity = _mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        _mLight = _mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        // lister les sensors disponibles dans votre device
        List<Sensor> sensors = _mSensorManager.getSensorList( Sensor.TYPE_ALL);  
        for( int t=0; t<sensors.size(); t++ ){
        		Log.v("ltm", "Nom : " + sensors.get(t).getName() + " -> Vendor : " +  sensors.get(t).getVendor() );       	
        }
        
        _seek_x = (SeekBar)findViewById( R.id.seekBar_x );
        _seek_y = (SeekBar)findViewById( R.id.seekBar_y );
        _seek_z = (SeekBar)findViewById( R.id.seekBar_z );
        
        _seek_or_x = (SeekBar)findViewById( R.id.seekBar_or_x );
        _seek_or_y = (SeekBar)findViewById( R.id.seekBar_or_y );
        _seek_or_z = (SeekBar)findViewById( R.id.seekBar_or_z );
        
        mTimestamp = (TextView)findViewById( R.id._timestamp );
        mAccuracy = (TextView)findViewById( R.id._accuracy );
        mSensorX = (TextView)findViewById( R.id._sensorx );
        mSensorY = (TextView)findViewById( R.id._sensory );
        mSensorZ = (TextView)findViewById( R.id._sensorz );
        mTimestamp.setText("");
        mAccuracy.setText("");
        mSensorX.setText("");
        mSensorY.setText("");
        mSensorZ.setText("");
    }

    protected void onResume() {
        super.onResume();
        _mSensorManager.registerListener(_accelero_event, _mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        _mSensorManager.registerListener(_orientation_event, _mOrientation, SensorManager.SENSOR_DELAY_NORMAL);
        _mSensorManager.registerListener(_proximity_event, _mProximity, SensorManager.SENSOR_DELAY_NORMAL);
        _mSensorManager.registerListener(_light_event, _mLight, SensorManager.SENSOR_DELAY_FASTEST);
    }

    protected void onStop() {
        super.onStop();
    	_mSensorManager.unregisterListener(_accelero_event);
        _mSensorManager.unregisterListener(_orientation_event);
        _mSensorManager.unregisterListener(_proximity_event);
        _mSensorManager.unregisterListener(_light_event);
    }

    class LightEvent implements SensorEventListener {

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {}

		@Override
		public void onSensorChanged(SensorEvent event) {
			Log.v("ltm", "Lux = " + Float.valueOf(event.values[0]).toString());
		}
    }
    
    class ProximityEvent implements SensorEventListener {

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) { }

		@Override
		public void onSensorChanged(SensorEvent event) {
			Log.v("ltm", "Proximité = " + new Float(event.values[0]).toString());
			
		}
    }
    
    class AccelerometerEvent implements SensorEventListener {
		@SuppressLint("UseValueOf")
		@Override
		public void onAccuracyChanged(Sensor arg0, int accuracy ) {
			mAccuracy.setText( new Integer(accuracy).toString() );
		}

		private int _cpt = 0;
		
		@SuppressLint("UseValueOf")
		@Override
		public void onSensorChanged(SensorEvent event) {
			
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){	
				
				_cpt++;
				if( _cpt<10 ) {
					return;
				}else
					_cpt=0;
				
				//Log.v("LTM", "AccelerometerEvent");
				mTimestamp.setText( new Long(event.timestamp).toString() );
				mAccuracy.setText( new Integer(event.accuracy).toString() );
				
				float x = event.values[0];
				mSensorX.setText( new Float(x).toString() );
				_seek_x.setProgress( new Float(x*10.0).intValue() );
				
				float y = event.values[1];
				mSensorY.setText( new Float(y).toString() );
				int i = new Float(y*10.0).intValue();
				_seek_y.setProgress( i );
				
				float z = event.values[2];
				mSensorZ.setText( new Float(z).toString() );
				i = new Float(z*10.0).intValue();
				_seek_z.setProgress(i);
			}
		}
    }
    
    class OrientationEvent implements SensorEventListener {
		@Override
		public void onAccuracyChanged(Sensor arg0, int accuracy ) {
			mAccuracy.setText( new Integer(accuracy).toString() );
		}

		//comment
		@Override
		public void onSensorChanged(SensorEvent event) {
			if ( event.sensor.getType() == Sensor.TYPE_ORIENTATION ){		
				//Log.v("LTM", "OrientationEvent");
				
				float x = event.values[0];
				//mSensorX.setText( new Float(x).toString() );
				//Log.v("LTM", "Azimuth = " + new Float(x).intValue());
				_seek_or_x.setProgress( new Float(x).intValue() );
				
				float y = event.values[1];
				//mSensorY.setText( new Float(y).toString() );
				int i = new Float(y).intValue();
				//Log.v("LTM", "Pitch = " + new Float(y).intValue());
				_seek_or_y.setProgress( i );
				
				float z = event.values[2];
				//mSensorZ.setText( new Float(z).toString() );
				i = new Float(z).intValue();
				//Log.v("LTM", "Roll = " + new Float(z).intValue());
				_seek_or_z.setProgress(i);
			}
		}
    }
}