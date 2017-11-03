package lannooo.data;

public class Record {
    String type;
    String title; //nullable
    String[] keyWords;
    String content;

    public Record() {
    }

    public Record(String type, String title, String[] keyWords, String content) {
        this.type = type;
        this.title = title;
        this.keyWords = keyWords;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String[] keyWords) {
        this.keyWords = keyWords;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "["+type+"]"+title+"("+keyWords.toString()+")\n"+content;
    }
}
