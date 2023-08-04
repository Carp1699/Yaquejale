import java.util.Arrays;

class Starter {

    public static void startRunnables(Runnable[] runnables) {
        Arrays.stream(runnables).forEach(runnable -> new Thread(runnable).start());
    }
}