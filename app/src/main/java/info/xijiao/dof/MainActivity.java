package info.xijiao.dof;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static MainActivity activity;
    private DepthOfFieldCalculator dofCalculator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        activity = this;
        dofCalculator = new DepthOfFieldCalculator(24, 70);

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
        SeekBar focalBar = (SeekBar)findViewById(R.id.focalBar);
        focalBar.setMax(dofCalculator.getFocalBarMax());
        focalBar.setProgress(dofCalculator.getFocalBarProgress());
        focalBar.setOnSeekBarChangeListener(listener);

        SeekBar apertureBar = (SeekBar)findViewById(R.id.apertureBar);
        apertureBar.setMax(dofCalculator.getApertureBarMax());
        apertureBar.setProgress(dofCalculator.getApertureBarProgress());
        apertureBar.setOnSeekBarChangeListener(listener);

        SeekBar distanceBar = (SeekBar)findViewById(R.id.distanceBar);
        distanceBar.setMax(dofCalculator.getDistanceBarMax());
        distanceBar.setProgress(dofCalculator.getDistanceBarProgress());
        distanceBar.setOnSeekBarChangeListener(listener);

        Spinner.OnItemSelectedListener spinnerListener = new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.activity.onDataChanged();
            }
            public void onNothingSelected(AdapterView<?> parent) {
                MainActivity.activity.onDataChanged();
            }
        };

        Spinner sensorSizeSpinner = (Spinner)findViewById(R.id.sensorSizeSpinner);
        ArrayAdapter<CharSequence> sensorSizeAdapter = ArrayAdapter.createFromResource(this,
                R.array.sensers_array, android.R.layout.simple_spinner_item);
        sensorSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sensorSizeSpinner.setAdapter(sensorSizeAdapter);
        sensorSizeSpinner.setSelection(dofCalculator.getCircleOfConfusionIndex());
        sensorSizeSpinner.setOnItemSelectedListener(spinnerListener);

        Spinner distanceUnitSpinner = (Spinner)findViewById(R.id.distanceUnitSpinner);
        ArrayAdapter<CharSequence> distanceUnitAdapter = ArrayAdapter.createFromResource(this,
                R.array.distance_size_array, android.R.layout.simple_spinner_item);
        distanceUnitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distanceUnitSpinner.setAdapter(distanceUnitAdapter);
        distanceUnitSpinner.setSelection(dofCalculator.getDistanceUnitIndex());
        distanceUnitSpinner.setOnItemSelectedListener(spinnerListener);

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
        Spinner sensorSizeSpinner = (Spinner)findViewById(R.id.sensorSizeSpinner);
        dofCalculator.setCircleOfConfusionIndex(sensorSizeSpinner.getSelectedItemPosition());

        Spinner distanceUnitSpinner = (Spinner)findViewById(R.id.distanceUnitSpinner);
        dofCalculator.setDistanceUnitIndex(distanceUnitSpinner.getSelectedItemPosition());

        SeekBar focalBar = (SeekBar)findViewById(R.id.focalBar);
        TextView focalText = (TextView)findViewById(R.id.forcalText);
        dofCalculator.setFocalBarProgress(focalBar.getProgress());
        focalText.setText(dofCalculator.getCurFocalText());

        SeekBar apertureBar = (SeekBar)findViewById(R.id.apertureBar);
        TextView apertureText = (TextView)findViewById(R.id.apertureText);
        dofCalculator.setApertureBarProgress(apertureBar.getProgress());
        apertureText.setText(dofCalculator.getCurApertureText());

        SeekBar distanceBar = (SeekBar)findViewById(R.id.distanceBar);
        TextView distanceText = (TextView)findViewById(R.id.distanceText);
        dofCalculator.setDistanceBarProgress(distanceBar.getProgress());
        distanceText.setText(dofCalculator.getCurDistanceText());

        TextView dofText = (TextView)findViewById(R.id.dofText);
        dofText.setText(dofCalculator.getDepthOfFieldText());
        TextView nearDofText = (TextView)findViewById(R.id.nearDofText);
        nearDofText.setText(dofCalculator.getNearDepthOfFieldText());
        TextView farDofText = (TextView)findViewById(R.id.farDofText);
        farDofText.setText(dofCalculator.getFarDepthOfFieldText());
    }
}
