package net.avalith.elections.service;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import net.avalith.elections.Utils.Utilities;
import net.avalith.elections.entities.FakeUser;
import net.avalith.elections.entities.FakeUserDetails;
import net.avalith.elections.entities.FakeUserId;
import net.avalith.elections.entities.FakeUserLogin;
import net.avalith.elections.entities.FakeUsers;
import net.avalith.elections.entities.MessageResponse;
import net.avalith.elections.entities.UserListResponse;
import net.avalith.elections.entities.UserResponse;
import net.avalith.elections.model.User;
import net.avalith.elections.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
public class TestUserService {

    @Value("${api.url,randomuser}")
    private  String apiUrlRandomUser;

    @Autowired
    UserService userService;

    @Autowired
    @MockBean
    UserRepository userRepository;

    @Autowired
    @MockBean
    Utilities utilities;

    @Autowired
    @MockBean
    RestTemplate restTemplate;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findByIdTest(){
        String id = "06f8847c-2133-4d89-94b4-24a39695bc76";

        User userExpected = User.builder().name("Damian").lastname("Tacconi").id(id).dni(231231).build();

        Mockito.when(userRepository.findById(id))
                .thenReturn(Optional.of(userExpected));

        User user = userService.findById(id);

        Assert.assertEquals(userExpected , user);
        verify(userRepository,times(1)).findById(id);
    }


    @Test
    public void deleteTest(){
        String id = "06f8847c-2133-4d89-94b4-24a39695bc76";
        User user = User.builder()
                .id(id)
                .name("Damian")
                .lastname("Tacconi")
                .build();

        Mockito.when(userRepository.findById(id)).thenReturn(Optional.ofNullable(user));

        userService.delete(id);
        Mockito.verify(userRepository, times(1)).delete(user);
    }

    @Test
    public void saveTest(){
        String uuid = "06f8847c-2133-4d89-94b4-24a39695bc76";
        User userRequest = User.builder().dni(29123821).lastname("Tacconi").name("Damian").build();
        User userResponse = User.builder().id(uuid).dni(29123821).lastname("Tacconi").name("Damian").build();

        when(utilities.generateUUID()).thenReturn(uuid);
        when(userRepository.save(userRequest)).thenReturn(userResponse);
        UserResponse expected = new UserResponse(userResponse.getId());

        Assert.assertEquals(expected,userService.save(userRequest));
        verify(userRepository,times(1)).save(userRequest);
    }

    @Test
    public void saveAllTest(){
        List<User> usersToSave = Arrays.asList(
                User.builder().dni(2312323).lastname("Tacconi").name("Damian").build(),
                User.builder().dni(2321413).lastname("Diaz").name("Martin").build());

        userService.save(usersToSave);

        verify(userRepository,times(1)).saveAll(usersToSave);
    }

    @Test
    public void findAllTest(){
        List<User> usersInDB = Arrays.asList(
                User.builder().id("06f8847c-2133-4d89-94b4-24a39695bc76")
                        .isFake(0)
                        .dni(2312213)
                        .name("Carlos")
                        .lastname("Tevez").build(),
                User.builder().id("02f8847c-2133-4d89-94b4-24a39695bc71")
                        .isFake(1)
                        .dni(23123123)
                        .name("Martin")
                        .lastname("Palermo")
                        .build());

        //test
        when(userRepository.findAll()).thenReturn(usersInDB);
        UserListResponse userListResponse = userService.findAll();

        Assert.assertEquals(2,userListResponse.getUsers().size());
        verify(userRepository, times(1)).findAll();
    }


    @Test
    public void generateFakeUsersTest(){
        Integer quantity = 2;

        FakeUser fakeUser1 = FakeUser.builder()
                    .id(FakeUserId.builder().value("32134231").build())
                    .login(FakeUserLogin.builder().uuid("06f8847c-2133-4d89-94b4-24a39695bc76").build())
                    .name(FakeUserDetails.builder().first("Jorge").last("Sanchez").build())
                .build();
        FakeUser fakeUser2 = FakeUser.builder()
                .id(FakeUserId.builder().value("32134233").build())
                .login(FakeUserLogin.builder().uuid("16r8847c-2133-4d89-94b4-24a39695bc76").build())
                .name(FakeUserDetails.builder().first("Ramiro").last("Rodriguez").build())
                .build();
        FakeUsers fakeUsers = FakeUsers.builder().results(Arrays.asList(fakeUser1,fakeUser2)).build();

        List<User> generatedUsers = fakeUsers.getResults().stream()
               .map(fakeUser -> User.builder()
                       .name(fakeUser.getName().getFirst())
                       .lastname(fakeUser.getName().getLast())
                       .isFake(1)
                       .id(fakeUser.getLogin().getUuid())
                       .dni(Integer.parseInt(fakeUser.getId().getValue().replaceAll("[^\\d.]", "")))
                       .build())
               .collect(Collectors.toList());

        String apiUsersUrl = apiUrlRandomUser + "&results=" + 2;

        when(restTemplate.getForObject(apiUsersUrl,FakeUsers.class)).thenReturn(fakeUsers);

        MessageResponse messageResponse = userService.generateFakeUser(quantity);

        Mockito.verify(userRepository, times(1)).saveAll(generatedUsers);

        Assert.assertEquals("Usuarios agregados correctamente" , messageResponse.getMessage());
    }

    @Test
    public void updateTest(){
        String id = "16r8847c-2133-4d89-94b4-24a39695bc76";

        User newUser = User.builder()
                .name("Martin")
                .lastname("Diaz")
                .build();

        User oldUser = User.builder()
                .id(id)
                .name("Damian")
                .lastname("Tacconi")
                .build();
        User userUpdated = User.builder().id(id).name("Martin").lastname("Diaz").build();


        when(userRepository.findById(id)).thenReturn(Optional.ofNullable(oldUser));
        assert oldUser != null;
        oldUser.setLastname(newUser.getLastname());
        oldUser.setName(newUser.getName());
        oldUser.setDni(newUser.getDni());

        when(userRepository.save(oldUser)).thenReturn(userUpdated);

        userService.update(newUser,id);

        verify(userRepository,times(1)).findById(id);
        verify(userRepository,times(1)).save(oldUser);
    }


    @Test
    public void findFakesTest(){
        List<User> fakesUsersInDB = Arrays.asList(
                User.builder().id("06f8847c-2133-4d89-94b4-24a39695bc76")
                        .isFake(1)
                        .dni(2312213)
                        .name("Carlos")
                        .lastname("Tevez").build(),
                User.builder().id("02f8847c-2133-4d89-94b4-24a39695bc71")
                        .isFake(1)
                        .dni(23123123)
                        .name("Martin")
                        .lastname("Palermo")
                        .build());

        when(userRepository.findByIsFake(1)).thenReturn(fakesUsersInDB);
        List<User> userList = userService.findFakes();

        Assert.assertEquals(2,userList.size());
        verify(userRepository, times(1)).findByIsFake(1);
    }
}
