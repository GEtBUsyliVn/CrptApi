import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CrptApi {
    private final TimeUnit timeUnit;
    private final int requestLimit;
    private final AtomicInteger requestCount;
    private final Object lock = new Object();

    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        this.timeUnit = timeUnit;
        this.requestLimit = requestLimit;
        this.requestCount = new AtomicInteger(0);
    }

    public void createDocument(Document document, String signature) throws InterruptedException {
        synchronized (lock) {
            while (requestCount.get() >= requestLimit) {
                lock.wait();
            }
            // Simulating API call
            System.out.println("Sending request to create document: " + document.getDocId());
            // Increment request count
            requestCount.incrementAndGet();
            // Simulating API response time
            Thread.sleep(1000);
            // Simulating API response
            System.out.println("Document created successfully: " + document.getDocId());
            // Decrement request count
            requestCount.decrementAndGet();
            // Notify waiting threads
            lock.notifyAll();
        }
    }

    // Inner class representing a document
    public static class Document {
        private String docId;

        public Document(String docId) {
            this.docId = docId;
        }

        public String getDocId() {
            return docId;
        }

        public void setDocId(String docId) {
            this.docId = docId;
        }
    }

    // Inner class representing a signature
    public static class Signature {
        private String signature;

        public Signature(String signature) {
            this.signature = signature;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }
    }

    // Testing the implementation
    public static void main(String[] args) {
        CrptApi crptApi = new CrptApi(TimeUnit.SECONDS, 3);

        // Simulating concurrent API calls
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                try {
                    crptApi.createDocument(new Document("123"), "signature");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}