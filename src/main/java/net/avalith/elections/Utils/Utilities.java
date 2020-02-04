package net.avalith.elections.Utils;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Component
public class Utilities {

    public String generateUUID(){
        return UUID.randomUUID().toString();
    }

    public Timestamp now(){
        return Timestamp.from(Instant.now());
    }
}
