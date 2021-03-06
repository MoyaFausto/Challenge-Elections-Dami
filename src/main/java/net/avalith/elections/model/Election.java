package net.avalith.elections.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "elections")
public class Election {

    @Id
    @GeneratedValue
    @JsonProperty(value = "id_election")
    private Integer id;

    @JsonProperty(value = "start_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @FutureOrPresent
    private LocalDateTime startDate;

    @JsonProperty(value = "end_date")
    @FutureOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "election")
    private List<ElectionCandidate> electionCandidates;

    @ToString.Exclude
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "election")
    private List<ElectionHistory> electionHistories;
}
