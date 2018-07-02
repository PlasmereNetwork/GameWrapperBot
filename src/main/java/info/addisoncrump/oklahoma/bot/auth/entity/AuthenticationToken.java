package info.addisoncrump.oklahoma.bot.auth.entity;

import lombok.NonNull;

import java.security.SecureRandom;

public final class AuthenticationToken implements Comparable<AuthenticationToken> {
    private final String token;

    public AuthenticationToken(final @NonNull SecureRandom random) {
        this.token = String.format(
                "%06d",
                random.nextLong() % (int) 1e6
        );
    }

    public AuthenticationToken(final @NonNull String token) {
        this.token = token;
    }

    @Override
    public int hashCode() {
        return token.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof String) {
            return this.toString().equals(obj);
        } else if (obj instanceof AuthenticationToken) {
            return this.toString().equals(obj.toString());
        }
        return false;
    }

    public String toString() {
        return token;
    }

    @Override
    public int compareTo(final AuthenticationToken o) {
        return o.token.compareTo(this.token);
    }
}
