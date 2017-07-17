package me.xaanit.apparatus.internal.json.github;

import com.google.gson.annotations.SerializedName;

public class GithubResponse {
    public String url;
    public String forks_url;
    public String commits_url;
    public String id;
    public String git_pull_url;
    public String git_push_url;
    public String html_url;
    public GithubFile files;
    @SerializedName("public")
    public boolean isPublic;
    public String created_at;
    public String updated_at;
    public String description;
    public int comments;
    // There should be a user here but fuck that
    public String comments_url;
    // Should be forks here but fuck that
    // Should be history here but fuck that
    public boolean truncated;
}
