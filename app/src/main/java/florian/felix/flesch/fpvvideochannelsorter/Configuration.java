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

import java.util.List;

public class Configuration {
	private List<Pilot> pilots;
	private int minFrequency = 5362;
	private int maxFrequency = 5945;
	private boolean considerIMD = true;
	private String saveName;

	public Configuration(List<Pilot> pilots, String saveName) {
		this.pilots = pilots;
		this.saveName = saveName;
	}

	public Configuration(List<Pilot> pilots, String saveName, int minFrequency, int maxFrequency, boolean considerIMD) {
		this.pilots = pilots;
		this.saveName = saveName;
		this.minFrequency = minFrequency;
		this.maxFrequency = maxFrequency;
		this.considerIMD = considerIMD;
	}

	public List<Pilot> getPilots () {
		return this.pilots;
	}

	public String getSaveName() {
		return this.saveName;
	}

	public int getMinFrequency() {
		return this.minFrequency;
	}

	public int getMaxFrequency() {
		return this.maxFrequency;
	}

	public boolean isConsiderIMD() { return considerIMD; }
}
