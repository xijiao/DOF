package info.xijiao.dof.activity;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import info.xijiao.dof.R;
import info.xijiao.dof.model.DepthOfFieldCalculator;
import info.xijiao.dof.util.UnitManager;
import info.xijiao.dof.view.DepthView;
import info.xijiao.dof.view.Wheel;

public class MainActivity extends AppCompatActivity {
    public static MainActivity activity;
    private DepthOfFieldCalculator mDof;
    private SeekBar mFocalBar;
    private TextView mFocalText;
    private SeekBar mApertureBar;
    private TextView mApertureText;
    private TextView mDistanceText;
    private Spinner mSensorSizeSpinner;
    private Spinner mDistanceUnitSpinner;
    private DepthView mDepthView;
    private Wheel mDistanceWheel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        activity = this;
        mDof = new DepthOfFieldCalculator(24, 70);
        TypedArray distanceArray = getResources().obtainTypedArray(R.array.distance_list);
        Double distanceList[] = new Double[distanceArray.length()];
        for (int i = 0; i < distanceArray.length(); i++) {
            distanceList[i] = (double)distanceArray.getFloat(i, 0.0f);
        }
        distanceArray.recycle();
        mDof.setDistanceList(distanceList);

        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                MainActivity.activity.onDataChanged();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        };
        mFocalBar = (SeekBar)findViewById(R.id.focal_bar);
        mFocalBar.setMax(mDof.getFocalBarMax());
        mFocalBar.setProgress(mDof.getFocalBarProgress());
        mFocalBar.setOnSeekBarChangeListener(listener);
        mFocalText = (TextView)findViewById(R.id.forcal_text);

        mApertureBar = (SeekBar)findViewById(R.id.aperture_bar);
        mApertureBar.setMax(mDof.getApertureBarMax());
        mApertureBar.setProgress(mDof.getApertureBarProgress());
        mApertureBar.setOnSeekBarChangeListener(listener);
        mApertureText = (TextView)findViewById(R.id.aperture_text);

        Spinner.OnItemSelectedListener spinnerListener = new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.activity.onDataChanged();
            }
            public void onNothingSelected(AdapterView<?> parent) {
                MainActivity.activity.onDataChanged();
            }
        };

        mSensorSizeSpinner = (Spinner)findViewById(R.id.sensorSizeSpinner);
        ArrayAdapter<CharSequence> sensorSizeAdapter = ArrayAdapter.createFromResource(this,
                R.array.sensers_array, android.R.layout.simple_spinner_item);
        sensorSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSensorSizeSpinner.setAdapter(sensorSizeAdapter);
        mSensorSizeSpinner.setSelection(mDof.getCircleOfConfusionIndex());
        mSensorSizeSpinner.setOnItemSelectedListener(spinnerListener);

        mDistanceUnitSpinner = (Spinner)findViewById(R.id.distanceUnitSpinner);
        ArrayAdapter<CharSequence> distanceUnitAdapter = ArrayAdapter.createFromResource(this,
                R.array.distance_size_array, android.R.layout.simple_spinner_item);
        distanceUnitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDistanceUnitSpinner.setAdapter(distanceUnitAdapter);
        mDistanceUnitSpinner.setSelection(mDof.getDistanceUnitIndex());
        mDistanceUnitSpinner.setOnItemSelectedListener(spinnerListener);

        mDepthView  = (DepthView)findViewById(R.id.depth_view);

        mDistanceText = (TextView)findViewById(R.id.distance_text);
        mDistanceWheel = (Wheel)findViewById(R.id.distance_wheel);
        mDistanceWheel.setAdapter(new Wheel.WheelAdapter() {
            @Override
            public int getCount() {
                return mDof.getDistanceCount();
            }

            @Override
            public String getItemText(int position) {
                return UnitManager.getInstance().getCompatDistanceText(activity,
                        mDof.getDistanceAtIndex(position));
            }
        });
        mDistanceWheel.setScrollListener(new Wheel.OnScrollListener() {
            @Override
            public void onWheelScroll(int position, float offset) {
                mDof.setDistancePosition(position, offset);
                onDataChanged();
            }
        });

        onDataChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onDataChanged()
    {
        Log.d("Trace", "onDataChanged");
        mDof.setCircleOfConfusionIndex(mSensorSizeSpinner.getSelectedItemPosition());

        mDof.setDistanceUnitIndex(mDistanceUnitSpinner.getSelectedItemPosition());

        mDof.setFocalBarProgress(mFocalBar.getProgress());
        mFocalText.setText(mDof.getCurFocalText());

        mDof.setApertureBarProgress(mApertureBar.getProgress());
        mApertureText.setText(mDof.getCurApertureText());

        mDistanceText.setText(UnitManager.getInstance().getCompatDistanceText(this, mDof.getCurDistance()));

        mDepthView.setData((double)mDof.getDepthOfField(), mDof.getCurDistance(),
                mDof.getCurDistance() - mDof.getNearDepthOfField(),
                mDof.getCurDistance() + mDof.getFarDepthOfField());
    }
}
