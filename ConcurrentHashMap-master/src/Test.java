import java.util.concurrent.*;

public class Test {
    public static void main(String [] args) throws InterruptedException {
        int THREADS_MAX = 42;
        int ITERATIONS = 10;
        for (int numThreads = 1; numThreads < THREADS_MAX; numThreads++) {
            final ConcurrentHashMap myConcurrentHashMap = new ConcurrentHashMap();

            ExecutorService threadPool = Executors.newFixedThreadPool(numThreads);
            long startMillis = System.currentTimeMillis();
            for (int i = 0; i < numThreads; i++) {
                threadPool.submit(new Runnable() {
                    public void run() {
                        for (long i = 0; i < ITERATIONS; i++) {
                            int nextInt = ThreadLocalRandom.current().nextInt(100);
                            myConcurrentHashMap.put(nextInt,nextInt);

                            // myConcurrentHashMap.get(nextInt);
                            //  myConcurrentHashMap.remove(nextInt);


    		     /*   map.put(3, 4);
    		        map.put(5, 8);
    		        map.put(7, 9);
    		        map.put(7, 19);
    		        map.put(8, 18);
    		        map.put(9, 188);
    		        map.put(10, 159);
    		        map.put(11, 1569);
    		        map.put(12, 159);
    		        map.put(13, 1599);
    		        map.put(14 , 1559);
    		        map.put(15 , 1559);
    		        map.put(134 , 15549);*/

    		    /*		System.out.println(map.get(15));
    			        System.out.println(map.get(3));
    			        System.out.println(map.get(5));
    			        System.out.println(map.get(7));
    			        System.out.println(map.get(8));
    			        System.out.println(map.get(9));
    			        System.out.println(map.get(10));
    			        System.out.println(map.get(11));
    			        System.out.println(map.get(12));
    			        System.out.println(map.get(13));
    			        System.out.println(map.get(14));
    			        System.out.println(map.get(15));*/
                        }
                    }
                });
            }
            threadPool.shutdown();
            threadPool.awaitTermination(5, TimeUnit.MINUTES);
            long totalMillis = System.currentTimeMillis() - startMillis;
            double throughput = (double)(numThreads * ITERATIONS ) / (double) totalMillis;
            System.out.println(String.format("%s with %d threads: %dms (throughput: %.1f ops/s)", ConcurrentHashMap.class.getSimpleName(), numThreads, totalMillis, throughput));
            //System.out.println( numThreads+ " 			 "+ totalMillis+"                                "+throughput);
        }


    }
}
