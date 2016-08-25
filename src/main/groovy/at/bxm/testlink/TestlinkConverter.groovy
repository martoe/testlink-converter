package at.bxm.testlink

import groovy.xml.MarkupBuilder
import org.apache.poi.openxml4j.exceptions.InvalidFormatException
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.WorkbookFactory

class TestlinkConverter {

  String convert(File excelFile) {
    if (!excelFile.exists() || !excelFile.canRead() || !excelFile.file) {
      throw new FileNotFoundException("Cannot read local file $excelFile.absolutePath")
    }
    def wb = WorkbookFactory.create(excelFile)
    if (wb.numberOfSheets > 1 && wb.activeSheetIndex != 0) {
      throw new InvalidFormatException("File contains multiple sheets - which one to use?")
    }
    return processSheet(wb.getSheetAt(0))
  }

  String processSheet(Sheet sheet) {
    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)
    xml.testcases {
      testcase(internalid: "6", name: "Test Case") {
        node_order(1000)
        version(1)
        summary("Test Case Summary")
        preconditions()
        execution_type(1)
        importance(2)
        estimated_exec_duration {}
        status(1)
        is_open(1)
        active(1)
        steps {
          step {
            step_number(1)
            actions("<p> step 1 </p>")
            expectedresults("<p> result 1 </p>")
            execution_type(1)
          }
          step {
            step_number(2)
            actions("<p> step 2 </p>")
            expectedresults("<p> result 2 </p>")
            execution_type(1)
          }
        }
      }
    }
    return writer as String
  }
}
