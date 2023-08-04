// You can experiment here, it won’t be checked

public class Task {
  public static void main(String[] args) {
    Thread thread = new Thread(() -> {
      while(Thread.currentThread().isInterrupted()) {
        try {
          Thread.sleep(2_000);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      }
    });
    thread.start();
    thread.interrupt();
    System.out.println(Thread.currentThread().isInterrupted());
  }
}
