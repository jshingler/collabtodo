
class GroovyMacro extends org.radeox.macro.Preserved {

    String name               = "groovy"
    String description        = "formatting Groovy code"
    String[] paramDescription = ["no params, Groovy code goes inside a pair of groovy tags"];

    GroovyMacro() {
        '[]{}*-\\'.each { addSpecial(it as char) }
    }

    void execute(Writer writer, org.radeox.macro.parameter.MacroParameter params) {
        writer << "<PRE>"
        writer << replace(params.content)        
        writer << "</PRE>"
    }

    String getName() {
        return "GroovyMacro"
    }
}
