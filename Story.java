import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedList;

public class Story {
    public LinkedList<String> story = new LinkedList<String>();

    /**
     * добавить новый элемент в список
     *
     * @param el
     */

    public void addStoryEl(String el) {
        story.add(el);
    }



    /**
     * отсылаем последовательно каждое сообщение из списка
     * в поток вывода данному клиенту (новому подключению)
     *
     * @param writer
     */

    public LinkedList<String> printStory(BufferedWriter writer) {
        if (story.size() > 0) {
            try {
                writer.write("History messages" + "\n");
                for (String vr : story) {
                    writer.write(vr + "\n");
                }
                writer.write("/...." + "\n");
                writer.flush();
            } catch (IOException ignored) {
            }

        }

        return story;
    }
}

