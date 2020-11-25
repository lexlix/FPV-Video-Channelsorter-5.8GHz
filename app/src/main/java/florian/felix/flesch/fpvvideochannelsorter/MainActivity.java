/**
 * MIT License
 *
 * Copyright (c) 2017 Felix-Florian Flesch
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package florian.felix.flesch.fpvvideochannelsorter;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener {

    private RecyclerView rvPilots;
    private RVPilotAdapter rvPilotAdapter;
    private static RecyclerView.LayoutManager layoutManager;
    private Button btnAddPilot;

    private ArrayList<Pilot> pilots;

	private static Configuration config = null;

	private NumberPicker npFrequencyRangeMin;
	private NumberPicker npFrequencyRangeMax;
	private Switch swConsiderIMD;

	@Override
	protected void onResume() {
		super.onResume();

		if(config != null) {
			this.rvPilotAdapter.setData(config.getPilots(), config.getMinFrequency(), config.getMaxFrequency(), config.isConsiderIMD());
			String message = getString(R.string.config_loaded) + " " + config.getSaveName();
			System.out.println(message);
			Snackbar.make(layoutManager.findViewByPosition(0), message, Snackbar.LENGTH_LONG).show();
			config = null;
		}
	}

	public static void showSnackbarMessage(int message) {
		Snackbar.make(layoutManager.findViewByPosition(0), message, Snackbar.LENGTH_SHORT).show();
	}

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		SharedPreferences.Editor prefsEditor = mPrefs.edit();
		Gson gson = new Gson();

		String json = mPrefs.getString("save", "");
		SaveState saveState = gson.fromJson(json, SaveState.class);

		if(saveState == null) {
			saveState = new SaveState();
			json = gson.toJson(saveState);
			prefsEditor.putString("save", json);
			prefsEditor.apply();
		}

        this.btnAddPilot = (Button) findViewById(R.id.btnAddPilot);
        this.btnAddPilot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pilots.size() == 7) {
                    btnAddPilot.setVisibility(View.GONE);
                }
                updatePilotCount(pilots.size() + 1);
            }
        });

        this.rvPilots = (RecyclerView) findViewById(R.id.rvPilots);
        this.rvPilots.setHasFixedSize(true); //improves performance

        this.layoutManager = new LinearLayoutManager(this);
        rvPilots.setLayoutManager(this.layoutManager);

        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(rvPilots,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipe(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    pilots = rvPilotAdapter.getData();
									pilots.remove(position);
                                    rvPilotAdapter.setData(pilots, rvPilotAdapter.getMinFrequency(), rvPilotAdapter.getMaxFrequency(), rvPilotAdapter.isConsiderIMD());
                                    rvPilotAdapter.notifyItemRemoved(position);
                                }
                                rvPilotAdapter.notifyDataSetChanged();
                                btnAddPilot.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
									pilots = rvPilotAdapter.getData();
                                    pilots.remove(position);
                                    rvPilotAdapter.setData(pilots, rvPilotAdapter.getMinFrequency(), rvPilotAdapter.getMaxFrequency(), rvPilotAdapter.isConsiderIMD());
                                    rvPilotAdapter.notifyItemRemoved(position);
                                }
                                rvPilotAdapter.notifyDataSetChanged();
                                btnAddPilot.setVisibility(View.VISIBLE);
                            }
                        });

        rvPilots.addOnItemTouchListener(swipeTouchListener);

        this.pilots = new ArrayList<>();
        Pilot pilot1 = new Pilot(0,"Pilot 1", false, false, false, false, false, false, 5362, 5945);
        Pilot pilot2 = new Pilot(1, "Pilot 2", false, false, false, false, false, false, 5362, 5945);
        this.pilots.add(pilot1);
        this.pilots.add(pilot2);

        this.rvPilotAdapter = new RVPilotAdapter(this.pilots, 5362, 5945, true, (TextView)findViewById(R.id.tvMinDifValue), (TextView)findViewById(R.id.tvMaxDifValue), (TextView)findViewById(R.id.tvIMDValue), (ProgressBar)findViewById(R.id.pbSorter));
        this.rvPilots.setAdapter(this.rvPilotAdapter);
    }

    private void updatePilotCount(int pilotCount) {
        if(this.pilots.size() < pilotCount)
        {
			this.pilots = rvPilotAdapter.getData();
            int oldPilotSize = this.pilots.size();
            for(int i=0; i<pilotCount-oldPilotSize; i++) {
                Pilot pilot = new Pilot(pilotCount-1, getNewPilotName(), false, false, false, false, false, false, rvPilotAdapter.getMinFrequency(), rvPilotAdapter.getMaxFrequency());
                this.pilots.add(pilot);
				this.rvPilotAdapter.setData(pilots, rvPilotAdapter.getMinFrequency(), rvPilotAdapter.getMaxFrequency(), rvPilotAdapter.isConsiderIMD());
                this.rvPilotAdapter.notifyDataSetChanged();
            }
            this.rvPilotAdapter.sortChannels();
            this.rvPilots.scrollToPosition(pilotCount-1);
        }
        else if(this.pilots.size() > pilotCount) {
            while(this.pilots.size() > pilotCount) {
                this.pilots.remove(this.pilots.size()-1);
				this.rvPilotAdapter.setData(pilots, rvPilotAdapter.getMinFrequency(), rvPilotAdapter.getMaxFrequency(), rvPilotAdapter.isConsiderIMD());
                this.rvPilotAdapter.notifyDataSetChanged();
            }
            this.rvPilotAdapter.sortChannels();
        }
        // else if same do nothing
    }

	private String getNewPilotName() {
		int counter = 1;
		String pilotName = "Pilot " + (this.pilots.size() + counter);

		boolean free = false;
		while(!free) {
			boolean taken = false;

			for (Pilot currentPilot : this.pilots) {
				if(currentPilot.getName().equals(pilotName)) {
					taken = true;
					break;
				}
			}

			if (taken) {
				counter++;
				pilotName = "Pilot " + (this.pilots.size() + counter);
			} else {
				free = true;
			}
		}

		return pilotName;
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
        if (id == R.id.action_frequency_overview) {
            Intent intent = new Intent(this, FrequencyOverview.class);
            startActivity(intent);
        }

        if (id == R.id.action_about) {
            Intent intent = new Intent(this, About.class);
            startActivity(intent);
        }

        if (id == R.id.action_save) {
			final SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

			final ArrayList<Pilot> data = this.rvPilotAdapter.getData();

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.set_name_for_save);

			final EditText input = new EditText(this);
			input.setInputType(InputType.TYPE_CLASS_TEXT);
			input.setText("");
			builder.setView(input);

			builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					final String name = input.getText().toString();
					if(name.equals("")) {
						Snackbar.make(layoutManager.findViewByPosition(0), getString(R.string.name_can_not_be_empty), Snackbar.LENGTH_LONG).show();
					}
					else {
						final SharedPreferences.Editor prefsEditor = mPrefs.edit();
						final Gson gson = new Gson();

						//load saveState
						String json = mPrefs.getString("save", "");
						final SaveState saveState = gson.fromJson(json, SaveState.class);

						Configuration config = null;
						for(int i=0; i<saveState.getConfigurations().size(); i++) {
							if(saveState.getConfigurations().get(i).getSaveName().equals(name)) {
								config = saveState.getConfigurations().get(i);
								break;
							}
						}

						final Configuration finalConfig = config;

						if(finalConfig != null) { //name is already used
							AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
							builder.setTitle(getString(R.string.name_exists));

							builder.setPositiveButton(getString(R.string.overwrite), new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									saveState.removeConfiguration(finalConfig);

									if(npFrequencyRangeMin == null || npFrequencyRangeMax == null) {
										saveState.addConfiguration(new Configuration(data, name));
									} else {
										saveState.addConfiguration(new Configuration(data, name, npFrequencyRangeMin.getValue(), npFrequencyRangeMax.getValue(), swConsiderIMD.isChecked()));
									}

									String json = gson.toJson(saveState);
									prefsEditor.putString("save", json);
									prefsEditor.apply();

									Snackbar.make(layoutManager.findViewByPosition(0), getString(R.string.config_saved), Snackbar.LENGTH_LONG).show();
								}
							});

							builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									//canceled do nothing
								}
							});

							builder.show();
						}
						else { //name is new
							if(npFrequencyRangeMin == null || npFrequencyRangeMax == null) {
								saveState.addConfiguration(new Configuration(data, name));
							} else {
								saveState.addConfiguration(new Configuration(data, name, npFrequencyRangeMin.getValue(), npFrequencyRangeMax.getValue(), swConsiderIMD.isChecked()));
							}

							json = gson.toJson(saveState);
							prefsEditor.putString("save", json);
							prefsEditor.apply();

							Snackbar.make(layoutManager.findViewByPosition(0), getString(R.string.config_saved), Snackbar.LENGTH_LONG).show();
						}
					}
				}
			});
			builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});

			builder.show();
        }

        if (id == R.id.action_saved) {
			Intent intent = new Intent(this, LoadConfiguration.class);
			startActivity(intent);
        }

        if (id == R.id.action_frequency_range) {

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setView(R.layout.dialog_frequency_range);
			builder.setTitle(R.string.frequency_range);
			builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					rvPilotAdapter.setFrequencyRange(npFrequencyRangeMin.getValue(), npFrequencyRangeMax.getValue(), swConsiderIMD.isChecked());
				}
			});
			builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					//Do nothing
				}
			});

			AlertDialog dialog = builder.create();

			dialog.show();

			npFrequencyRangeMin = (NumberPicker) dialog.findViewById(R.id.numberPickerMin);
			npFrequencyRangeMin.setMinValue(5362);
			npFrequencyRangeMin.setMaxValue(5945);
			npFrequencyRangeMin.setValue(rvPilotAdapter.getMinFrequency());
			npFrequencyRangeMin.setWrapSelectorWheel(false);
			npFrequencyRangeMin.setOnValueChangedListener(this);

			npFrequencyRangeMax = (NumberPicker) dialog.findViewById(R.id.numberPickerMax);
			npFrequencyRangeMax.setMinValue(5362);
			npFrequencyRangeMax.setMaxValue(5945);
			npFrequencyRangeMax.setValue(rvPilotAdapter.getMaxFrequency());
			npFrequencyRangeMax.setWrapSelectorWheel(false);
			npFrequencyRangeMax.setOnValueChangedListener(this);

			swConsiderIMD = (Switch) dialog.findViewById(R.id.switchIMD);
			swConsiderIMD.setChecked(rvPilotAdapter.isConsiderIMD());
		}

        if (id == R.id.action_help) {
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			builder.setTitle(R.string.help);
			builder.setMessage(getString(R.string.help_1) + "\n\n" + getString(R.string.help_2) + "\n\n" + getString(R.string.help_3) + "\n\n" + getString(R.string.help_4) + "\n\n" + getString(R.string.help_5) + "\n\n" + getString(R.string.help_6) + "\n\n" + getString(R.string.help_7));

			builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//do nothing
				}
			});

			builder.show();
		}

        return super.onOptionsItemSelected(item);
    }

    public static void setConfiguration(Configuration config) {
		MainActivity.config = config;
	}

	@Override
	public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
		if(picker.getId() == R.id.numberPickerMin) {
			if (npFrequencyRangeMax.getValue() <= newVal) {
				picker.setValue(oldVal);
			}
		}

		if(picker.getId() == R.id.numberPickerMax) {
			if (npFrequencyRangeMin.getValue() >= newVal) {
				picker.setValue(oldVal);
			}
		}
	}
}
