package net.avalith.elections.service;

import net.avalith.elections.Utils.ErrorMessage;
import net.avalith.elections.entities.FakeUsers;
import net.avalith.elections.entities.MessageResponse;
import net.avalith.elections.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FakeUserService {

    private final Logger logger = LoggerFactory.getLogger(FakeUserService.class);

    @Autowired
    private UserService userService;

    public MessageResponse generateFakeUser(Integer quantity){

        String apiUsersUrl = "https://randomuser.me/api/?nat=es&results="+quantity;

        RestTemplate restTemplate = new RestTemplate();
        FakeUsers fakeUsers = restTemplate.getForObject(apiUsersUrl, FakeUsers.class);

        if(Objects.isNull(fakeUsers))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.FAILURE_GENERATING_FAKE_USERS);

        List<User> users = fakeUsers.getResults().stream()
                .map(fakeUser -> User.builder()
                        .name(fakeUser.getName().getFirst())
                        .lastname(fakeUser.getName().getLast())
                        .isFake(1)
                        .id(UUID.randomUUID().toString())
                        .dni(10000000 + new Random().nextInt(50000000))
                        .build())
                .collect(Collectors.toList());

        this.userService.save(users);

        return new MessageResponse("Usuarios agregados correctamente");
    }
}
