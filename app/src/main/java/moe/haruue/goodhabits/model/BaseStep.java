package moe.haruue.goodhabits.model;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class BaseStep {
    // 标题，将会直接传送给 Task
    public String title = "";
    // 这一步的内容，将会直接传送给 Task
    public String content = "";
    // 类型
    public String type = "";
    // 预留，图片 Url，将会直接传送给 Task
    public String imageUrl = "";
    // 默认的 note ，将会直接传送给 Task 的 note
    public String defaultNote = "";

    public BaseStep() {

    }

    public BaseStep(Task metaTask) {
        this(metaTask.title,
                metaTask.content,
                metaTask.type,
                metaTask.imageUrl,
                metaTask.note);
    }

    public BaseStep(String title,
                    String content,
                    String type,
                    String imageUrl,
                    String defaultNote) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.imageUrl = imageUrl;
        this.defaultNote = defaultNote;
    }
}
