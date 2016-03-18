package info.xijiao.dof.activity;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
    private Wheel mFocalWheel;
    private TextView mFocalText;
    private Wheel mApertureWheel;
    private TextView mApertureText;
    private TextView mDistanceText;
    private Spinner mSensorSizeSpinner;
    private Spinner mDistanceUnitSpinner;
    private DepthView mDepthView;
    private Wheel mDistanceWheel;

    public static Double[] typedArray2DoubleList(TypedArray array) {
        Double list[] = new Double[array.length()];
        for (int i = 0; i < array.length(); i++) {
            list[i] = (double)array.getFloat(i, 0.0f);
        }
        return list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        activity = this;

        TypedArray distanceArray = getResources().obtainTypedArray(R.array.distance_list);
        Double[] distance_list = typedArray2DoubleList(distanceArray);
        distanceArray.recycle();

        TypedArray apertureArray = getResources().obtainTypedArray(R.array.aperture_list);
        Double[] aperture_list = typedArray2DoubleList(apertureArray);
        apertureArray.recycle();

        TypedArray focalArray = getResources().obtainTypedArray(R.array.focal_list);
        Double[] focal_list = typedArray2DoubleList(focalArray);
        focalArray.recycle();

        mDof = new DepthOfFieldCalculator(distance_list, aperture_list, focal_list);

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

        mApertureText = (TextView)findViewById(R.id.aperture_text);
        mApertureWheel = (Wheel)findViewById(R.id.aperture_wheel);
        mApertureWheel.setAdapter(new Wheel.WheelAdapter() {
            @Override
            public int getCount() {
                return mDof.getApertureCount();
            }

            @Override
            public String getItemText(int position) {
                return UnitManager.getInstance().getApertureText(activity, mDof.getApertureAtIndex(position));
            }
        });
        mApertureWheel.setScrollListener(new Wheel.OnScrollListener() {
            @Override
            public void onWheelScroll(int position, float offset) {
                mDof.setAperturePosition(position, offset);
                onDataChanged();
            }
        });

        mFocalText = (TextView)findViewById(R.id.focal_text);
        mFocalWheel = (Wheel)findViewById(R.id.focal_wheel);
        mFocalWheel.setAdapter(new Wheel.WheelAdapter() {
            @Override
            public int getCount() {
                return mDof.getFocalCount();
            }

            @Override
            public String getItemText(int position) {
                return UnitManager.getInstance().getFocalText(activity, mDof.getFocalAtIndex(position));
            }
        });
        mFocalWheel.setScrollListener(new Wheel.OnScrollListener() {
            @Override
            public void onWheelScroll(int position, float offset) {
                mDof.setFocalPosition(position, offset);
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

        mDistanceText.setText(UnitManager.getInstance().getCompatDistanceText(this, mDof.getCurDistance()));
        mApertureText.setText(UnitManager.getInstance().getApertureText(this, mDof.getCurAperture()));
        mFocalText.setText(UnitManager.getInstance().getFocalText(this, mDof.getCurFocal()));

        mDepthView.setData(mDof.getDepthOfField(), mDof.getCurDistance(),
                mDof.getCurDistance() - mDof.getNearDepthOfField(),
                mDof.getCurDistance() + mDof.getFarDepthOfField());
    }
}
