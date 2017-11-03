package lannooo.comsumer;

import lannooo.data.Record;
import lannooo.util.FileUtil;
import lannooo.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Collectors;

public class MarkdownConsumer implements Consumer {
    private static final Logger logger = LoggerFactory.getLogger(MarkdownConsumer.class);

    public static final String defaultFilePath = "../mynote";
    public static final String defaultType = "default";
    private String savePath;

    public MarkdownConsumer() {
        Properties prop = new FileUtil().loadProperty("conf/note.properties");
        savePath = prop.getProperty("directory.md", defaultFilePath);
    }

    @Override
    public boolean accept(final Record record) {
        String type = StringUtil.isEmpty(record.getType()) ? defaultType : record.getType();

        try {
            appendToFile(format(record, type), type + ".md");
        } catch (IOException e) {
            logger.error("Fail to append to fille" + e);
            return false;
        }

        return true;
    }

    private String format(final Record record, String type) {
        String[] keywords = record.getKeyWords();
        String title = record.getTitle();
        String content = record.getContent();
        StringBuilder stringBuilder = new StringBuilder();
        String keys = Arrays.stream(keywords).map(k -> "**" + k + "**").collect(Collectors.joining(";"));
        stringBuilder.append("## " + title + "\n")
                .append(StringUtil.isEmpty(keys) ? "" : "> 关键词 " + keys + "\n\n")
                .append(content + "\n");
        return stringBuilder.toString();
    }

    private void appendToFile(String content, final String target) throws IOException {
        File directory = new File(savePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File targetFile = new File(savePath + File.separator + target);
        if (!targetFile.exists()) {
            targetFile.createNewFile();
        }
        try(FileOutputStream fileOutputStream = new FileOutputStream(targetFile);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
            Writer writer = new BufferedWriter(outputStreamWriter)){
            writer.append(content);
            writer.flush();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
