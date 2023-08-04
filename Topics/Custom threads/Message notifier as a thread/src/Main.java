class MessageNotifier extends Thread {

    // write fields to store variables here
    String name;
    int repeat;


    public MessageNotifier(String msg, int repeats) {
        this.name = msg;
        this.repeat = repeats;
    }

    @Override
    public void run() {
        for (int i = 0; i < repeat; i++) {
            System.out.println(this.name);
        }
    }
}