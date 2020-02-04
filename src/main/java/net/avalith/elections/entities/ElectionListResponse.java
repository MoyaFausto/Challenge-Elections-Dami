package net.avalith.elections.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.avalith.elections.model.Election;

import java.util.List;

@Data
@AllArgsConstructor
public class ElectionListResponse {
    private List<Election> elections;
}
