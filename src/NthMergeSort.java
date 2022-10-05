import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class NthMergeSort {
    public static void main(String[] args)
    {
        final int[] arr = new int[1000000];
        IntStream.range(0, arr.length).forEach(index -> arr[index] = (int) (Math.random() * 500));
        final int[] arrClone = arr.clone();
        final long startTime = System.currentTimeMillis();
        nthMergeSort(arr, 0, arr.length-1, 30);
        final long stopTime = System.currentTimeMillis();
        System.out.println(String.format("Executing time: %d ms", stopTime - startTime));

        Arrays.sort(arrClone);
        System.out.println(Arrays.equals(arr, arrClone));
    }

    public static void nthMergeSort(final int[] arr, final int leftIndex, final int rightIndex, final int N) throws NullPointerException, ArrayIndexOutOfBoundsException{
        if (arr == null) throw new NullPointerException("arr cannot be null");
        if (leftIndex < 0 || rightIndex >= arr.length) throw new ArrayIndexOutOfBoundsException("Incorrect indexes was passed");
        if (leftIndex < rightIndex){
            final List<Integer> indexes = new ArrayList<>();
            indexes.add(leftIndex);
            for (int i = 0; i < N - 1; ++i){
                indexes.add(leftIndex + ((rightIndex - leftIndex) / N) * (i + 1));
            }
            indexes.add(rightIndex);

            for (int i = 0; i < N; ++i){
                if (i == 0){
                    nthMergeSort(arr, indexes.get(i), indexes.get(i+1), N);
                }
                else{
                    nthMergeSort(arr, indexes.get(i)+1, indexes.get(i+1), N);
                }
            }
            NthMerge(arr, indexes, N);
        }
    }

    private static void NthMerge(final int[] arr, final List<Integer> pointIndexes, final int N){
        List<int[]> subarrays = new ArrayList<>();
        for (int i = 0; i < N; ++i){
            if (i == 0){
                subarrays.add(Arrays.copyOfRange(arr, pointIndexes.get(i), pointIndexes.get(i+1)+1));
            }
            else{
                subarrays.add(Arrays.copyOfRange(arr, pointIndexes.get(i) + 1, pointIndexes.get(i+1) + 1));
            }

        }
        final int[] indexes = new int[subarrays.size() + 1];
        indexes[indexes.length-1] = pointIndexes.get(0);
        List<Integer> remainingIndexes = IntStream.range(0, subarrays.size()).filter(index -> indexes[index] < subarrays.get(index).length).boxed().toList();
        while (!remainingIndexes.isEmpty()){
            final int min = Collections.min(remainingIndexes.stream().map(index -> subarrays.get(index)[indexes[index]]).toList());
            for (final int index : remainingIndexes){
                if (subarrays.get(index)[indexes[index]] == min){
                    arr[indexes[indexes.length - 1]] = min;
                    ++indexes[index];
                    ++indexes[indexes.length-1];
                    break;
                }
            }
            remainingIndexes = IntStream.range(0, subarrays.size()).filter(index -> indexes[index] < subarrays.get(index).length).boxed().toList();
        }
    }
}
