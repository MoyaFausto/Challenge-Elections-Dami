package net.avalith.elections.Utils;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class Utilities {

    public String generateUUID(){
        return UUID.randomUUID().toString();
    }


}
