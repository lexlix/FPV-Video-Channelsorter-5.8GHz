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

package florian.felix.flesch.fpvvideochannelsorter.sorterlogic;

public class Frequency
{
    private int frequency;
    private Band band;
    private int channel;

    public static int[] BandA = {5865, 5845, 5825, 5805, 5785, 5765, 5745, 5725}; //Boscam
    public static int[] BandB = {5733, 5752, 5771, 5790, 5809, 5828, 5847, 5866};
    public static int[] BandE = {5705, 5685, 5665, 5645, 5885, 5905, 5925, 5945}; //Boscam, Hobbyking, Foxtech
    public static int[] BandF = {5740, 5760, 5780, 5800, 5820, 5840, 5860, 5880}; //FatShark, Immersion
    public static int[] BandR = {5658, 5695, 5732, 5769, 5806, 5843, 5880, 5917}; //RaceBand
    public static int[] BandDJI = {5660, 5695, 5735, 5770, 5805, 5878, 5914, 5839}; //DJI
    public static int[] BandL = {5362, 5399, 5436, 5473, 5510, 5547, 5584, 5621}; //LowBand

	/**
	 *
	 * @param band
	 * @param channel the channel of the set band from 1 to 8
	 */
    public Frequency(Band band, int channel)
    {
        if(band == Band.BAND_A) {
            this.frequency = BandA[channel -1];
        }
		else if(band == Band.BAND_B) {
			this.frequency = BandB[channel -1];
		}
		else if(band == Band.BAND_E) {
			this.frequency = BandE[channel -1];
		}
		else if(band == Band.BAND_F) {
			this.frequency = BandF[channel -1];
		}
		else if(band == Band.BAND_R) {
			this.frequency = BandR[channel -1];
		}
        else if(band == Band.BAND_DJI) {
            this.frequency = BandDJI[channel -1];
        }
		else if(band == Band.BAND_L) {
			this.frequency = BandL[channel -1];
		}
        this.band = band;
        this.channel = channel;
    }

    public int getFrequenz()
    {
        return this.frequency;
    }

    public int getChannel()
    {
        return this.channel;
    }

    public Band getBand()
    {
        return this.band;
    }

    public String getBandString() {
        if(this.band == Band.BAND_A) {
            return "Band A";
        }
        else if(this.band == Band.BAND_B) {
            return "Band B";
        }
        else if(this.band == Band.BAND_E) {
            return "Band E";
        }
        else if(this.band == Band.BAND_F) {
            return "Band F";
        }
        else if(this.band == Band.BAND_R) {
            return "Band R";
        }
        else if(this.band == Band.BAND_DJI) {
            return "Band DJI";
        }

        return "Band L";
    }

    @Override
    public String toString()
    {
        return this.frequency + " MHz " + this.band + " " + this.channel + "CH";
    }
}
