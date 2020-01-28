package net.avalith.elections.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ElectionRequest {
    @JsonProperty(value = "start_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @FutureOrPresent
    private LocalDateTime startDate;

    @JsonProperty(value = "end_date")
    @FutureOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    @JsonProperty(value = "candidate_ids")
    private Set<Integer> candidateIds;
}
