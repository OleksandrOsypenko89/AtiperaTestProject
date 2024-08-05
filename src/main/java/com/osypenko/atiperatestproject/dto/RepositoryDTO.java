package com.osypenko.atiperatestproject.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RepositoryDTO {
    private String repositoryName;
    private String ownerLogin;
    private List<BranchDTO> branches;
}
