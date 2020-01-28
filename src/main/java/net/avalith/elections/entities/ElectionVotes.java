package net.avalith.elections.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class ElectionVotes {
    private Integer id_election;
    private List<CandidateVotes> candidates;
    private Integer total_votes;
}
