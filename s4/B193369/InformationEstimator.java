package s4.B193369;

// Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 
import java.lang.*;
import s4.specification.*;

/* What is imported from s4.specification
package s4.specification;
public interface InformationEstimatorInterface{
    void setTarget(byte target[]); // set the data for computing the information quantities
    void setSpace(byte space[]); // set data for sample space to computer probability
    double estimation(); // It returns 0.0 when thåe target is not set or Target's length is zero;
// It returns Double.MAX_VALUE, when the true value is infinite, or space is not set.
// The behavior is undefined, if the true value is finete but larger than Double.MAX_VALUE.
// Note that this happens only when the space is unreasonably large. We will encounter other problem anyway.
// Otherwise, estimation of information quantity, 
}                        
*/

public class InformationEstimator implements InformationEstimatorInterface {
	// Code to tet, *warning: This code condtains intentional problem*
	byte[] myTarget; // data to compute its information quantity
	byte[] mySpace; // Sample space to compute the probability
	FrequencerInterface myFrequencer; // Object for counting frequency

	byte[] subBytes(byte[] x, int start, int end) {
		// corresponding to substring of String for byte[] ,
		// It is not implement in class library because internal structure of byte[]
		// requires copy.
		byte[] result = new byte[end - start];
		for (int i = 0; i < end - start; i++) {
			result[i] = x[start + i];
		}
		;
		return result;
	}

	// IQ: information quantity for a count, -log2(count/sizeof(space))
	double iq(int freq) {
		return -Math.log10((double) freq / (double) mySpace.length) / Math.log10((double) 2.0);
	}

	public void setTarget(byte[] target) {
		myTarget = target;
	}

	public void setSpace(byte[] space) {
		myFrequencer = new Frequencer();
		mySpace = space;
		myFrequencer.setSpace(space);
	}

	// double f(byte[] target, int start, int end) {
	// double valuer = Double.MAX_VALUE;
	// double[] valuef = new double[target.length];

	// byte[] targetf = new byte[end - start];
	// for (int i = 0; i < end - start; i++) {
	// targetf[i] = target[i];
	// }

	// myFrequencer.setTarget(subBytes(targetf, start, end));
	// valuef[0] = iq(myFrequencer.frequency());

	// for (int mid = 1; mid < end; mid++) {
	// myFrequencer.setTarget(subBytes(targetf, mid, end));
	// valuef[mid] = f(targetf, start, mid) + iq(myFrequencer.frequency());
	// }

	// for (int i = 0; i < end; i++) {
	// valuer = Math.min(valuer, valuef[i]);
	// }
	// return valuer;
	// }

	public double estimation() {
		boolean[] partition = new boolean[myTarget.length + 1];
		int np;
		np = 1 << (myTarget.length - 1);
		// System.out.println("np="+np+" length="+myTarget.length);
		double value = Double.MAX_VALUE; // value = mininimum of each "value1".

		// for (int p = 0; p < np; p++) { // There are 2^(n-1) kinds of partitions.
		// binary representation of p forms partition.
		// for partition {"ab" "cde" "fg"}
		// a b c d e f g : myTarget
		// T F T F F T F T : partition:
		// partition[0] = true; // I know that this is not needed, but..
		// for (int i = 0; i < myTarget.length - 1; i++) {
		// partition[i + 1] = (0 != ((1 << i) & p));
		// }
		// partition[myTarget.length] = true;

		// Compute Information Quantity for the partition, in "value1"
		// value1 = IQ(#"ab")+IQ(#"cde")+IQ(#"fg") for the above example
		double value1 = (double) 0.0;
		int end = 0;
		int start = end;
		end = myTarget.length;

		/*
		 * while(start<myTarget.length) { // System.out.write(myTarget[end]); end++;;
		 * while(partition[end] == false) { // System.out.write(myTarget[end]); end++; }
		 * // System.out.print("("+start+","+end+")");
		 * myFrequencer.setTarget(subBytes(myTarget, start, end)); value1 = value1 +
		 * iq(myFrequencer.frequency()); start = end; } // System.out.println(" "+
		 * value1);
		 */

		double targetf[] = new double[myTarget.length];

		for (int i = 0; i < myTarget.length; i++) {
			if (i == 0) {
				myFrequencer.setTarget(subBytes(myTarget, 0, 1));
				targetf[i] = iq(myFrequencer.frequency());
			} else {
				double temp = Double.MAX_VALUE;
				myFrequencer.setTarget(subBytes(myTarget, start, i + 1));
				targetf[i] = iq(myFrequencer.frequency());

				for (int j = i; j > 0; j--) {
					myFrequencer.setTarget(subBytes(myTarget, j, i + 1));
					temp = targetf[j - 1] + iq(myFrequencer.frequency());

					if (temp < targetf[i]) {
						targetf[i] = temp;
					}
				}
			}
		}

		// Get the minimal value in "value"
		if (targetf[targetf.length - 1] < value) {
			value = targetf[targetf.length - 1];
		}

		return value;
	}

	public static void main(String[] args) {
		InformationEstimator myObject;
		double value;
		// long startTime = System.nanoTime();
		myObject = new InformationEstimator();
		myObject.setSpace("3210321001230123".getBytes());
		myObject.setTarget("0".getBytes());
		value = myObject.estimation();
		System.out.println(">0 " + value);
		myObject.setTarget("01".getBytes());
		value = myObject.estimation();
		System.out.println(">01 " + value);
		myObject.setTarget("0123".getBytes());
		value = myObject.estimation();
		System.out.println(">0123 " + value);
		myObject.setTarget("00".getBytes());
		value = myObject.estimation();
		System.out.println(">00 " + value);
		// long endTime = System.nanoTime();

		// System.out.println("開始時刻：" + startTime + " ms");
		// System.out.println("終了時刻：" + endTime + " ms");
		// System.out.println("処理時間：" + (endTime - startTime) + " ms");
	}
}
