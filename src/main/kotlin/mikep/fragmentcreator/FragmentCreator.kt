package mikep.fragmentcreator

import org.w3c.dom.Document
import org.w3c.dom.NamedNodeMap
import org.w3c.dom.NodeList
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

fun main(args: Array<String>) {
    if(args.size < 2) {
        print("""Provide all the arguments! "pathToXml" "className"""")
        System.exit(1)
    }
    val filePath = args[0]
    val className = args[1]
    FragmentCreator(filePath, className).buildFragmentClass()
}

class FragmentCreator(filePath: String, className: String) {

    private val xmlDocument: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(File(filePath))
    private var clazz: String = "class $className : Fragment() {\n"
    private val regex = Regex("\\w+\\.xml")
    private val layoutName = "R.layout.${regex.find(filePath)?.value?.replace(".xml", "")}"
    private val file = File("$className.kt")
    fun buildFragmentClass(){
        printChildNodes(xmlDocument.childNodes.item(0).childNodes)
        clazz = clazz.plus("\n\n\toverride fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {\n\t\treturn inflater.inflate($layoutName, container, false)\n\t}")
        clazz = clazz.plus("\n\n}")
        print(clazz)
        file.createNewFile()
        file.writeText(clazz)
    }

    private fun printChildNodes(nodeList: NodeList) {
        for(i in 1 until nodeList.length step 2) {
            val node = nodeList.item(i)
            node.attributes.androidIdOrNull?.let {
                clazz = clazz.plus("\n\tprivate val $it: ${node.nodeName} by findViewById(${it.rId})")
            }
            if(node.hasChildNodes()) printChildNodes(node.childNodes)
        }
    }
}

val NamedNodeMap.androidIdOrNull: String?
    get() = this.getNamedItem("android:id")?.nodeValue?.replace("@+id/", "")

val String.rId : String
    get() = "R.id.$this"