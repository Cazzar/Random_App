package caydedixon;

import com.sun.istack.internal.NotNull;

import java.io.PrintStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

//Because of the nature of my menu system (generated at runtime via reflection API, this is here
@SuppressWarnings("UnusedDeclaration")
public class Main {
    private static final PrintStream out = System.out;
    //just a little cache
    static Map<Integer, List<MethodInfo>> methods = new HashMap<>();
    static Scanner scanner = null;

    //
    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {
        while (true)
            getMenu(0);
    }

    @MenuItem(comment = "Math", level = 0)
    public static void mathMenu() {
        getMenu(1);
    }

    @MenuItem(comment = "Dot Product", level = 1)
    public static void dotProduct() {
        int count = getInt("Please enter an amount of elements: ");

        while (count < 0) {
            out.print("Incorrect input, ");
            count = getInt("Please enter an amount of elements: ");
        }

        double[][] input = new double[2][count];

        for (int i = 0; i < count; i++)
            input[0][i] = getDouble("Please enter a number for matrix 1 position " + (i + 1) + ":");
        for (int i = 0; i < count; i++)
            input[1][i] = getDouble("Please enter a number for matrix 2 position " + (i + 1) + ":");

        final double dotProduct = MathUtil.dotProduct(input[0], input[1]);
        System.out.println(dotProduct);
    }

    @MenuItem(comment = "Multiply matrix by a factor", level = 1)
    public static void matrix() {
        final double factor = getDouble("please enter a factor:");

        int col, row;

        col = getInt("Please enter a number of columns:");
        while (col < 0) {
            out.print("Incorrect input, ");
            col = getInt("Please enter a number of columns:");
        }

        row = getInt("Please enter a number of rows:");
        while (row < 0) {
            out.print("Incorrect input, ");
            row = getInt("Please enter a number of rows:");
        }

        double[][] matrix = new double[row][col];

        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                matrix[i][j] = getDouble(String.format("Please enter a value for row %s position %s:", i + 1, j + 1));

        double[][] result = MathUtil.multiplyMatrix(matrix, factor);

        out.println("The resulting matrix is: ");
        for (double[] doubles : result) {
            for (double element : doubles) out.printf("%s ", element);

            out.println();
        }
    }

    @MenuItem(comment = "Java collection sorting", level = 0)
    public static void sortMenu() {
        getMenu(2);
    }

    @MenuItem(comment = "Random sort", level = 2)
    public static void sortRandomly() {
        final Random random = new Random();

        int count = getInt("Please enter an amount of numbers: ");
        while (count < 0) {
            out.print("Incorrect input, ");
            count = getInt("Please enter an amount of numbers: ");
        }

        List<Double> doubles = new ArrayList<>();

        for (int i = 1; i <= count; i++)
            doubles.add(getDouble(String.format("Please enter a number for position %s", i)));

        Collections.sort(doubles, new Comparator<Double>() {
            @Override
            public int compare(Double o1, Double o2) {
                return random.nextInt(2) - 1;
            }
        });

        out.println("The resultant collection when sorted randomly: ");
        out.println(doubles);
    }

    @MenuItem(comment = "Normal sort", level = 2)
     public static void sortNormal() {
        final Random random = new Random();

        int count = getInt("Please enter an amount of numbers: ");
        while (count < 0) {
            out.print("Incorrect input, ");
            count = getInt("Please enter an amount of numbers: ");
        }

        List<Double> doubles = new ArrayList<>();

        for (int i = 1; i <= count; i++)
            doubles.add(getDouble(String.format("Please enter a number for position %s", i)));

        Collections.sort(doubles);

        out.println("The resultant collection when sorted highest to lowest: ");
        out.println(doubles);
    }

    @MenuItem(comment = "Inverse sort", level = 2)
    public static void sortInverse() {
        final Random random = new Random();

        int count = getInt("Please enter an amount of numbers: ");
        while (count < 0) {
            out.print("Incorrect input, ");
            count = getInt("Please enter an amount of numbers: ");
        }

        List<Double> doubles = new ArrayList<>();

        for (int i = 1; i <= count; i++)
            doubles.add(getDouble(String.format("Please enter a number for position %s", i)));

        Collections.sort(doubles);
        Collections.reverse(doubles);

        out.println("The resultant collection when sorted lowest to highest: ");
        out.println(doubles);
    }

