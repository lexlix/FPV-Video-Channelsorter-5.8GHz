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

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.TextView;

import florian.felix.flesch.fpvvideochannelsorter.sorterlogic.Frequency;

public class FrequencyOverview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frequency_overview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView tvA = (TextView) findViewById(R.id.tv_overview_values_a);
        TextView tvB = (TextView) findViewById(R.id.tv_overview_values_b);
        TextView tvE = (TextView) findViewById(R.id.tv_overview_values_e);
        TextView tvF = (TextView) findViewById(R.id.tv_overview_values_f);
        TextView tvR = (TextView) findViewById(R.id.tv_overview_values_r);
        TextView tvD = (TextView) findViewById(R.id.tv_overview_values_d);

        String channels = "";
        for(int i=0; i<8; i++) {
            channels += Frequency.BandA[i];
            if(i != 7) {
                channels += " - ";
            }
        }
        tvA.setText(channels);

        channels = "";
        for(int i=0; i<8; i++) {
            channels += Frequency.BandB[i];
            if(i != 7) {
                channels += " - ";
            }
        }
        tvB.setText(channels);

        channels = "";
        for(int i=0; i<8; i++) {
            channels += Frequency.BandE[i];
            if(i != 7) {
                channels += " - ";
            }
        }
        tvE.setText(channels);

        channels = "";
        for(int i=0; i<8; i++) {
            channels += Frequency.BandF[i];
            if(i != 7) {
                channels += " - ";
            }
        }
        tvF.setText(channels);

        channels = "";
        for(int i=0; i<8; i++) {
            channels += Frequency.BandR[i];
            if(i != 7) {
                channels += " - ";
            }
        }
        tvR.setText(channels);

        channels = "";
        for(int i=0; i<8; i++) {
            channels += Frequency.BandL[i];
            if(i != 7) {
                channels += " - ";
            }
        }
        tvD.setText(channels);
    }

}
