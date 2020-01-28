package net.avalith.elections.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class CandidateVotes {
    private Integer id_candidate;
    private String first_name;
    private String last_name;
    private Integer votes;
}
