public static Runnable createRunnable(String text,int repeats){
        return()->{
        for(int i=0;i<repeats; i++){
        System.out.println(text);
        }
        };
        }