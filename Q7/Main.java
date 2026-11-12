import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
public class Main {
    static final int TOTAL = 10000;
    static final String FOLDER = "answer_scripts";
    static class Result {
        boolean success;
        Result(boolean success) {
            this.success = success;
        }
    }
    static class Stats {
        int success;
        int failed;
        long time;
        Stats(int success, int failed, long time) {
            this.success = success;
            this.failed = failed;
            this.time = time;
        }
        void display(String method) {
            System.out.println("\n" + method);
            System.out.println("Successful files: " + success);
            System.out.println("Failed files: " + failed);
            System.out.println("Total processed: " + (success + failed));
            System.out.println("Time taken: " + time + " ms");
        }
    }

    static void createFiles() throws IOException {
        Path folder = Paths.get(FOLDER);
        Files.createDirectories(folder);

        try (java.util.stream.Stream<Path> paths = Files.list(folder)) {
            paths.forEach(path -> {
                try {
                    Files.deleteIfExists(path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        for (int i = 0; i < TOTAL; i++) {

            if (i % 100 == 0) {
                continue;
            }

            Path file = folder.resolve("student" + i + ".txt");

            if (i % 100 == 1) {
                Files.writeString(file, "INVALID_SCORE");
            } else {
                int score = i % 101;
                Files.writeString(file, String.valueOf(score));
            }
        }
    }

    static Result processOne(
            int id, ConcurrentLinkedQueue<String> errors) {

        Path file = Paths.get(
            FOLDER, "student" + id + ".txt"
        );

        try {
            String data = Files.readString(file).trim();
            int score = Integer.parseInt(data);

            if (score < 0 || score > 100) {
                throw new IllegalArgumentException("Invalid score");
            }

            String grade = score >= 50 ? "PASS" : "FAIL";

            return new Result(true);

        } catch (Exception e) {
            errors.add("Student " + id + ": " + e.getMessage());
            return new Result(false);
        }
    }

    static Stats runThreadPool(int poolSize)
            throws Exception {

        ExecutorService executor =
            Executors.newFixedThreadPool(poolSize);

        ConcurrentLinkedQueue<String> errors =
            new ConcurrentLinkedQueue<>();

        List<Future<Result>> futures = new ArrayList<>();

        long start = System.currentTimeMillis();

        for (int i = 0; i < TOTAL; i++) {
            final int id = i;

            Callable<Result> task =
                () -> processOne(id, errors);

            futures.add(executor.submit(task));
        }

        int success = 0;
        int failed = 0;

        for (Future<Result> future : futures) {
            if (future.get().success) {
                success++;
            } else {
                failed++;
            }
        }

        executor.shutdown();

        long end = System.currentTimeMillis();

        return new Stats(success, failed, end - start);
    }

    static Stats runParallelStream() {

        ConcurrentLinkedQueue<String> errors =
            new ConcurrentLinkedQueue<>();

        AtomicInteger success = new AtomicInteger();
        AtomicInteger failed = new AtomicInteger();

        long start = System.currentTimeMillis();

        IntStream.range(0, TOTAL).parallel().forEach(i -> {
            if (processOne(i, errors).success) {
                success.incrementAndGet();
            } else {
                failed.incrementAndGet();
            }
        });

        long end = System.currentTimeMillis();

        return new Stats(
            success.get(), failed.get(), end - start
        );
    }

    static Stats runOneThreadPerFile() throws Exception {

        ConcurrentLinkedQueue<String> errors =
            new ConcurrentLinkedQueue<>();

        AtomicInteger success = new AtomicInteger();
        AtomicInteger failed = new AtomicInteger();

        long start = System.currentTimeMillis();

        for (int batch = 0; batch < TOTAL; batch += 100) {

            List<Thread> threads = new ArrayList<>();

            for (int i = batch;
                 i < Math.min(batch + 100, TOTAL); i++) {

                final int id = i;

                Thread thread = new Thread(() -> {
                    if (processOne(id, errors).success) {
                        success.incrementAndGet();
                    } else {
                        failed.incrementAndGet();
                    }
                });

                threads.add(thread);
                thread.start();
            }

            for (Thread thread : threads) {
                thread.join();
            }
        }

        long end = System.currentTimeMillis();

        return new Stats(
            success.get(), failed.get(), end - start
        );
    }

    public static void main(String[] args) {

        try {
            System.out.println("Creating 10,000 answer-script files...");
            createFiles();
            System.out.println("Files created successfully.");

            for (int size : new int[]{1, 2, 4, 8}) {
                Stats result = runThreadPool(size);
                result.display("ExecutorService - " + size + " threads");
            }

            Stats parallel = runParallelStream();
            parallel.display("Parallel Stream");

            Stats individual = runOneThreadPerFile();
            individual.display("One Thread Per File");

            System.out.println("\nProcessing completed.");

        } catch (Exception e) {
            System.out.println("Program error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
