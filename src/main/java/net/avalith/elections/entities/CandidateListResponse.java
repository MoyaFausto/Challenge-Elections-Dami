package net.avalith.elections.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import net.avalith.elections.model.Candidate;

import java.util.List;

@Data
@AllArgsConstructor
@ToString
public class CandidateListResponse {
    @JsonIgnoreProperties("id")
    List<Candidate> candidates;
}
