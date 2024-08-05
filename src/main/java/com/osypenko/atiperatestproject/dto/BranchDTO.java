package com.osypenko.atiperatestproject.dto;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BranchDTO {
    private String branchName;
    private String commitSha;
}