    /**
     * Get an int from the user, it has no checking on it, other than it is of type {@link int}
     *
     * @param prompt the prompt to display, "Incorrect input" will be prepended if wrong
     * @return the user inputted int
     */
    private static int getInt(String prompt) {
        if (scanner == null) scanner = new Scanner(System.in);

        out.printf("%s ", prompt);

        while (!scanner.hasNextInt()) {
            out.printf("Incorrect input, %s ", prompt);
            scanner.next();
        }

        return scanner.nextInt();
    }

    /**
     * Get an int from the user, it has no checking on it, other than it is of type {@link double}
     *
     * @param prompt the prompt to display, "Incorrect input" will be prepended if wrong
     * @return the user inputted int
     */
    private static double getDouble(String prompt) {
        if (scanner == null) scanner = new Scanner(System.in);

        out.printf("%s ", prompt);

        while (!scanner.hasNextDouble()) {
            out.printf("Incorrect input, %s ", prompt);
            scanner.next();
        }

        return scanner.nextDouble();
    }

    private static void getMenu(int level) {
        if (!methods.containsKey(level)) {
            methods.put(level, new ArrayList<MethodInfo>());

            //loop through all the methods in the class
            //yes this is slow
            for (Method method : Main.class.getDeclaredMethods()) {
                //get the annotation we want, else null
                final MenuItem annotation = method.getAnnotation(MenuItem.class);
                //if it isnt what we want ignore it.
                if (annotation == null || annotation.level() != level) continue;

                MethodInfo info = new MethodInfo(annotation.comment(), method, annotation.weight());
                //add it to our "Cache"
                methods.get(level).add(info);
            }

            methods.get(level).add(new MethodInfo(level == 0 ? "Exit" : "Back", null, Integer.MAX_VALUE));
        }

        List<MethodInfo> methodInfoList = methods.get(level);
        //Sort the list.
        Collections.sort(methodInfoList);

        for (int i = 0; i < methodInfoList.size(); i++) {
            out.printf("%d: %s%n", i + 1, methodInfoList.get(i).comment);
        }

        if (scanner == null) scanner = new Scanner(System.in);
        int choice;
        do {
            out.print("Please enter a choice: ");

            while (!scanner.hasNextInt()) {
                //CONSUME THAT BITCH!
                scanner = null;

                out.print("Invalid choice, ");
            }

            choice = scanner.nextInt();

            //check if valid
            if (methodInfoList.size() < choice || choice <= 0) {
                out.print("Invalid choice, ");
                continue;
            }

            break;
        } while (true);

        Method m = methodInfoList.get(choice - 1).method;
        if (m == null && level == 0) System.exit(0);
        //Just break on exit
        if (m == null) return;

        try {
            m.setAccessible(true);
            m.invoke(null);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    static class MethodInfo implements Comparable<MethodInfo> {
        final String comment;
        final Method method;
        final int weight;

        MethodInfo(String comment, Method method, int weight) {
            this.comment = comment;
            this.method = method;
            this.weight = weight;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MethodInfo)) return false;

            MethodInfo that = (MethodInfo) o;

            return weight == that.weight
                    && comment.equals(that.comment)
                    && !(method != null ? !method.equals(that.method) : that.method != null);

        }

        @Override
        public int hashCode() {
            int result = comment.hashCode();
            result = 31 * result + (method != null ? method.hashCode() : 0);
            result = 31 * result + weight;
            return result;
        }

        @Override
        public int compareTo(@NotNull MethodInfo o) {
            //for Collections.sort
            return Integer.compare(weight, o.weight) == 0 ? comment.compareTo(o.comment) : Integer.compare(weight, o.weight);
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    private static @interface MenuItem {
        int level();

        String comment();

        int weight() default 0;
    }
}
