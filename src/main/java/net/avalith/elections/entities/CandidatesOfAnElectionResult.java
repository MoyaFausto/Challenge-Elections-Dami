package net.avalith.elections.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.avalith.elections.interfaces.CandidateView;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidatesOfAnElectionResult {

    private List<CandidateView> candidates;
}
