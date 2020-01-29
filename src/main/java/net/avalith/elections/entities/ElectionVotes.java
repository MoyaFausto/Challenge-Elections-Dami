package net.avalith.elections.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ElectionVotes {
    @JsonProperty("id_election")
    private Integer idElection;
    private List<CandidateVotes> candidates;
    @JsonProperty("total_votes")
    private Integer totalVotes;
}
