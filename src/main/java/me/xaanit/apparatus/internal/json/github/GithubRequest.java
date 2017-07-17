package me.xaanit.apparatus.internal.json.github;

import com.google.gson.annotations.SerializedName;

public class GithubRequest {
    private String description;
    @SerializedName("public")
    private boolean isPublic;
    GithubFile files;

    public GithubRequest(String description, boolean isPublic, String name, String content) {
        this.description = description;
        this.isPublic = isPublic;
        this.files = new GithubFile(name, content);
    }

    public GithubRequest(String content) {
        this("Info on all shards.", false, "Shardinfo.txt", content);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public GithubFile getFiles() {
        return files;
    }
}
