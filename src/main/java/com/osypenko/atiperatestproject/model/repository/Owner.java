package com.osypenko.atiperatestproject.model.repository;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({
        "id",
        "node_id",
        "avatar_url",
        "gravatar_id",
        "url",
        "html_url",
        "followers_url",
        "followers_url",
        "following_url",
        "gists_url",
        "starred_url",
        "subscriptions_url",
        "organizations_url",
        "repos_url",
        "events_url",
        "received_events_url",
        "type",
        "site_admin"
})
public record Owner(
        String login
) {}
