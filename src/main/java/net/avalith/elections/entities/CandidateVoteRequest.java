package net.avalith.elections.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateVoteRequest {
    @JsonProperty("candidate_id")
    private Integer candidateId;
}
