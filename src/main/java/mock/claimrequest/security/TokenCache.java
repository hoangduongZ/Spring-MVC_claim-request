package mock.claimrequest.security;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenCache {
    private final ConcurrentHashMap<String, String> tokenStore = new ConcurrentHashMap<>();

    public void put(String token, String email) {
        tokenStore.put(token, email);
        new Thread(() -> {
            try {
                Thread.sleep(15 * 60 * 1000);
                tokenStore.remove(token);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    public String get(String token) {
        return tokenStore.get(token);
    }

    public boolean exists(String token) {
        return tokenStore.containsKey(token);
    }

    public void remove(String token) {
        tokenStore.remove(token);
    }
}

