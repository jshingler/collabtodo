import org.radeox.engine.context.BaseRenderContext
import org.radeox.api.engine.context.RenderContext
import org.radeox.api.engine.RenderEngine
import org.radeox.api.engine.WikiRenderEngine
import org.radeox.engine.BaseRenderEngine
import org.radeox.filter.*
import org.radeox.macro.*

class RadeoxTagLib { 

    static final RenderContext CONTEXT 
    static final RenderEngine  ENGINE

    static {
        CONTEXT = new BaseRenderContext()
        ENGINE = new GrailsWikiEngine()
        ENGINE.init()
        CONTEXT.renderEngine = ENGINE

        def repo = MacroRepository.getInstance()
        repo.put('groovy', new GroovyMacro())
    }


    def radeoxRender = { attribs, body ->
println "What is the body - " +body()    
        out << ENGINE.render(body(), CONTEXT)
    }
}

