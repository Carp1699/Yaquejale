class Create {

    public static Runnable createRunnable(String text, int repeats) {
        Runnable anonimus = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < repeats; i++) {
                    System.out.println(text);
                }
            }
        };

        return anonimus;
    }
}
