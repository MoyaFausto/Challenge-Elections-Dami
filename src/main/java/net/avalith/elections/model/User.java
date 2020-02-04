package net.avalith.elections.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    private String id;

    @NotNull(message = "Insert a valid dni")
    @Column(unique = true)
    private Integer dni;

    @NotNull(message = "Insert a valid name")
    @NotBlank
    private String name;

    @NotNull(message = "Insert a valid lastname")
    @NotBlank
    private String lastname;

    @Column(name = "is_fake")
    private Integer isFake = 0;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Vote> votes;
}
