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

import java.util.ArrayList;

import florian.felix.flesch.fpvvideochannelsorter.Pilot;

public class Sorter {

    private Sorter(){}

    public static ArrayList<Pilot> sort(ArrayList<Pilot> pilots, boolean considerIMD) {

        ArrayList<Pilot> solution = new ArrayList<>();

        for(int i=0; i<pilots.size(); i++) {
            Pilot p = pilots.get(i).getCopy();
            p.setFrequency(p.getAvailableFrequencies().get(0));
            solution.add(p);
        }

        ArrayList<Pilot> remainingP = new ArrayList<>();
        for(int i=1; i<pilots.size(); i++) {
            remainingP.add(pilots.get(i).getCopy());
        }

        ArrayList<Pilot> finalSolution = solution;

        for(int i=0; i<pilots.get(0).getAvailableFrequencies().size(); i++) {

            ArrayList<Pilot> newSolution = new ArrayList<>();
            Pilot p = pilots.get(0).getCopy();
            p.setFrequency(p.getAvailableFrequencies().get(i));
            newSolution.add(p);

            finalSolution = recursion(remainingP, newSolution, finalSolution, getMinDisCoeff(finalSolution, considerIMD), considerIMD);
        }


        /*for(int i=0; i<finalSolution.size(); i++) {
            System.out.println(finalSolution.get(i).getFrequency());
        }
        int[] minDis = getMinDisVector(finalSolution);
        System.out.println("MinDis: " + minDis[0]);
        System.out.println("MinDisIMD: " + minDis[1]);
        System.out.println("IMDRating: " + minDis[2]);*/

        return finalSolution;
    }

    private static ArrayList<Pilot> recursion(ArrayList<Pilot> remainingP, ArrayList<Pilot> newSolution, ArrayList<Pilot> solution, int minDis, boolean considerIMD) {
        for(int i=remainingP.get(0).getAvailableFrequencies().size()-1; 0<=i; i--) { //go throu each remaindingP.get(0) elements
            ArrayList<Pilot> newSolutionCopy = new ArrayList<>();
            newSolutionCopy.addAll(newSolution);

            Pilot p = remainingP.get(0).getCopy();
            p.setFrequency(p.getAvailableFrequencies().get(i));
            newSolutionCopy.add(p);

            int newMinDis = getMinDisCoeff(newSolutionCopy, considerIMD);
            if(minDis < newMinDis) { //current path could be better than solution
                ArrayList<Pilot> newRemaindingP = new ArrayList<>();
                for(int j=1; j<remainingP.size(); j++) {
                    newRemaindingP.add(remainingP.get(j));
                }

                if(newRemaindingP.size() == 0) { //no elements left to add -> new better solution found
                    minDis = newMinDis;
                    solution = newSolutionCopy;
                }
                else { //still elements to add -> call recursion
                    solution = recursion(newRemaindingP, newSolutionCopy, solution, minDis, considerIMD);
                    minDis = getMinDisCoeff(solution, considerIMD);
                }
            }
            //else do nothing the current path is already worse as a found solution
        }

        return solution;
    }

    public static int getMinDis(ArrayList<Pilot> solution) {
        int minDis = Integer.MAX_VALUE;

        for(int i=0; i<solution.size(); i++) {
            for(int j=0; j<solution.size(); j++) {
                if(i != j) {
                    int dis = Math.abs(solution.get(i).getFrequency().getFrequenz() - solution.get(j).getFrequency().getFrequenz());
                    if(dis < minDis) {
                        minDis = dis;
                    }
                    if (minDis == 0) {
                        return minDis;
                    }
                }
            }
        }

        return minDis;
    }

    public static int getMaxDis(ArrayList<Pilot> solution) {
        int maxDis = Integer.MIN_VALUE;

        for(int i=0; i<solution.size(); i++) {
            for(int j=0; j<solution.size(); j++) {
                if(i != j) {
                    int dis = Math.abs(solution.get(i).getFrequency().getFrequenz() - solution.get(j).getFrequency().getFrequenz());
                    if(dis > maxDis) {
                        maxDis = dis;
                    }
                }
            }
        }

        return maxDis;
    }

    public static int[] getMinDisVector(ArrayList<Pilot> solution) {
        int[] vector = new int[] {Integer.MAX_VALUE, Integer.MAX_VALUE, 0};
        ArrayList<Integer> freqsIMD = new ArrayList<>(solution.size()*solution.size());
        for(int i=0; i<solution.size(); i++) {
            for(int j=0; j<solution.size(); j++) {
                if(i != j) {
                    vector[0] = Math.min(vector[0], Math.abs(solution.get(i).getFrequency().getFrequenz() - solution.get(j).getFrequency().getFrequenz()));
                    if (vector[0] == 0) return new int[]{0,0,0};
                    freqsIMD.add((solution.get(i).getFrequency().getFrequenz() * 2) - solution.get(j).getFrequency().getFrequenz());
                }
            }
        }
        for(int i=0; i<solution.size(); i++)
            for(int j=0; j<freqsIMD.size(); j++) {
                int freq = solution.get(i).getFrequency().getFrequenz();
                int freqIMD = freqsIMD.get(j);
                if (freqIMD < 5100 || freqIMD > 6099) continue;
                int diff = Math.abs(freqIMD - freq);
                vector[1] = Math.min(vector[1], diff);
                if (vector[1] == 0) {
                    vector[2] = 0;
                    return vector;
                }
                if (diff < 35) {
                    vector[2] += (35 - diff) * (35 - diff);
                }
            }
        vector[2] = 100 - vector[2]/5/solution.size();
        return vector;
    }

    private static int getMinDisCoeff(ArrayList<Pilot> solution, boolean considerIMD) {
        if (considerIMD) {
            int[] vector = getMinDisVector(solution);
            return vector[0] + (1000*vector[1]);
        } else {
            return getMinDis(solution);
        }
    }
}
