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
import android.os.AsyncTask;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import florian.felix.flesch.fpvvideochannelsorter.sorterlogic.Band;
import florian.felix.flesch.fpvvideochannelsorter.sorterlogic.Frequency;
import florian.felix.flesch.fpvvideochannelsorter.sorterlogic.Sorter;

public class RVPilotAdapter extends RecyclerView.Adapter<RVPilotAdapter.PilotViewHolder>
{
    static ArrayList<Pilot> data;
    TextView tvMinDif;
    TextView tvMaxDif;
    TextView tvIMD;
	ProgressBar pbSorter;
	Calc currentClacThread;
	private int minFrequency;
	private int maxFrequency;
    private boolean considerIMD;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class PilotViewHolder extends RecyclerView.ViewHolder {
        CardView cv;

        TextView name;
        CheckBox cbA;
        CheckBox cbB;
        CheckBox cbE;
        CheckBox cbF;
        CheckBox cbR;
        CheckBox cbD;
        Switch swFixed;
        Spinner spChannel;
        TextView tvspChannel;
        TextView tvBand;
        TextView tvChannel;
        TextView tvFrequency;

        public PilotViewHolder(View itemView){
            super(itemView);
            this.cv = (CardView)itemView.findViewById(R.id.cvPilot);
            this.name = (TextView)itemView.findViewById(R.id.tvPilotnumber);
            this.cbA = (CheckBox)itemView.findViewById(R.id.cbA);
            this.cbB = (CheckBox)itemView.findViewById(R.id.cbB);
            this.cbE = (CheckBox)itemView.findViewById(R.id.cbE);
            this.cbF = (CheckBox)itemView.findViewById(R.id.cbF);
            this.cbR = (CheckBox)itemView.findViewById(R.id.cbR);
            this.cbD = (CheckBox)itemView.findViewById(R.id.cbD);
            this.swFixed = (Switch)itemView.findViewById(R.id.sw_pilot_fixed);
            this.spChannel = (Spinner)itemView.findViewById(R.id.sp_pilot_channel);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(itemView.getContext(), R.array.channels_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spChannel.setAdapter(adapter);
            this.tvspChannel = (TextView)itemView.findViewById(R.id.tv_pilot_channel);
            this.tvBand = (TextView)itemView.findViewById(R.id.tvBand);
            this.tvChannel = (TextView)itemView.findViewById(R.id.tvChannel);
            this.tvFrequency = (TextView)itemView.findViewById(R.id.tvFrequency);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RVPilotAdapter(ArrayList<Pilot> pilots, int minFrequency, int maxFrequency, boolean considerIMD, TextView tvMinDif, TextView tvMaxDif, TextView tvIMD, ProgressBar pbSorter)
    {
        this.data = pilots;
		this.minFrequency = minFrequency;
		this.maxFrequency = maxFrequency;
        this.considerIMD = considerIMD;
        this.tvMinDif = tvMinDif;
        this.tvMaxDif = tvMaxDif;
        this.tvIMD = tvIMD;
		this.pbSorter = pbSorter;
    }

    @Override
    public RVPilotAdapter.PilotViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_pilot, viewGroup, false);
        PilotViewHolder pilotViewHolder = new PilotViewHolder(v);
        return pilotViewHolder;
    }

    @Override
    public void onBindViewHolder(final PilotViewHolder holder, final int position) {
        holder.name.setText(this.data.get(position).getName());
        holder.cbA.setChecked(this.data.get(position).getBandA());
        holder.cbB.setChecked(this.data.get(position).getBandB());
        holder.cbE.setChecked(this.data.get(position).getBandE());
        holder.cbF.setChecked(this.data.get(position).getBandF());
        holder.cbR.setChecked(this.data.get(position).getBandR());
        holder.cbD.setChecked(this.data.get(position).getBandD());
        holder.swFixed.setChecked(this.data.get(position).isFixed());
        holder.spChannel.setSelection(this.data.get(position).getFixedChannel()-1);
        if(this.data.get(position).isFixed()) {
            holder.tvspChannel.setVisibility(View.VISIBLE);
            holder.spChannel.setVisibility(View.VISIBLE);
        }
        else {
            holder.tvspChannel.setVisibility(View.GONE);
            holder.spChannel.setVisibility(View.GONE);
        }

		System.out.println(this.data.get(position).getFrequency());
		if(this.data.get(position).getFrequency() == null) {
			holder.tvBand.setText("-");
			holder.tvChannel.setText("-");
			holder.tvFrequency.setText("-");
		}
		else {
			holder.tvBand.setText(this.data.get(position).getFrequency().getBandString());
			holder.tvChannel.setText(this.data.get(position).getFrequency().getChannel() + "");
			holder.tvFrequency.setText(this.data.get(position).getFrequency().getFrequenz() + "");
		}

        final CheckBox fcbA = holder.cbA;
        final CheckBox fcbB = holder.cbB;
        final CheckBox fcbE = holder.cbE;
        final CheckBox fcbF = holder.cbF;
        final CheckBox fcbR = holder.cbR;
        final CheckBox fcbD = holder.cbD;

        holder.spChannel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int spPosition, long id) {
                data.get(position).setFixedChannel(spPosition+1);
                sortChannels();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do nothing
            }
        });

        holder.cbA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.get(position).setBandA(fcbA.isChecked());
                if(data.get(position).isFixed() && fcbA.isChecked()) { //Disable all other bands in fixed mode
                    data.get(position).setBandB(false);
                    data.get(position).setBandE(false);
                    data.get(position).setBandF(false);
                    data.get(position).setBandR(false);
                    data.get(position).setBandD(false);
                }
                sortChannels();
            }
        });

        holder.cbB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.get(position).setBandB(fcbB.isChecked());
                if(data.get(position).isFixed() && fcbB.isChecked()) { //Disable all other bands in fixed mode
                    data.get(position).setBandA(false);
                    data.get(position).setBandE(false);
                    data.get(position).setBandF(false);
                    data.get(position).setBandR(false);
                    data.get(position).setBandD(false);
                }
                sortChannels();
            }
        });

        holder.cbE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.get(position).setBandE(fcbE.isChecked());
                if(data.get(position).isFixed() && fcbE.isChecked()) { //Disable all other bands in fixed mode
                    data.get(position).setBandA(false);
                    data.get(position).setBandB(false);
                    data.get(position).setBandF(false);
                    data.get(position).setBandR(false);
                    data.get(position).setBandD(false);
                }
                sortChannels();
            }
        });

        holder.cbF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.get(position).setBandF(fcbF.isChecked());
                if(data.get(position).isFixed() && fcbF.isChecked()) { //Disable all other bands in fixed mode
                    data.get(position).setBandA(false);
                    data.get(position).setBandB(false);
                    data.get(position).setBandE(false);
                    data.get(position).setBandR(false);
                    data.get(position).setBandD(false);
                }
                sortChannels();
            }
        });

        holder.cbR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.get(position).setBandR(fcbR.isChecked());
                if(data.get(position).isFixed() && fcbR.isChecked()) { //Disable all other bands in fixed mode
                    data.get(position).setBandA(false);
                    data.get(position).setBandB(false);
                    data.get(position).setBandE(false);
                    data.get(position).setBandF(false);
                    data.get(position).setBandD(false);
                }
                sortChannels();
            }
        });

        holder.cbD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.get(position).setBandD(fcbD.isChecked());
                if(data.get(position).isFixed() && fcbD.isChecked()) { //Disable all other bands in fixed mode
                    data.get(position).setBandA(false);
                    data.get(position).setBandB(false);
                    data.get(position).setBandE(false);
                    data.get(position).setBandF(false);
                    data.get(position).setBandR(false);
                }
                sortChannels();
            }
        });

        holder.swFixed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.get(position).setFixed(!data.get(position).isFixed());
                if(data.get(position).isFixed()) {
                    Frequency freq = data.get(position).getFrequency();
                    data.get(position).setBandA(freq == null || Band.BAND_A.equals(freq.getBand()));
                    data.get(position).setBandB(freq != null && Band.BAND_B.equals(freq.getBand()));
                    data.get(position).setBandE(freq != null && Band.BAND_E.equals(freq.getBand()));
                    data.get(position).setBandF(freq != null && Band.BAND_F.equals(freq.getBand()));
                    data.get(position).setBandR(freq != null && Band.BAND_R.equals(freq.getBand()));
                    data.get(position).setBandD(freq != null && Band.BAND_L.equals(freq.getBand()));
                    data.get(position).setFixedChannel(freq != null ? freq.getChannel() : 1);
                }

                sortChannels();
            }
        });

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] name = new String[1];

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle(R.string.change_pilot_name);

                final EditText input = new EditText(v.getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setText(holder.name.getText());
                builder.setView(input);

                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        name[0] = input.getText().toString();
                        holder.name.setText(name[0]);
                        data.get(position).setName(name[0]);
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
        });
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public ArrayList<Pilot> getData() {
		return data;
	}

	public void setData(ArrayList<Pilot> data, int minFrequency, int maxFrequency, boolean considerIMD) {
		RVPilotAdapter.data = data;
		this.minFrequency = minFrequency;
		this.maxFrequency = maxFrequency;
        this.considerIMD = considerIMD;
		sortChannels();
	}

    public void sortChannels() {
		notifyDataSetChanged();
        boolean allPilotsSet = true;
        for(int i=0; i<data.size(); i++)
        {
            data.get(i).setNumber(i); //renumber the pilots in correct order
            if(!data.get(i).isSet())
            {
                allPilotsSet = false;
            }
        }

        if(allPilotsSet && data.size() > 1)
        {
			this.pbSorter.setVisibility(View.VISIBLE);
			//resetChannels(); flickering with fast calculations but the old values stay without it

			if(this.currentClacThread != null) {
				this.currentClacThread.cancel(true);
				while(!this.currentClacThread.isCancelled()){}
			}
			ArrayList<Pilot> pilotCopy = new ArrayList<>();
			for(int i=0; i<data.size(); i++) {
				pilotCopy.add(data.get(i).getCopy());
			}

			this.currentClacThread = new Calc(pilotCopy, this.minFrequency, this.maxFrequency, this.considerIMD, this);
			this.currentClacThread.execute();
        }
        else
        {
			this.pbSorter.setVisibility(View.INVISIBLE);
            resetChannels();
        }
    }

    public void setFrequencyRange(int min, int max, boolean considerIMD) {
		this.minFrequency = min;
		this.maxFrequency = max;
        this.considerIMD = considerIMD;
		sortChannels();
    }

    public int getMinFrequency() {
		return this.minFrequency;
	}

	public int getMaxFrequency() {
		return this.maxFrequency;
	}

    public boolean isConsiderIMD() {
        return considerIMD;
    }

    protected void calculationFinished(ArrayList<Pilot> calculatedData, boolean error) {
		this.currentClacThread = null;
		this.pbSorter.setVisibility(View.INVISIBLE);

		if(!error) {
			data = calculatedData;
			notifyDataSetChanged();

            int[] minDisVector = Sorter.getMinDisVector(data);
			int maxDis = Sorter.getMaxDis(data);
			this.tvMinDif.setText(minDisVector[0] + " mHz");
			this.tvMaxDif.setText(maxDis + " mHz");
            this.tvIMD.setText(minDisVector[1] + " mHz / " + minDisVector[2]);
		} else {
			MainActivity.showSnackbarMessage(R.string.no_values_with_given_frequency_range);
			resetChannels();
		}
	}

    private void resetChannels()
    {
        for(int i=0; i<this.data.size(); i++)
        {
            data.get(i).setFrequency(null);
        }

        this.tvMinDif.setText("- mHz");
        this.tvMaxDif.setText("- mHz");
        this.tvIMD.setText("- mHz / --");
        notifyDataSetChanged();
    }
}

class Calc extends AsyncTask<Void, Void, Void> {

	ArrayList<Pilot> data;
	RVPilotAdapter rvPilotAdapter;
	int minFrequency;
	int maxFrequency;
    boolean considerIMD;
	boolean error = false;

	public Calc(ArrayList<Pilot> data, int minFrequency, int maxFrequency, boolean considerIMD, RVPilotAdapter rvPilotAdapter) {
		this.data = data;
		this.minFrequency = minFrequency;
		this.maxFrequency = maxFrequency;
        this.considerIMD = considerIMD;
		this.rvPilotAdapter = rvPilotAdapter;
	}

	@Override
	protected Void doInBackground(Void... params) {

		try {
			this.data = Sorter.sort(this.data, this.considerIMD);
		} catch (Exception e) {
			error = true;
		}
		return null;
	}

	protected void onPostExecute(Void result) {
		this.rvPilotAdapter.calculationFinished(data, error);
	}
}