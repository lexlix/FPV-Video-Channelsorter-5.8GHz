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
import java.util.List;

import florian.felix.flesch.fpvvideochannelsorter.sorterlogic.Band;
import florian.felix.flesch.fpvvideochannelsorter.sorterlogic.Frequency;
import florian.felix.flesch.fpvvideochannelsorter.sorterlogic.Sorter;

public class RVPilotAdapter extends RecyclerView.Adapter<RVPilotAdapter.PilotViewHolder>
{
    static List<Pilot> data;
    TextView tvMinDif;
    TextView tvMaxDif;
    TextView tvIMD;
	ProgressBar pbSorter;
	Calc currentCalcThread;
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
        CheckBox cbDJI;
        Switch swFixed;
        Spinner spChannel7;
        Spinner spChannel8;
        TextView tvspChannel7;
        TextView tvspChannel8;
        TextView tvBand;
        TextView tvChannel;
        TextView tvFrequency;

        public PilotViewHolder(final View itemView){
            super(itemView);
            this.cv = itemView.findViewById(R.id.cvPilot);
            this.name = itemView.findViewById(R.id.tvPilotnumber);
            this.cbA = itemView.findViewById(R.id.cbA);
            this.cbB = itemView.findViewById(R.id.cbB);
            this.cbE = itemView.findViewById(R.id.cbE);
            this.cbF = itemView.findViewById(R.id.cbF);
            this.cbR = itemView.findViewById(R.id.cbR);
            this.cbD = itemView.findViewById(R.id.cbD);
            this.cbDJI = itemView.findViewById(R.id.cbDJI);
            this.swFixed = itemView.findViewById(R.id.sw_pilot_fixed);
            this.spChannel7 = itemView.findViewById(R.id.sp_pilot_channel_7);
            ArrayAdapter<CharSequence> adapter7 = ArrayAdapter.createFromResource(itemView.getContext(), R.array.channels_array_7, android.R.layout.simple_spinner_item);
            adapter7.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spChannel7.setAdapter(adapter7);
            this.spChannel8 = itemView.findViewById(R.id.sp_pilot_channel_8);
            ArrayAdapter<CharSequence> adapter8 = ArrayAdapter.createFromResource(itemView.getContext(), R.array.channels_array_8, android.R.layout.simple_spinner_item);
            adapter8.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spChannel8.setAdapter(adapter8);
            this.tvspChannel7 = itemView.findViewById(R.id.tv_pilot_channel_7);
            this.tvspChannel8 = itemView.findViewById(R.id.tv_pilot_channel_8);
            this.tvBand = itemView.findViewById(R.id.tvBand);
            this.tvChannel = itemView.findViewById(R.id.tvChannel);
            this.tvFrequency = itemView.findViewById(R.id.tvFrequency);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RVPilotAdapter(List<Pilot> pilots, int minFrequency, int maxFrequency, boolean considerIMD, TextView tvMinDif, TextView tvMaxDif, TextView tvIMD, ProgressBar pbSorter)
    {
        data = pilots;
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
        Pilot p = data.get(position);

        holder.name.setText(p.getName());
        holder.cbA.setChecked(p.getBandA());
        holder.cbB.setChecked(p.getBandB());
        holder.cbE.setChecked(p.getBandE());
        holder.cbF.setChecked(p.getBandF());
        holder.cbR.setChecked(p.getBandR());
        holder.cbD.setChecked(p.getBandD());
        holder.cbDJI.setChecked(p.getBandDJI());
        holder.swFixed.setChecked(p.isFixed());
        if (p.isFixed() && p.getBandDJI() && p.getFixedChannel() == 8) p.setFixedChannel(7);
        holder.spChannel7.setSelection(p.getFixedChannel()-1); // check fixedChannel is not 8 when switching to DJI
        holder.spChannel8.setSelection(p.getFixedChannel()-1);
        if(!p.isFixed()) {
            holder.tvspChannel7.setVisibility(View.GONE);
            holder.tvspChannel8.setVisibility(View.GONE);
            holder.spChannel7.setVisibility(View.GONE);
            holder.spChannel8.setVisibility(View.GONE);
        }
        else if(p.getBandDJI()) {
            holder.tvspChannel7.setVisibility(View.VISIBLE);
            holder.tvspChannel8.setVisibility(View.GONE);
            holder.spChannel7.setVisibility(View.VISIBLE);
            holder.spChannel8.setVisibility(View.GONE);
        } else { // Not DJI
            holder.tvspChannel7.setVisibility(View.GONE);
            holder.tvspChannel8.setVisibility(View.VISIBLE);
            holder.spChannel7.setVisibility(View.GONE);
            holder.spChannel8.setVisibility(View.VISIBLE);
        }

		if(p.getFrequency() == null) {
			holder.tvBand.setText("-");
			holder.tvChannel.setText("-");
			holder.tvFrequency.setText("-");
		}
		else {
			holder.tvBand.setText(p.getFrequency().getBandString());
			holder.tvChannel.setText(p.getFrequency().getChannel() + "");
			holder.tvFrequency.setText(p.getFrequency().getFrequenz() + "");
		}

        final CheckBox fcbA = holder.cbA;
        final CheckBox fcbB = holder.cbB;
        final CheckBox fcbE = holder.cbE;
        final CheckBox fcbF = holder.cbF;
        final CheckBox fcbR = holder.cbR;
        final CheckBox fcbD = holder.cbD;
        final CheckBox fcbDji = holder.cbDJI;

        holder.spChannel7.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        holder.spChannel8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        holder.cbA.setOnClickListener(v -> {
            p.setBandA(fcbA.isChecked());
            if(p.isFixed() && fcbA.isChecked()) { //Disable all other bands in fixed mode
                p.setBandB(false);
                p.setBandE(false);
                p.setBandF(false);
                p.setBandR(false);
                p.setBandD(false);
                p.setBandDJI(false);
            }
            sortChannels();
        });

        holder.cbB.setOnClickListener(v -> {
            p.setBandB(fcbB.isChecked());
            if(p.isFixed() && fcbB.isChecked()) { //Disable all other bands in fixed mode
                p.setBandA(false);
                p.setBandE(false);
                p.setBandF(false);
                p.setBandR(false);
                p.setBandD(false);
                p.setBandDJI(false);
            }
            sortChannels();
        });

        holder.cbE.setOnClickListener(v -> {
            p.setBandE(fcbE.isChecked());
            if(p.isFixed() && fcbE.isChecked()) { //Disable all other bands in fixed mode
                p.setBandA(false);
                p.setBandB(false);
                p.setBandF(false);
                p.setBandR(false);
                p.setBandD(false);
                p.setBandDJI(false);
            }
            sortChannels();
        });

        holder.cbF.setOnClickListener(v -> {
            p.setBandF(fcbF.isChecked());
            if(p.isFixed() && fcbF.isChecked()) { //Disable all other bands in fixed mode
                p.setBandA(false);
                p.setBandB(false);
                p.setBandE(false);
                p.setBandR(false);
                p.setBandD(false);
                p.setBandDJI(false);
            }
            sortChannels();
        });

        holder.cbR.setOnClickListener(v -> {
            p.setBandR(fcbR.isChecked());
            if(p.isFixed() && fcbR.isChecked()) { //Disable all other bands in fixed mode
                p.setBandA(false);
                p.setBandB(false);
                p.setBandE(false);
                p.setBandF(false);
                p.setBandD(false);
                p.setBandDJI(false);
            }
            sortChannels();
        });

        holder.cbD.setOnClickListener(v -> {
            p.setBandD(fcbD.isChecked());
            if(p.isFixed() && fcbD.isChecked()) { //Disable all other bands in fixed mode
                p.setBandA(false);
                p.setBandB(false);
                p.setBandE(false);
                p.setBandF(false);
                p.setBandR(false);
                p.setBandDJI(false);
            }
            sortChannels();
        }
        );
        holder.cbDJI.setOnClickListener(v -> {
            p.setBandDJI(fcbDji.isChecked());
            holder.spChannel7.setSelection(p.getFixedChannel()-1);
            if (p.isFixed() && fcbDji.isChecked()) { //Disable all other bands in fixed mode
                p.setBandA(false);
                p.setBandB(false);
                p.setBandE(false);
                p.setBandF(false);
                p.setBandR(false);
                p.setBandD(false);
            }
            sortChannels();
        }
        );

        holder.swFixed.setOnClickListener(v -> {
            p.setFixed(!p.isFixed());
            if(p.isFixed()) {
                Frequency freq = p.getFrequency();
                p.setBandA(freq == null || Band.BAND_A.equals(freq.getBand()));
                p.setBandB(freq != null && Band.BAND_B.equals(freq.getBand()));
                p.setBandE(freq != null && Band.BAND_E.equals(freq.getBand()));
                p.setBandF(freq != null && Band.BAND_F.equals(freq.getBand()));
                p.setBandR(freq != null && Band.BAND_R.equals(freq.getBand()));
                p.setBandD(freq != null && Band.BAND_L.equals(freq.getBand()));
                p.setBandDJI(freq != null && Band.BAND_DJI.equals(freq.getBand()));
                p.setFixedChannel(freq != null ? freq.getChannel() : 1);
            }

            sortChannels();
        });

        holder.name.setOnClickListener(v -> {
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
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public List<Pilot> getData() {
		return data;
	}

	public void setData(List<Pilot> data, int minFrequency, int maxFrequency, boolean considerIMD) {
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

			if(this.currentCalcThread != null) {
				this.currentCalcThread.cancel(true);
				while(!this.currentCalcThread.isCancelled()){}
			}
			ArrayList<Pilot> pilotCopy = new ArrayList<>();
			for(int i=0; i<data.size(); i++) {
				pilotCopy.add(data.get(i).getCopy());
			}

			this.currentCalcThread = new Calc(pilotCopy, this.minFrequency, this.maxFrequency, this.considerIMD, this);
			this.currentCalcThread.execute();
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

    protected void calculationFinished(List<Pilot> calculatedData, boolean error) {
		this.currentCalcThread = null;
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
        for(int i = 0; i< data.size(); i++)
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

	List<Pilot> data;
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