package me.xaanit.apparatus.internal.json.github;

public class GithubFile {
    private GithubFileInner shardinfo;
    public GithubFile(String name, String content) {
        this.shardinfo = new GithubFileInner(name, content);
    }
    public GithubFile(){}
}

class GithubFileInner {
    private String name;
    private String content;

    protected GithubFileInner(String name, String content) {
        this.name = name;
        this.content = content;
    }
    public GithubFileInner() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "GithubFile{" +
                "name='" + name + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
