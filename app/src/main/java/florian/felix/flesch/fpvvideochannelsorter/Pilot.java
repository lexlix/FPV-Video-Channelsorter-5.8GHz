package florian.felix.flesch.fpvvideochannelsorter;

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

import java.util.ArrayList;

import florian.felix.flesch.fpvvideochannelsorter.sorterlogic.Band;
import florian.felix.flesch.fpvvideochannelsorter.sorterlogic.Frequency;


public class Pilot
{
    private int number;
    private String name;
    private boolean fixed;
    private int fixedChannel = 1;
    private boolean bandA;
    private boolean bandB;
    private boolean bandE;
    private boolean bandF;
    private boolean bandR;
    private boolean bandD;
    private Frequency frequency = null;
    ArrayList<Frequency> availableFrequencies;
    private boolean settingsChanged;
    int minFreq;
    int maxFreq;

    /**
     *
     * @param number number of the pilot in the recyclerview order. Starting with 0
     * @param name
     * @param bandA
     * @param bandB
     * @param bandE
     * @param bandF
     * @param bandR
     * @param bandD
     */
    public Pilot(int number, String name, boolean bandA, boolean bandB, boolean bandE, boolean bandF, boolean bandR, boolean bandD, int minFreq, int maxFreq)
    {
        this.number = number;
        this.name = name;
        this.bandA = bandA;
        this.bandB = bandB;
        this.bandE = bandE;
        this.bandF = bandF;
        this.bandR = bandR;
        this.bandD = bandD;
        this.fixed = false;
        this.minFreq = minFreq;
        this.maxFreq = maxFreq;

        this.settingsChanged = true;
    }

    public int getNumber() {
        return this.number;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getMinFreq() {
        return this.minFreq;
    }

    public int getMaxFreq() {
        return this.maxFreq;
    }

    public boolean getBandA() {
        return this.bandA;
    }

    public boolean getBandB() {
        return this.bandB;
    }

    public boolean getBandE() {
        return this.bandE;
    }

    public boolean getBandF() {
        return this.bandF;
    }

    public boolean getBandR() {
        return this.bandR;
    }

    public boolean getBandD() {
        return this.bandD;
    }

    public boolean isFixed() {
        return this.fixed;
    }

    public int getFixedChannel() {
        return this.fixedChannel;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setBandA(boolean bandA)
    {
        if (this.bandA != bandA) {
            this.settingsChanged = true;
        }
        this.bandA = bandA;
    }

    public void setBandB(boolean bandB)
    {
        if (this.bandB != bandB) {
            this.settingsChanged = true;
        }
        this.bandB = bandB;
    }

    public void setBandE(boolean bandE)
    {
        if (this.bandE != bandE) {
            this.settingsChanged = true;
        }
        this.bandE = bandE;
    }

    public void setBandF(boolean bandF)
    {
        if (this.bandF != bandF) {
            this.settingsChanged = true;
        }
        this.bandF = bandF;
    }

    public void setBandR(boolean bandR)
    {
        if (this.bandR != bandR) {
            this.settingsChanged = true;
        }
        this.bandR = bandR;
    }

    public void setBandD(boolean bandD) {
        if (this.bandD != bandD) {
            this.settingsChanged = true;
        }
        this.bandD = bandD;
    }

    public void setFixed(boolean fixed) {
        if (this.fixed != fixed) {
            this.settingsChanged = true;
        }
        this.fixed = fixed;
    }

    /**
     *
     * @param channel value from 1 to 8
     */
    public void setFixedChannel(int channel) {
        this.fixedChannel = channel;
    }

    public boolean isSet() {
        return this.bandA || this.bandB || this.bandE || this.bandF || this.bandR || this.bandD;
    }



    /**
     * Calculate the available frequencies only when they are changed to save time
     */
    private void calculateAvailableFrequencies() {
        ArrayList<Frequency> frequencies = new ArrayList();

        if(this.isSet()) {
            if(this.fixed) {
                if(this.bandA) {
                    frequencies.add(new Frequency(Band.BAND_A, this.fixedChannel));
                }
                if(this.bandB) {
                    frequencies.add(new Frequency(Band.BAND_B, this.fixedChannel));
                }
                if(this.bandE) {
                    frequencies.add(new Frequency(Band.BAND_E, this.fixedChannel));
                }
                if(this.bandF) {
                    frequencies.add(new Frequency(Band.BAND_F, this.fixedChannel));
                }
                if(this.bandR) {
                    frequencies.add(new Frequency(Band.BAND_R, this.fixedChannel));
                }
                if(this.bandD) {
                    frequencies.add(new Frequency(Band.BAND_L, this.fixedChannel));
                }
            }
            else {
                if(this.bandA) {
                    for(int i=1; i<9; i++) {
                        if(Frequency.BandA[i-1] >= this.minFreq && Frequency.BandA[i-1] <= this.maxFreq) {
                            frequencies.add(new Frequency(Band.BAND_A, i));
                        }
                    }
                }
                if(this.bandB) {
                    for(int i=1; i<9; i++) {
                        if(Frequency.BandB[i-1] >= this.minFreq && Frequency.BandB[i-1] <= this.maxFreq) {
                            frequencies.add(new Frequency(Band.BAND_B, i));
                        }
                    }
                }
                if(this.bandE) {
                    for(int i=1; i<9; i++) {
                        if(Frequency.BandE[i-1] >= this.minFreq && Frequency.BandE[i-1] <= this.maxFreq) {
                            frequencies.add(new Frequency(Band.BAND_E, i));
                        }
                    }
                }
                if(this.bandF) {
                    for(int i=1; i<9; i++) {
                        if(Frequency.BandF[i-1] >= this.minFreq && Frequency.BandF[i-1] <= this.maxFreq) {
                            frequencies.add(new Frequency(Band.BAND_F, i));
                        }
                    }
                }
                if(this.bandR) {
                    for(int i=1; i<9; i++) {
                        if(Frequency.BandR[i-1] >= this.minFreq && Frequency.BandR[i-1] <= this.maxFreq) {
                            frequencies.add(new Frequency(Band.BAND_R, i));
                        }
                    }
                }
                if(this.bandD) {
                    for(int i=1; i<9; i++) {
                        if(Frequency.BandL[i-1] >= this.minFreq && Frequency.BandL[i-1] <= this.maxFreq) {
                            frequencies.add(new Frequency(Band.BAND_L, i));
                        }
                    }
                }
            }
        }

        this.availableFrequencies = frequencies;
    }

    /**
     *
     * @return a list of the available frequencies for this pilot
     */
    public ArrayList<Frequency> getAvailableFrequencies() {
        if (this.settingsChanged) {
            this.calculateAvailableFrequencies();
            this.settingsChanged = false;
        }
        return this.availableFrequencies;
    }

    public Frequency getFrequency() {
        return this.frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public Pilot getCopy() {
        Pilot p = new Pilot(this.number, this.name, this.bandA, this.bandB, this.bandE, this.bandF, this.bandR, this.bandD, this.minFreq, this.maxFreq);

        p.fixed = this.fixed;
        p.fixedChannel = this.fixedChannel;
        p.frequency = this.frequency;

        return p;
    }
}