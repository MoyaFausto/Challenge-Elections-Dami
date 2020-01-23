package net.avalith.elections.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import net.avalith.elections.model.User;

import java.util.List;

@Data
@AllArgsConstructor
@ToString
public class UserListResponse {
    private List<User> users;
}
