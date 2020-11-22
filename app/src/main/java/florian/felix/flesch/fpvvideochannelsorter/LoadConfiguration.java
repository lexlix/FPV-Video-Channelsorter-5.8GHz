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
import androidx.appcompat.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;


public class LoadConfiguration extends AppCompatActivity {

	private SaveState saveState;
	private ListView lvConfigs;
	private RecyclerView.LayoutManager layoutManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load_configuration);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		this.layoutManager = new LinearLayoutManager(this);

		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		Gson gson = new Gson();
		String json = mPrefs.getString("save", "");
		this.saveState = gson.fromJson(json, SaveState.class);

		this.lvConfigs = (ListView)findViewById(R.id.lvConfigurations);
		loadConfigs();

		lvConfigs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				MainActivity.setConfiguration(saveState.getConfigurations().get(position));
				onBackPressed();
			}
		});

		lvConfigs.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
				AlertDialog.Builder builder = new AlertDialog.Builder(LoadConfiguration.this);
				builder.setTitle(R.string.delete_config);

				builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						saveState.removeConfiguration(saveState.getConfigurations().get(position));

						SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
						SharedPreferences.Editor prefsEditor = mPrefs.edit();
						Gson gson = new Gson();

						String json = gson.toJson(saveState);
						prefsEditor.putString("save", json);
						prefsEditor.apply();

						Toast.makeText(getApplicationContext(), getString(R.string.config_deleted), Toast.LENGTH_SHORT).show();

						loadConfigs();
					}
				});
				builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

				builder.show();

				return true;
			}
		});
	}

	private void loadConfigs() {
		String[] values = new String[this.saveState.getConfigurations().size()];

		if(values.length == 0) {
			Toast.makeText(getApplicationContext(), getString(R.string.no_config), Toast.LENGTH_SHORT).show();
		}

		for(int i=0; i<this.saveState.getConfigurations().size(); i++) {
			values[i] = this.saveState.getConfigurations().get(i).getSaveName();
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, values);
		this.lvConfigs.setAdapter(adapter);
	}
}
