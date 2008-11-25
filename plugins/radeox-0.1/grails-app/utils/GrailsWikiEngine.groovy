import org.radeox.api.engine.WikiRenderEngine
import org.radeox.engine.BaseRenderEngine

public class GrailsWikiEngine extends BaseRenderEngine implements WikiRenderEngine {

    boolean exists(String name) {
        null != Page.findByTitle(name)
    }

    boolean showCreate() {
        true
    }

    void appendLink(StringBuffer buffer, String name, String view) {
        def id = Page.findByTitle(name)?.id
        if (id) {
           buffer << "<A HREF='$id'>$view</A>"
        }  else {
           appendCreateLink(buffer, name, view)
        }
    }

    void appendLink(StringBuffer buffer, String name, String view, String anchor) {
        buffer << "called with anchor: $anchor"
    }

    void appendCreateLink(StringBuffer buffer, String name, String view) {
        buffer << "[create <A HREF='../create?title=${URLEncoder.encode(name)}'>$view</A>]"
    }

}