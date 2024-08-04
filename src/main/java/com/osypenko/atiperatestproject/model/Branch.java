package com.osypenko.atiperatestproject.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({
        "protected"
})
public class Branch {
    private String name;
    private Commit commit;
}
