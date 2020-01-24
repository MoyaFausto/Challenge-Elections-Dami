package net.avalith.elections.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Entity
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Table(name = "candidates")
public class Candidate {
    @Id
    private String id;

    @NotBlank
    @NotNull
    private String name;

    @NotBlank
    @NotNull
    private String lastname;
}
