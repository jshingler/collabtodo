/**
 * This class is used to generate a show / hide tag on the page.
 * Commonly used to show or hide certain div areas.
 * 
 */
class ShowHideTagLib {

    def showHide = { attrs, body ->
    
        def divId = attrs['update']

        // TODO research how to use this with mkp
        out << """<a href="javascript:showhide('$divId');">${body()}</a>"""
    }

    /**
     *  This will pre load the java script for the show hide functionality
     */
    def preLoadShowHide = { attrs, body ->

        out << """<script language="javascript">
            <!--

            function showhide(layer_ref) {
                // lets get the state.
                var state = document.getElementById(layer_ref).style.display;

                if (state == 'block') {
                    state = 'none';
                } else {
                    state = 'block';
                }

                if (document.all) { //IS IE 4 or 5 (or 6 beta)
                    eval( "document.all." + layer_ref + ".style.display = state");
                }

                if (document.layers) { //IS NETSCAPE 4 or below
                    document.layers[layer_ref].display = state;
                }

                if (document.getElementById &&!document.all) {
                    hza = document.getElementById(layer_ref);
                    hza.style.display = state;
                }
            }
            //-->
            </script>
        """
    }
}