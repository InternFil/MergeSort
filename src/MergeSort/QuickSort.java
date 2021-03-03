package MergeSort;

/**
 * Класс для сортировки строк и целочисленных значений
 */
public class QuickSort {

    /**
     * Метод для сортировки строк. Используется быстрая сортировка
     * @param arr       массив строк
     * @param orderType необходимый порядок для сортировки
     * @return          отсортированный массив строк
     */
    public String[] sort(String[] arr, TypeOrder orderType) {
        if (arr.length == 0) return null;
        if (arr.length == 1) return arr;
        int leftBorder = 0;
        int rightBorder = arr.length - 1;

        switch (orderType) {
            case ASCENDING:
                sortStringAscending(arr, leftBorder, rightBorder);
                break;
            case DESCENDING:
                sortStringDescending(arr, leftBorder, rightBorder);
                break;
        }
        return arr;
    }

    /**
     * Метод для сортировки целочисленных значений. Используется быстрая сортировка
     * @param arr       массив чисел
     * @param orderType необходимый порядок для сортировки
     * @return          отсортированный массив чисел
     */
    public int[] sort(int[] arr, TypeOrder orderType) {
        if (arr.length == 0) return null;
        if (arr.length == 1) return arr;
        int leftBorder = 0;
        int rightBorder = arr.length - 1;

        switch (orderType) {
            case ASCENDING:
                sortIntAscending(arr, leftBorder, rightBorder);
                break;
            case DESCENDING:
                sortIntDescending(arr, leftBorder, rightBorder);
                break;
        }
        return arr;
    }

    private void sortStringAscending(String[] arr, int leftBorder, int rightBorder) {
        int leftCursor = leftBorder;
        int rightCursor = rightBorder;
        String pivot = arr[(leftBorder + rightBorder) / 2];

        while (leftCursor <= rightCursor) {
            while (arr[leftCursor].compareTo(pivot) < 0) {
                leftCursor++;
            }
            while (arr[rightCursor].compareTo(pivot) > 0) {
                rightCursor--;
            }
            if (leftCursor <= rightCursor) {
                String tmp = arr[leftCursor];
                arr[leftCursor] = arr[rightCursor];
                arr[rightCursor] = tmp;
                leftCursor++;
                rightCursor--;
            }
        }
        if (leftCursor < rightBorder) {
            sortStringAscending(arr, leftCursor, rightBorder);
        }
        if (leftBorder < rightCursor) {
            sortStringAscending(arr, leftBorder, rightCursor);
        }
    }

    private void sortStringDescending(String[] arr, int leftBorder, int rightBorder) {
        int leftCursor = leftBorder;
        int rightCursor = rightBorder;
        String pivot = arr[(leftBorder + rightBorder) / 2];

        while (leftCursor <= rightCursor) {
            while (arr[leftCursor].compareTo(pivot) > 0) {
                leftCursor++;
            }
            while (arr[rightCursor].compareTo(pivot) < 0) {
                rightCursor--;
            }
            if (leftCursor <= rightCursor) {
                String tmp = arr[leftCursor];
                arr[leftCursor] = arr[rightCursor];
                arr[rightCursor] = tmp;
                leftCursor++;
                rightCursor--;
            }
        }
        if (leftCursor < rightBorder) {
            sortStringDescending(arr, leftCursor, rightBorder);
        }
        if (leftBorder < rightCursor) {
            sortStringDescending(arr, leftBorder, rightCursor);
        }
    }

    private void sortIntAscending(int[] arr, int leftBorder, int rightBorder) {
        int leftCursor = leftBorder;
        int rightCursor = rightBorder;
        int pivot = arr[(leftBorder + rightBorder) / 2];

        while (leftCursor <= rightCursor) {
            while (arr[leftCursor] < pivot) {
                leftCursor++;
            }
            while (arr[rightCursor] > pivot) {
                rightCursor--;
            }
            if (leftCursor <= rightCursor) {
                int tmp = arr[leftCursor];
                arr[leftCursor] = arr[rightCursor];
                arr[rightCursor] = tmp;
                leftCursor++;
                rightCursor--;
            }
        }
        if (leftCursor < rightBorder) {
            sortIntAscending(arr, leftCursor, rightBorder);
        }
        if (leftBorder < rightCursor) {
            sortIntAscending(arr, leftBorder, rightCursor);
        }
    }

    private void sortIntDescending(int[] arr, int leftBorder, int rightBorder) {
        int leftCursor = leftBorder;
        int rightCursor = rightBorder;
        int pivot = arr[(leftBorder + rightBorder) / 2];

        while (leftCursor <= rightCursor) {
            while (arr[leftCursor] > pivot) {
                leftCursor++;
            }
            while (arr[rightCursor] < pivot) {
                rightCursor--;
            }
            if (leftCursor <= rightCursor) {
                int tmp = arr[leftCursor];
                arr[leftCursor] = arr[rightCursor];
                arr[rightCursor] = tmp;
                leftCursor++;
                rightCursor--;
            }
        }
        if (leftCursor < rightBorder) {
            sortIntDescending(arr, leftCursor, rightBorder);
        }
        if (leftBorder < rightCursor) {
            sortIntDescending(arr, leftBorder, rightCursor);
        }
    }
}
